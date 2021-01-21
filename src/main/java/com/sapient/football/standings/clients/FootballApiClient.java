package com.sapient.football.standings.clients;

import com.sapient.football.standings.models.client.response.Country;
import com.sapient.football.standings.models.client.response.League;
import com.sapient.football.standings.models.client.response.Standings;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.sapient.football.standings.constants.Constants.REQUEST_ID;

@Slf4j
@Service
public class FootballApiClient {

    @Autowired
    @Qualifier("footballApiRestTemplate")
    private RestTemplate restTemplate;

    @Value("${client.apiFootball.http.scheme}")
    private String httpScheme;

    @Value("${client.apiFootball.http.host}")
    private String host;

    @Value("${client.apiFootball.http.port}")
    private int port;

    @Value("${client.apiFootball.key}")
    private String key;

    public List<Country> getCountries() {
        try {
            ResponseEntity<List<Country>> responseEntity = restTemplate.exchange(
                    constructUrl("get_countries", null), HttpMethod.GET, new HttpEntity<>(populateHeaders()),
                    new ParameterizedTypeReference<List<Country>>() {
                    });
            log.info("responseEntity {}", responseEntity);
            return responseEntity.getBody();
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    public List<League> getLeaguesByCountryId(String countryId) {
        try {
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("country_id", countryId);
            ResponseEntity<List<League>> responseEntity = restTemplate.exchange(
                    constructUrl("get_leagues", queryParams), HttpMethod.GET, new HttpEntity<>(populateHeaders()),
                    new ParameterizedTypeReference<List<League>>() {
                    });
            log.info("responseEntity {}", responseEntity);
            return responseEntity.getBody();
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    public List<Standings> getStandingsByLeagueId(String leagueId) {
        try {
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("league_id", leagueId);
            ResponseEntity<List<Standings>> responseEntity = restTemplate.exchange(
                    constructUrl("get_standings", queryParams), HttpMethod.GET, new HttpEntity<>(populateHeaders()),
                    new ParameterizedTypeReference<List<Standings>>() {
                    });
            log.info("responseEntity {}", responseEntity);
            return responseEntity.getBody();
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    //TODO test cases
    private String constructUrl(String operation, MultiValueMap<String, String> params) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(httpScheme + "://" + host + ":" + port)
                .queryParam("action", operation)
                .queryParam("APIkey", key)
                .queryParams(params);
        return uriComponentsBuilder.toUriString();
    }

    //TODO test cases
    private HttpHeaders populateHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("requestId", ThreadContext.get(REQUEST_ID));
        return httpHeaders;
    }

    //TODO Improve exception to error mapper
    private void handleException(Exception e) {
        if (e instanceof HttpStatusCodeException) {
            HttpStatusCodeException hse = (HttpStatusCodeException) e;
            log.error("error statusCode {} body {}", hse.getStatusCode(), hse.getResponseBodyAsString(), e);
            throw new RuntimeException(hse.getStatusCode() + " - " + hse.getMessage());
        }
        log.error("error", e);
        throw new RuntimeException(e.getMessage());
    }

}
