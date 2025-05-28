public class Player {

    protected String name;
    protected String initials;
    protected int handicap;

    protected boolean areInitialsAndNameLegal(String name, String initials) {
        // Test for empty strings
        if(name.trim().isEmpty() || initials.trim().isEmpty()) {
            return false;
        }
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


    public Player(String name, String initials, Integer handicap) {
        if(!areInitialsAndNameLegal(name, initials)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.initials = initials;

        if(handicap == null) {
            this.handicap = 10;
        }else {
            this.handicap = handicap;
        }
    }

    // TODO: Maybe get rid of this constructor
    public Player(String name, String initials) {
        this(name, initials, null);
    }

    public Player(PlayerInput playerInput) {
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
