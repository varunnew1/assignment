package com.sapient.football.standings;

import com.sapient.football.standings.clients.FootballApiClient;
import com.sapient.football.standings.exceptions.ValidationException;
import com.sapient.football.standings.models.client.response.Country;
import com.sapient.football.standings.models.client.response.League;
import com.sapient.football.standings.models.client.response.Standings;
import com.sapient.football.standings.models.request.TeamStandingsRequest;
import com.sapient.football.standings.service.FootballDataService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FootballDataServiceTest {

    @Mock
    private FootballApiClient client;

    @InjectMocks
    FootballDataService footballDataService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testFindCountryFromList(){
        assertNull(footballDataService.findCountryFromList(null, null));
        assertNull(footballDataService.findCountryFromList(new ArrayList<>(), null));
        assertNull(footballDataService.findCountryFromList(Arrays.asList(Country.builder().build()), null));
        assertNull(footballDataService.findCountryFromList(Arrays.asList(Country.builder().countryName("abc").build()),
                "def"));
        assertEquals("abc", footballDataService.findCountryFromList(Arrays.asList(Country.builder().countryName("abc").build()),
                "abc").getCountryName());
        assertNull(footballDataService.findCountryFromList(Arrays.asList(Country.builder().countryName("abC").build()),
                "abc"));
    }

    @Test
    public void testFindLeagueFromList(){
        assertNull(footballDataService.findLeagueFromList(null, null));
        assertNull(footballDataService.findLeagueFromList(new ArrayList<>(), null));
        assertNull(footballDataService.findLeagueFromList(Arrays.asList(League.builder().build()), null));
        assertNull(footballDataService.findLeagueFromList(Arrays.asList(League.builder().leagueName("abc").build()),
                "def"));
        assertEquals("abc", footballDataService.findLeagueFromList(Arrays.asList(League.builder().leagueName("abc").build()),
                "abc").getLeagueName());
        assertNull(footballDataService.findLeagueFromList(Arrays.asList(League.builder().leagueName("abC").build()),
                "abc"));
    }

    @Test
    public void testFindStandingsFromList(){
        assertNull(footballDataService.findTeamStandingFromList(null, null));
        assertNull(footballDataService.findTeamStandingFromList(new ArrayList<>(), null));
        assertNull(footballDataService.findTeamStandingFromList(Arrays.asList(Standings.builder().build()), null));
        assertNull(footballDataService.findTeamStandingFromList(Arrays.asList(Standings.builder().teamName("abc").build()),
                "def"));
        assertEquals("abc", footballDataService.findTeamStandingFromList(Arrays.asList(Standings.builder().teamName("abc").build()),
                "abc").getTeamName());
        assertNull(footballDataService.findTeamStandingFromList(Arrays.asList(Standings.builder().teamName("abC").build()),
                "abc"));
    }

    @Test
    public void testGetStandingsForInvalidCountry() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("country data not found for null");
        footballDataService.getStandings(TeamStandingsRequest.builder().build());
    }

    @Test
    public void testGetStandingsForInvalidLeague() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("league data not found for null");
        Mockito.when(client.getCountries()).thenReturn(Arrays.asList(Country.builder().countryName("abc").build()));
        footballDataService.getStandings(TeamStandingsRequest.builder().countryName("abc").build());
    }

    @Test
    public void testGetStandingsForInvalidTeam() {
        expectedException.expect(ValidationException.class);
        expectedException.expectMessage("standings data not found for null");
        Mockito.when(client.getCountries()).thenReturn(Arrays.asList(Country.builder().countryName("abc").build()));
        Mockito.when(client.getLeaguesByCountryId(Mockito.any())).thenReturn(Arrays.asList(League.builder().leagueName("def").build()));
        footballDataService.getStandings(TeamStandingsRequest.builder().countryName("abc").leagueName("def").build());
    }

    @Test
    public void testGetStandingsForValidTeam() {
        Mockito.when(client.getCountries()).thenReturn(Arrays.asList(Country.builder().countryName("abc").build()));
        Mockito.when(client.getLeaguesByCountryId(Mockito.any())).thenReturn(Arrays.asList(League.builder().leagueName("def").build()));
        Mockito.when(client.getStandingsByLeagueId(Mockito.any())).thenReturn(Arrays.asList(Standings.builder().teamName("ghi").build()));
        assertNotNull(footballDataService.getStandings(TeamStandingsRequest.builder().countryName("abc").leagueName("def").teamName("ghi").build()));
    }
}
