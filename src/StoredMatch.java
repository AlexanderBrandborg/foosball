public class StoredMatch extends Match {
    private final String id;
    public StoredMatch(String id, Team homeTeam, Team awayTeam, Integer homeScore, Integer AwayScore) {
        super(homeTeam, awayTeam, homeScore, AwayScore);
        this.id = id;
    }

    public StoredMatch(String id, Team homeTeam, Team awayTeam) {
        super(homeTeam, awayTeam, null, null);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
