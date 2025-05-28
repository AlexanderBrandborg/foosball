import java.util.Objects;

public class StoredPlayer extends Player {
    private String id;

    public StoredPlayer(String id, String name, String initials, Integer handicap) {
        super(name, initials, handicap);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean equals(StoredPlayer otherPlayer) {
        return Objects.equals(this.getId(), otherPlayer.getId());
    }
}
