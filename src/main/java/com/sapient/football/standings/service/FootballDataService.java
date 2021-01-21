package com.sapient.football.standings.service;

import com.sapient.football.standings.clients.FootballApiClient;
import com.sapient.football.standings.exceptions.ValidationException;
import com.sapient.football.standings.models.client.response.League;
import com.sapient.football.standings.models.client.response.Country;
import com.sapient.football.standings.models.client.response.Standings;
import com.sapient.football.standings.models.request.TeamStandingsRequest;
import com.sapient.football.standings.models.response.TeamStandingsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class FootballDataService {

    @Autowired
    private FootballApiClient footballApiClient;

    public TeamStandingsResponse getStandings(TeamStandingsRequest teamStandingsRequest) {
        List<Country> countryList = footballApiClient.getCountries();
        log.debug("countryList {}", countryList);

        Country country = findCountryFromList(countryList, teamStandingsRequest.getCountryName());
        if (country == null) {
            throw new ValidationException("country data not found for " + teamStandingsRequest.getCountryName());
        }
        log.debug("country {}", country);

        List<League> leaguesList = footballApiClient.getLeaguesByCountryId(country.getCountryId());
        log.debug("leaguesList {}", leaguesList);

        League league = findLeagueFromList(leaguesList, teamStandingsRequest.getLeagueName());
        if (league == null) {
            throw new ValidationException("league data not found for " + teamStandingsRequest.getLeagueName());
        }
        log.debug("league {}", league);

        List<Standings> standingsList = footballApiClient.getStandingsByLeagueId(league.getLeagueId());
        log.debug("standingsList {}", standingsList);

        Standings standings = findTeamStandingFromList(standingsList, teamStandingsRequest.getTeamName());
        if (standings == null) {
            throw new ValidationException("standings data not found for " + teamStandingsRequest.getTeamName());
        }
        log.debug("standings {}", standings);

        TeamStandingsResponse teamStandingsResponse = new TeamStandingsResponse();
        teamStandingsResponse.setCountryId(country.getCountryId());
        teamStandingsResponse.setCountryName(country.getCountryName());
        teamStandingsResponse.setLeagueId(league.getLeagueId());
        teamStandingsResponse.setLeagueName(league.getLeagueName());
        teamStandingsResponse.setOverallLeaguePosition(standings.getOverallLeaguePosition());
        teamStandingsResponse.setTeamId(standings.getTeamId());
        teamStandingsResponse.setTeamName(standings.getTeamName());
        return teamStandingsResponse;
    }

    public Country findCountryFromList(List<Country> countryList, String countryName) {
        if (!CollectionUtils.isEmpty(countryList) && countryName != null) {
            return countryList.stream().filter(country -> countryName.equals(country.getCountryName()))
                    .findFirst().orElse(null);
        }
        return null;
    }

    public League findLeagueFromList(List<League> leaguesList, String leagueName) {
        if (!CollectionUtils.isEmpty(leaguesList) && leagueName != null) {
            return leaguesList.stream().filter(league -> leagueName.equals(league.getLeagueName()))
                    .findFirst().orElse(null);
        }
        return null;
    }

    public Standings findTeamStandingFromList(List<Standings> standingsList, String teamName) {
        if (!CollectionUtils.isEmpty(standingsList) && teamName != null) {
            return standingsList.stream().filter(standings -> teamName.equals(standings.getTeamName()))
                    .findFirst().orElse(null);
        }
        return null;
    }

}
