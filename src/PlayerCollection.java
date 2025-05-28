import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class PlayerCollection {
    private MongoCollection<Document> players;

    private StoredPlayer doc2Player(Document playerDoc) {
        return new StoredPlayer(playerDoc.getObjectId("_id").toString(), playerDoc.getString("name"), playerDoc.getString("initials"), playerDoc.getInteger("handicap"));
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

    public String CreatePlayer(Player player) {
        Document doc = new Document()
                .append ("name", player.getName())
                .append("initials", player.getInitials())
                .append("handicap", player.getHandicap());

        InsertOneResult result = players.insertOne(doc);

        if(result.wasAcknowledged()){
            return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue().toString();
        }
        else {
            throw new MongoException("Insertion failed");
        }
    }

    public void UpdatePlayer(StoredPlayer player) {
        Document setData = new Document()
                .append ("name", player.getName())
                .append("initials", player.getInitials())
                .append("handicap", player.getHandicap());

        Document update = new Document();
        update.append("$set", setData);

        UpdateResult result = players.updateOne(eq("_id", new ObjectId(player.getId())), update);

        // TODO: Since we allow for failed updates in some cases. Maybe move error handling out?
        if(!result.wasAcknowledged()){
            throw new MongoException("Update failed");
        }
    }

    public StoredPlayer GetPlayer(String id) {
        Document playerDoc = players.find(eq("_id", new ObjectId(id))).first();
        if (playerDoc == null) {
            return  null;
        }
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
