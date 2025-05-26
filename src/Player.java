import org.bson.types.ObjectId;

public final class Player {

    private ObjectId id;
    private String name;
    private String initials;
    private Integer handicap;


    // Needs here for the mongoDB conversion
    public Player() {
    }

    public Player(String name, String initials, Integer handicap) {
        this.name = name;
        this.initials = initials;
        this.handicap = handicap;
    }

    public Player(String name, String initials) {
        this.name = name;
        this.initials = initials;
        this.handicap = 10; // Default value
    }

    public ObjectId getId() {
        return id;
    }

    public Player setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    public String getInitials() {
        return initials;
    }

    public Player setInitials(String initials) {
        this.initials = initials;
        return this;
    }

    public Integer getHandicap() {
        return handicap;
    }

    public Integer setHandicap(Integer handicap) {
        this.handicap = handicap;
        return handicap;
    }

    public void increaseHandicap() {
        handicap += 1;
    }

    public void decreaseHandicap() {
        handicap -= 1;
    }
}
