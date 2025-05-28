import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    private StoredPlayer player1, player2, player3, player4;
    private Team homeTeam, awayTeam;
    private StoredMatch match;

    @Before
    public void beforeFunction(){
        this.player1 = new StoredPlayer("1", "Test", "T", 10);
        this.player2 = new StoredPlayer("2", "Test", "T", 10);
        this.player3 = new StoredPlayer("3", "Test", "T", 10);
        this.player4 = new StoredPlayer("4", "Test", "T", 10);

        this.homeTeam = new Team(player1, player2);
        this.awayTeam = new Team(player3, player4);

        this.match = new StoredMatch("1", homeTeam, awayTeam);
    }

    @Test
    public void testMatchConstructor() {
        assertEquals(match.getHomeTeam(), homeTeam);
        assertEquals(match.getAwayTeam(), awayTeam);
    }

    @Test
    public void testTeamCantPlayItself() {
        assertThrows(IllegalArgumentException.class, () -> new StoredMatch("1", homeTeam, homeTeam));
    }

    @Test
    public void testPlayerCantBeOnBothTeams() {
        Team teamAlsoWithPlayer1 = new Team(player1, player3);
        assertThrows(IllegalArgumentException.class, () -> new StoredMatch("1", homeTeam, teamAlsoWithPlayer1));
    }

    @Test
    public void testHomeWin() {
        match.recordOutcome(5, 4);
        assertEquals(player1.getHandicap(), 11);
        assertEquals(player2.getHandicap(), 11);
        assertEquals(player3.getHandicap(), 9);
        assertEquals(player4.getHandicap(), 9);
    }

    @Test
    public void testAwayWin() {
        match.recordOutcome(4, 5);
        assertEquals(player1.getHandicap(), 9);
        assertEquals(player2.getHandicap(), 9);
        assertEquals(player3.getHandicap(), 11);
        assertEquals(player4.getHandicap(), 11);
    }

    @Test
    public void testTieWin() {
        match.recordOutcome(5, 5);
        assertEquals(player1.getHandicap(), 10);
        assertEquals(player2.getHandicap(), 10);
        assertEquals(player3.getHandicap(), 10);
        assertEquals(player4.getHandicap(), 10);
    }

    @Test
    public void testHomeWinThenHome() {
        match.recordOutcome(5, 4);
        match.recordOutcome(2, 1);
        assertEquals(player1.getHandicap(), 11);
        assertEquals(player2.getHandicap(), 11);
        assertEquals(player3.getHandicap(), 9);
        assertEquals(player4.getHandicap(), 9);
    }

    @Test
    public void testHomeWinThenAway() {
        match.recordOutcome(5, 4);
        match.recordOutcome(4, 5);
        assertEquals(player1.getHandicap(), 9);
        assertEquals(player2.getHandicap(), 9);
        assertEquals(player3.getHandicap(), 11);
        assertEquals(player4.getHandicap(), 11);
    }

    @Test
    public void testHomeWinThenTie() {
        match.recordOutcome(5, 4);
        match.recordOutcome(4, 4);
        assertEquals(player1.getHandicap(), 10);
        assertEquals(player2.getHandicap(), 10);
        assertEquals(player3.getHandicap(), 10);
        assertEquals(player4.getHandicap(), 10);
    }

    // TODO: Same for Away and Tie first. But maybe table tests instead, using the ol' copy paste a lot here.
}