import org.bson.types.ObjectId;

import java.util.Objects;

public class Player {

    private String name;
    private String initials;
    private Integer handicap;

    public Player(String name, String initials, Integer handicap) {
        this.name = name;
        this.initials = initials;
        this.handicap = handicap;
    }

    public void update(String name, String initials) {
        // TODO: Add rules for updates.
        // Only rule I can think is that initials need to be found in name. They also need to be capitalized
        this.name = name;
        this.initials = initials;
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

    public void increaseHandicap() {
        handicap += 1;
    }

    public void decreaseHandicap() {
        handicap -= 1;
    }

    // TODO: Overwirte getHash as well
}
