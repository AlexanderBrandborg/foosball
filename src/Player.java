import java.util.Objects;

public class Player {

    protected String name;
    protected String initials;
    protected int handicap;


    protected boolean areInitialsAndNameLegal(String name, String initials) {
        // Test for legal initials
        String uppercaseName = name.toUpperCase().replaceAll("\\s", "");
        String upperCaseInitials = initials.toUpperCase().replaceAll("\\s", "");;

        for(int i = 0; i < uppercaseName.length(); i++) {
            if(!upperCaseInitials.isEmpty() && uppercaseName.charAt(i) == upperCaseInitials.charAt(0)){
                upperCaseInitials = upperCaseInitials.substring(1);
            }
        }
        return upperCaseInitials.isEmpty();
    }


    public Player(String name, String initials, Integer handicap) throws FoosballException {


        if(name == null || name.trim().isEmpty()) {
            throw new FoosballException("Player name cannot be null or empty", 400);
        }
        if(initials == null || initials.trim().isEmpty()) {
            throw new FoosballException("Player initials cannot be null or empty", 400);
        }
        if(!areInitialsAndNameLegal(name, initials)) {
            throw new FoosballException("Initials are not contained within name", 400);
        }
        this.name = name;
        this.initials = initials;

        if(handicap == null) {
            this.handicap = 10;
        }else {
            this.handicap = handicap;
        }
    }

    public Player(String name, String initials) throws FoosballException {
        this(name, initials, null);
    }

    public Player(PlayerInput playerInput) throws FoosballException {
        this(playerInput.name, playerInput.initials, playerInput.handicap);
    }

    public String getName() {
        return this.name;
    }

    public String getInitials() {
        return this.initials;
    }

    public Integer getHandicap() {
        return this.handicap;
    }
}
