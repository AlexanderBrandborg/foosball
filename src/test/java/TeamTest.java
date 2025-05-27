import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    void isDistinctFromOtherTeam() {
        Player player1 = new Player("1", "Test", "T", 10);
        Player player2 = new Player("2", "Test", "T", 10);
        Team team1 = new Team(player1, player2);

        Player player3 = new Player("3", "Test", "T", 10);
        Player player4 = new Player("4", "Test", "T", 10);
        Team team2 = new Team(player3, player4);

        // Is distinct from team with different players. Distinct goes both ways
        assertEquals(true, team1.isDistinctFromOtherTeam(team2));
        assertEquals(team1.isDistinctFromOtherTeam(team2), team1.isDistinctFromOtherTeam(team2));

        // Is not distinct from itself
        assertEquals(false, team1.isDistinctFromOtherTeam(team1));

        // Is not distinct with one shared player. Goes both ways.
        Team teamSharingPlayerWithTeam1 = new Team(player1, player3);
        assertEquals(false, team1.isDistinctFromOtherTeam(teamSharingPlayerWithTeam1));
        assertEquals(teamSharingPlayerWithTeam1.isDistinctFromOtherTeam(team1), team1.isDistinctFromOtherTeam(teamSharingPlayerWithTeam1));
    }
    @Test
    void increaseHandicap() {
        Player player1 = new Player("1", "Test", "T", 10);
        Player player2 = new Player("2", "Test", "T", 10);
        Team team = new Team(player1, player2);
        team.IncreaseHandicap();
        assertEquals(player1.getHandicap(), 11);
        assertEquals(player2.getHandicap(), 11);
    }

    @Test
    void decreaseHandicap() {
        Player player1 = new Player("1", "Test", "T", 10);
        Player player2 = new Player("2", "Test", "T", 10);
        Team team = new Team(player1, player2);
        team.DecreaseHandicap();
        assertEquals(player1.getHandicap(), 9);
        assertEquals(player2.getHandicap(), 9);
    }
}
