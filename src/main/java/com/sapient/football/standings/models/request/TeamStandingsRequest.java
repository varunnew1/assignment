package com.sapient.football.standings.models.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class TeamStandingsRequest {
    private String countryName;
    private String leagueName;
    private String teamName;

    public TeamStandingsRequest(String countryName, String leagueName, String teamName) {
        this.countryName = countryName;
        this.leagueName = leagueName;
        this.teamName = teamName;
    }
}
