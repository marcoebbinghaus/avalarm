package de.codinghaus.avalarm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "check")
@Getter
@Setter
public class ConfigProperties {
    private int maxFailedRequests;
    private int intervalInSeconds;
    private String url;
    private Discord discord;
}