public class Team {
    private Player player1;
    private Player player2;

    public Team(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void Loss() {
        player1.decreaseHandicap();
        player2.decreaseHandicap();
    }

    public void Win() {
        player1.increaseHandicap();
        player2.increaseHandicap();
    }
}
