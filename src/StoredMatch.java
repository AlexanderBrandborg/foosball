public class StoredMatch extends Match {
    private final String id;
    private Integer awayScore;
    private Integer homeScore;


    public StoredMatch(String id, Team homeTeam, Team awayTeam, Integer homeScore, Integer AwayScore) throws FoosballException {
        super(homeTeam, awayTeam);
        this.id = id;
        this.homeScore = homeScore;
        this.awayScore = AwayScore;
    }

    public String getId() {
        return this.id;
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
        this.getHomeTeam().IncreaseHandicap();
        this.getAwayTeam().DecreaseHandicap();
    }

    private void awayTeamWin(){
        this.getAwayTeam().IncreaseHandicap();
        this.getHomeTeam().DecreaseHandicap();
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
        // Then correct them
        updateHandicaps(newOutcome);
    }

    public void recordOutcome(Integer homeScore, Integer awayScore) throws FoosballException {
        if (homeScore == null || awayScore == null) {
            throw new FoosballException("homeScore and awayScore must not be null", 400);
        }

        if (homeScore < 0 || awayScore < 0) {
            throw new FoosballException("homeScore and awayScore must be 0 or larger", 400);
        }

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
