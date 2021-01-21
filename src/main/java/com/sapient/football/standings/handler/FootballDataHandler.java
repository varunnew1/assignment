package com.sapient.football.standings.handler;

import com.sapient.football.standings.models.request.TeamStandingsRequest;
import com.sapient.football.standings.models.response.ApiResponse;
import com.sapient.football.standings.models.response.TeamStandingsResponse;
import com.sapient.football.standings.service.FootballDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FootballDataHandler {

    @Autowired
    private FootballDataService footballDataService;

    public ApiResponse<TeamStandingsResponse> fetchLeagueStandings(String countryName, String leagueName, String teamName) {
        TeamStandingsRequest teamStandingsRequest = TeamStandingsRequest.builder().countryName(countryName).leagueName(leagueName).teamName(teamName).build();
        TeamStandingsResponse teamStandingsResponse = footballDataService.getStandings(teamStandingsRequest);
        return new ApiResponse<>(true, teamStandingsResponse,null, null, null);
    }
}
