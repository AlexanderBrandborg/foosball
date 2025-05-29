import java.util.Objects;

public class StoredPlayer extends Player {
    private String id;

    public StoredPlayer(String id, String name, String initials, Integer handicap) throws FoosballException {
        super(name, initials, handicap);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void update(String name, String initials) throws FoosballException {
        String newName = (name != null && !name.isEmpty()) ? name : this.getName();
        String newInitials = (initials != null && !initials.isEmpty()) ? initials : this.getInitials();

        if(!super.areInitialsAndNameLegal(newName, newInitials)) {
            throw new FoosballException("Initials are not contained within name", 400);
        }
        super.name = newName;
        super.initials = newInitials;
    }

    public void increaseHandicap() {
        handicap += 1;
    }

    public void decreaseHandicap() {
        handicap -= 1;
    }
}
