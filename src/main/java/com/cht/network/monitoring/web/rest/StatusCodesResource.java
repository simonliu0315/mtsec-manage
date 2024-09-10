package com.cht.network.monitoring.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 提供前端取得StatusCodes相關i18n資源檔.
 *
 * @author chihyu
 * @version 1.0
 */
@Tag(name = "Common_Status", description = "資源檔")
@RestController
@RequestMapping("/api/common/status")
public class StatusCodesResource {

    private final Logger log = LoggerFactory.getLogger(StatusCodesResource.class);

    private final ResourcePatternResolver resolver;

    private final Properties zhProperties;
    private final Properties enProperties;

    public StatusCodesResource(ResourcePatternResolver resolver) throws IOException {
        this.resolver = resolver;
        this.zhProperties = getProperties("zh");
        this.enProperties = getProperties("en");
    }

    @GetMapping("/statusCodes/{lang}")
    @Operation(summary = "取得雙語資源檔")
    public ResponseEntity<Properties> statusCodes(
            @PathVariable("lang") @Parameter(description = "語系", schema = @Schema(allowableValues = {"en", "zh"})) String lang) {

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).getHeaderValue());

        switch (lang) {
            case "zh":
                return ResponseEntity.ok().headers(headers).body(zhProperties);
            case "en":
                return ResponseEntity.ok().headers(headers).body(enProperties);
            default:
                return ResponseEntity.notFound().build();
        }
    }

    private Properties getProperties(String lang) throws IOException {

        String locationPattern;
        switch (lang.toLowerCase()) {
            case "zh":
                locationPattern = "classpath*:**/StatusCodes.properties";
                break;
            case "en":
                locationPattern = "classpath*:**/StatusCodes_en.properties";
                break;
            default:
                throw new RuntimeException("can't find StatusCodes properties");
        }

        Resource[] resources = resolver.getResources(locationPattern);

        Properties properties = new Properties();
        for (Resource resource : resources) {
            log.debug("Initializing statusCodes {}.", resource.getURL().getPath());
            properties.load(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        }
        return properties;
    }
}
