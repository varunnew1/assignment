package com.sapient.football.standings.models.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class League {

    @JsonProperty("country_name")
    private String countryName;

    @JsonProperty("country_id")
    private String countryId;

    @JsonProperty("league_id")
    private String leagueId;

    @JsonProperty("league_name")
    private String leagueName;
}
