public class Match {
    private Team homeTeam;
    private Team awayTeam;
    private Integer homeScore = null;
    private Integer awayScore = null;

    enum Outcome {
        HOME,
        AWAY,
        TIE
    }

    public Match(Team homeTeam, Team awayTeam, Integer homeScore, Integer awayScore) {
        if(!homeTeam.isDistinctFromOtherTeam(awayTeam)){
            throw new IllegalArgumentException("Teams share at least one player");
        }
        this.homeTeam= homeTeam;
        this.awayTeam= awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;

    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    private Outcome getOutcome(int _homeScore, int _awayScore) {
        if(_homeScore < _awayScore) {
            return Outcome.AWAY;
        } else if (_homeScore > _awayScore) {
            return Outcome.HOME;
        }
        else {
            return Outcome.TIE;
        }
    }

    private void homeTeamWin(){
        this.homeTeam.IncreaseHandicap();
        this.awayTeam.DecreaseHandicap();
    }

    private void awayTeamWin(){
        this.awayTeam.IncreaseHandicap();
        this.homeTeam.DecreaseHandicap();
    }

    private void updateHandicaps(Outcome outcome){
        switch (outcome) {
            case HOME: this.homeTeamWin(); break;
            case AWAY: this.awayTeamWin(); break;
            case TIE: break;
        }
    }

    private void correctHandicaps(Outcome previousOutcome, Outcome newOutcome){
        // Reset handicaps first
        switch (previousOutcome) {
            case HOME: this.awayTeamWin(); break;
            case AWAY: this.homeTeamWin(); break;
            case TIE: break;
        }

        updateHandicaps(newOutcome);
    }

    public void recordOutcome(int homeScore, int awayScore) {
        Outcome outcome = this.getOutcome(homeScore, awayScore);
        boolean noScoresRegisteredYet = this.homeScore == null && this.awayScore == null;
        if(noScoresRegisteredYet) {
            this.updateHandicaps(outcome);
        } else {
            Outcome previousOutcome = this.getOutcome(this.homeScore, this.awayScore);
            this.correctHandicaps(previousOutcome, outcome);
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
}
