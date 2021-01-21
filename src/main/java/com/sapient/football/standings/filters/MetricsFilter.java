package com.sapient.football.standings.filters;

import com.sapient.football.standings.monitoring.MetricsAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
@Slf4j
public class MetricsFilter extends OncePerRequestFilter {

    @Autowired
    private MetricsAgent metricsAgent;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String apiName = ((HttpServletRequest) request).getRequestURI() + "_" + ((HttpServletRequest) request).getMethod();
        metricsAgent.incrementAPICount(apiName);
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long elapsedTime = System.currentTimeMillis() - startTime;
        metricsAgent.recordResponseCodeCountAPI(apiName, response.getStatus());
        metricsAgent.recordExecutionTimeOfAPI(apiName, elapsedTime);
    }
}
