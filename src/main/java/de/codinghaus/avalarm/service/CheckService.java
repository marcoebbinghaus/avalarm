package de.codinghaus.avalarm.service;

import de.codinghaus.avalarm.config.ConfigProperties;
import de.codinghaus.avalarm.model.Results;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final ConfigProperties configProperties;
    private final Results results;
    private final DiscordClient discordClient;
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(50))
            .build();

    @PostConstruct
    public void setup() {
        System.out.printf("Website monitored: '%s' (Interval: %ds, Consecutive failures needed for alarm: %d)%n",
                configProperties.getUrl(), configProperties.getIntervalInSeconds(), configProperties.getMaxFailedRequests());
    }

    @Scheduled(fixedRateString = "${check.interval_in_ms}")
    public void checkUrl() throws IOException, InterruptedException {
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(configProperties.getUrl()))
                .build();
        try {
            final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            results.process(response.statusCode());
            if (results.alarm()) {
                discordClient.informAboutAlarm(results.result());
                results.reset();
            } else {
                System.out.println("all good. (" + LocalDateTime.now() + ")");
            }
        } catch (HttpTimeoutException e) {
            results.process(Results.TIMEOUT);
        }

    }
}
