package de.codinghaus.avalarm.model;

import de.codinghaus.avalarm.config.ConfigProperties;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Results {
    public static final int TIMEOUT = 666;
    private int timeouts = 0;
    private int errors5xx = 0;
    private int errors4xx = 0;

    private final ConfigProperties configProperties;

    public void process(int statusCode) {
        if (statusCode == 200) {
            reset();
        } else if (statusCode >= 400 && statusCode <= 499) {
            errors4xx++;
            System.out.println("Caught 400 at " + LocalDateTime.now() + ". Now at " + timeouts);
        } else if (statusCode >= 500 && statusCode <= 599) {
            errors5xx++;
            System.out.println("Caught 500 at " + LocalDateTime.now() + ". Now at " + timeouts);
        } else if (statusCode == 666) {
            timeouts++;
            System.out.println("Caught timeout at " + LocalDateTime.now() + ". Now at " + timeouts);
        }
    }

    public void reset() {
        timeouts = 0;
        errors4xx = 0;
        errors5xx = 0;
    }

    public boolean alarm() {
        final int maxFailedRequests = configProperties.getMaxFailedRequests();
        return (errors4xx >= maxFailedRequests) || (errors5xx >= maxFailedRequests) || (timeouts >= maxFailedRequests);
    }

    public String result() {
        int maxFailedRequests = configProperties.getMaxFailedRequests();
        if (timeouts >= maxFailedRequests) {
            return createResultMessage("Timeouts");
        } else if (errors4xx  >= maxFailedRequests) {
            return createResultMessage("400er Errors");
        } else if (errors5xx  >= maxFailedRequests) {
            return createResultMessage("500er Errors");
        }
        return "Undefined.";
    }

    private String createResultMessage(String errorType) {
        return configProperties.getMaxFailedRequests() + " consecutive " + errorType + "!!";
    }
}
