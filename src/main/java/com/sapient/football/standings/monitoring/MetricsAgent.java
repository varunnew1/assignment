package com.sapient.football.standings.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MetricsAgent {

    public void incrementAPICount(String statName) {
        log.info("api called {}", statName);
    }

    public void recordResponseCodeCountAPI(String event, int httpCode) {
        String errorSeries = httpCode / 100 + "XX";
        log.info("api event {} errorSeries {}", event, errorSeries);
    }

    public void recordExecutionTimeOfAPI(String eventName, long timeTaken) {
        log.info("api eventName {} timeTaken {}", eventName, timeTaken);
    }

}
