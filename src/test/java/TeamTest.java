import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    void isDistinctFromOtherTeam() throws FoosballException {
        StoredPlayer player1 = new StoredPlayer("1", "Test", "T", 10);
        StoredPlayer player2 = new StoredPlayer("2", "Test", "T", 10);
        Team team1 = new Team(player1, player2);

        StoredPlayer player3 = new StoredPlayer("3", "Test", "T", 10);
        StoredPlayer player4 = new StoredPlayer("4", "Test", "T", 10);
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

        // Works with a player set to null
        Team teamWithMissingFirstPlayer = new Team(null, player4);
        assertEquals(true, team1.isDistinctFromOtherTeam(teamWithMissingFirstPlayer));

        Team teamWithMissingSecondPlayer = new Team(player3, null);
        assertEquals(true, team1.isDistinctFromOtherTeam(teamWithMissingSecondPlayer));

        Team teamWith1MissingPlayer1SharedPlayer = new Team(null, player2);
        assertEquals(false, team1.isDistinctFromOtherTeam(teamWith1MissingPlayer1SharedPlayer));

        Team teamWithMissingPlayers = new Team(null, null);
        assertEquals(true, team1.isDistinctFromOtherTeam(teamWithMissingPlayers));
    }
    @Test
    void increaseHandicap() throws FoosballException {
        StoredPlayer player1 = new StoredPlayer("1", "Test", "T", 10);
        StoredPlayer player2 = new StoredPlayer("2", "Test", "T", 10);
        Team team = new Team(player1, player2);
        team.IncreaseHandicap();
        assertEquals(11, player1.getHandicap());
        assertEquals(11, player2.getHandicap());

        // Nulls
        Team teamWithMissingFirstPlayer = new Team(null, player2);
        teamWithMissingFirstPlayer.IncreaseHandicap();
        assertEquals(12, player2.getHandicap());

        Team teamWithMissingSecondPlayer = new Team(player1, null);
        teamWithMissingSecondPlayer.IncreaseHandicap();
        assertEquals(12, player1.getHandicap());
    }

    @Test
    void decreaseHandicap() throws FoosballException {
        StoredPlayer player1 = new StoredPlayer("1", "Test", "T", 10);
        StoredPlayer player2 = new StoredPlayer("2", "Test", "T", 10);
        Team team = new Team(player1, player2);
        team.DecreaseHandicap();
        assertEquals(9, player1.getHandicap());
        assertEquals(9, player2.getHandicap());

        // Nulls
        Team teamWithMissingFirstPlayer = new Team(null, player2);
        teamWithMissingFirstPlayer.DecreaseHandicap();
        assertEquals(8, player2.getHandicap());

        Team teamWithMissingSecondPlayer = new Team(player1, null);
        teamWithMissingSecondPlayer.DecreaseHandicap();
        assertEquals(8, player1.getHandicap());
    }
}
