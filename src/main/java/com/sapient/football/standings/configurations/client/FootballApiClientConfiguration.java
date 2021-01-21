package com.sapient.football.standings.configurations.client;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class FootballApiClientConfiguration {

    @Value("${client.apiFootball.http.host}")
    private String host;

    @Value("${client.apiFootball.http.port}")
    private int port;

    @Value("${client.apiFootball.http.max.connections}")
    private int maxConnections;

    @Value("${client.apiFootball.http.connect.timeout.ms}")
    private int connectTimeout;

    @Value("${client.apiFootball.http.read.timeout.ms}")
    private int readTimeout;

    @Value("${client.apiFootball.http.request.timeout.ms}")
    private int requestTimeout;

    @Value("${client.apiFootball.http.scheme}")
    private String httpScheme;

    @Value("${client.apiFootball.http.secure}")
    private boolean secureConnection;

    private ClientHttpRequestFactory createRequestFactory() {
        PoolingHttpClientConnectionManager connManager = getPoolConnectionManager();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(requestTimeout)
                .setSocketTimeout(readTimeout)
                .setConnectTimeout(connectTimeout)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(httpClient);

    }

    private PoolingHttpClientConnectionManager getPoolConnectionManager() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        //TODO connManager.setMaxTotal(Integer.MAX_VALUE);
        HttpRoute route = new HttpRoute(new HttpHost(host, port, httpScheme),
                null, secureConnection);
        connManager.setMaxPerRoute(route, maxConnections);
        return connManager;
    }

    @Bean(name = "footballApiRestTemplate")
    protected RestTemplate getRestTemplate() {
        return new RestTemplate(createRequestFactory());
    }
}
