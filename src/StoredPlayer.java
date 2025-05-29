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

    public void update(String name, String initials) {
        if(!super.areInitialsAndNameLegal(name, initials)) {
            throw new IllegalArgumentException();
        }
        super.name = name;
        super.initials = initials;
    }

    public void increaseHandicap() {
        handicap += 1;
    }

    public void decreaseHandicap() {
        handicap -= 1;
    }
}
