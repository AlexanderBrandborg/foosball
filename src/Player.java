public class Player {

    protected String name;
    protected String initials;
    protected Integer handicap;

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
        this.handicap = handicap;
    }

    public Player(String name, String initials) {
        this(name, initials, 10);
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
