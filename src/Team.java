import java.util.Objects;

public class Team {
    public StoredPlayer player1;
    public StoredPlayer player2;

    public Team(StoredPlayer player1, StoredPlayer player2) {
        if (Objects.equals(player1.getId(), player2.getId())) {
            throw new IllegalArgumentException("Players have the same ID");
        }
        this.player1 = player1;
        this.player2 = player2;
    }

    public Boolean isDistinctFromOtherTeam(Team otherTeam) {
        return this.player1 != otherTeam.player1 && this.player1 != otherTeam.player2
                && this.player2 != otherTeam.player1 && this.player2 != otherTeam.player2;
    }

    public void IncreaseHandicap() {
        this.player1.increaseHandicap();
        this.player2.increaseHandicap();
    }

    public void DecreaseHandicap() {
        this.player1.decreaseHandicap();
        this.player2.decreaseHandicap();
    }
}
