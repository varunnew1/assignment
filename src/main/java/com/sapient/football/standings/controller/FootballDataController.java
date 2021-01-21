package com.sapient.football.standings.controller;

import com.sapient.football.standings.handler.FootballDataHandler;
import com.sapient.football.standings.models.response.ApiResponse;
import com.sapient.football.standings.models.response.TeamStandingsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class FootballDataController {

    @Autowired
    private FootballDataHandler footballDataHandler;

    //TODO missing params api response
    @GetMapping(value = "/v1/fetch-league-standings")
    @ResponseBody
    public ApiResponse<TeamStandingsResponse> fetchLeagueStandings(@RequestHeader HttpHeaders httpHeaders,
                                                                   @RequestParam(value = "country_name", required = true) String countryName,
                                                                   @RequestParam(value = "league_name", required = true) String leagueName,
                                                                   @RequestParam(value = "team_name", required = true) String teamName) {
        log.debug("Params: countryName {}, leagueName {}, teamName {}", countryName, leagueName, teamName);
        return footballDataHandler.fetchLeagueStandings(countryName, leagueName, teamName);
    }

}
