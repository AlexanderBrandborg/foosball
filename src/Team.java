import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Team {
    public StoredPlayer player1;
    public StoredPlayer player2;

    public Team(StoredPlayer player1, StoredPlayer player2) throws FoosballException {
        // Accounts for the case where one of the team members are null
        if (player1 != null && player2 != null && player1.getId() == player2.getId()) {
            throw new FoosballException("Players have the same ID", 400);
        }
        this.player1 = player1;
        this.player2 = player2;
    }

    public Set<String> getActiveIds(){
        Set<String> ids = new HashSet<>();
        if (player1 != null) {ids.add(player1.getId());}
        if (player2 != null) {ids.add(player2.getId());}
        return ids;
    }

    public Boolean isDistinctFromOtherTeam(Team otherTeam) {
        return Collections.disjoint(getActiveIds(), otherTeam.getActiveIds());
    }

    public void IncreaseHandicap() {
        if(this.player1 != null){
            this.player1.increaseHandicap();
        }
        if(this.player2 != null){
            this.player2.increaseHandicap();
        }
    }

    public void DecreaseHandicap() {
        if(this.player1 != null){
            this.player1.decreaseHandicap();
        }
        if(this.player2 != null){
            this.player2.decreaseHandicap();
        }
    }
}
