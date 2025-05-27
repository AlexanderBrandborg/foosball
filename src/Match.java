public class Match {
    private Player awayPlayer1;
    private Player awayPlayer2;
    private Player homePlayer1;
    private Player homePlayer2;
    private Integer homeScore; // Make nullable for when unset
    private Integer awayScore; // Make nullable for when unset

    public Match(Player awayPlayer1, Player awayPlayer2, Player homePlayer1, Player homePlayer2) {
        this.awayPlayer1 = awayPlayer1;
        this.awayPlayer2 = awayPlayer2;
        this.homePlayer1 = homePlayer1;
        this.homePlayer2 = homePlayer2;
    }

    public void RecordOutcome(Integer homeScore, Integer awayScore) {
        // If score was already set, we need to update the handicaps if another winner was found.
        // If score was not set, we just set the handicaps.

        this.homeScore = homeScore;
        this.awayScore = awayScore;


    }
}
