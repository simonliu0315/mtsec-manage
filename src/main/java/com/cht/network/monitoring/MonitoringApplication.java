package com.cht.network.monitoring;

import com.cht.network.monitoring.rest.AlertReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages="com.cht.network")
public class MonitoringApplication {

	private static final Logger log = LoggerFactory.getLogger(MonitoringApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(MonitoringApplication.class, args);
	}

}
