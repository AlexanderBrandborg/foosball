import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class PlayerCollection {
    private MongoCollection<Document> players;

    private Player doc2Player(Document playerDoc) {
        return new Player(playerDoc.getObjectId("_id").toString(), playerDoc.getString("name"), playerDoc.getString("initials"), playerDoc.getInteger("handicap"));
    }

    private List<Player> docs2Players(Iterable<Document> playerDocs) {
        List<Player> playerList = new ArrayList<>();
        playerDocs.forEach(p -> playerList.add(doc2Player(p)));
        return playerList;
    }

    public PlayerCollection(MongoClient mongo) {
        // TODO: Check whether object is in correct format?
        this.players = mongo.getDatabase("foosball").getCollection("players");
    }

    public String GetNewId() {
        return  ObjectId.get().toString();
    }

    public String SetPlayer(Player player) {
        Document doc = new Document("_id", new ObjectId(player.getId()))
                .append ("name", player.getName())
                .append("initials", player.getInitials())
                .append("handicap", player.getHandicap());

        // TODO: Check result here
        InsertOneResult result = players.insertOne(doc);
        return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue().toString();
    }

    public Player GetPlayer(String id) {
        Document playerDoc = players.find(eq("_id", new ObjectId(id))).first();
        // TODO: Better handle id not found
        assert playerDoc != null;

        return doc2Player(playerDoc);
    }

    public List<Player> GetPlayerByName(String name) {
        return this.docs2Players(players.find(eq("name", name)));
    }

    public List<Player> GetPlayerByInitials(String initials) {
        return this.docs2Players(players.find(eq("initials", initials)));
    }

    public List<Player> GetPlayers() {
        return this.docs2Players(players.find());
    }

    public void DeletePlayer(String id) {
        players.deleteOne(eq("_id", new ObjectId(id)));
    }
}
