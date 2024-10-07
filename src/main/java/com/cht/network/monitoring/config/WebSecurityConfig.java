package com.cht.network.monitoring.config;

import com.cht.network.monitoring.config.properties.ChtCorsProperties;
import com.cht.network.monitoring.config.properties.SecurityProperties;
import com.cht.network.monitoring.security.SecurityFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
@EnableConfigurationProperties({SecurityProperties.class, ChtCorsProperties.class})
public class WebSecurityConfig {

    private final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final CorsConfiguration corsConfiguration;

    private final ChtCorsProperties corsProperties;

    private final SecurityProperties securityProperties;

    private final SecurityFilter securityFilter;

    public WebSecurityConfig(CorsConfiguration corsConfiguration, ChtCorsProperties corsProperties,
    SecurityProperties securityProperties, Environment env) {
        this.corsConfiguration = corsConfiguration;
        this.corsProperties = corsProperties;
        this.securityProperties = securityProperties;
        this.securityFilter = new SecurityFilter(securityProperties, env.acceptsProfiles("dev", "init-user"));
    }

    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager(User.withUsername("TEST").password("{noop}12345").roles("USER").build());
    }
    */
    @ConditionalOnMissingBean
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("filterChain");
        http.cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                //.requestMatchers("/domesticCircuit/**").permitAll()
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated())
                .httpBasic(withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if (!CollectionUtils.isEmpty(corsConfiguration.getAllowedOrigins()) || !CollectionUtils.isEmpty(corsConfiguration.getAllowedOriginPatterns())) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", corsConfiguration);
            source.registerCorsConfiguration("/management/**", corsConfiguration);
            source.registerCorsConfiguration("/v2/api-docs", corsConfiguration);
            source.registerCorsConfiguration("/v3/api-docs", corsConfiguration);
            source.registerCorsConfiguration("/**", corsConfiguration);
            if (!CollectionUtils.isEmpty(corsProperties.getIncludes())) {
                corsProperties.getIncludes().forEach(uriPattern -> source.registerCorsConfiguration(uriPattern, corsConfiguration));
            }
        }
        return source;

    }
}
