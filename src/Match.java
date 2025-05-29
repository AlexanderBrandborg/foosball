public class Match {
    private Team homeTeam;
    private Team awayTeam;

    enum Outcome {
        HOME,
        AWAY,
        TIE
    }

    public Match(Team homeTeam, Team awayTeam) throws FoosballException {
        if(!homeTeam.isDistinctFromOtherTeam(awayTeam)){
            throw new FoosballException("Teams share at least one player", 400);
        }
        this.homeTeam= homeTeam;
        this.awayTeam= awayTeam;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }
}
