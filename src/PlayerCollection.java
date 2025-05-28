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
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_INITIALS = "initials";
    private static final String KEY_HANDICAP = "handicap";

    private MongoCollection<Document> players;

    private StoredPlayer doc2Player(Document playerDoc) {
        return new StoredPlayer(
                playerDoc.getObjectId(KEY_ID).toString(),
                playerDoc.getString(KEY_NAME),
                playerDoc.getString(KEY_INITIALS),
                playerDoc.getInteger(KEY_HANDICAP)
        );
    }

    private List<Player> docs2Players(Iterable<Document> playerDocs) {
        List<Player> playerList = new ArrayList<>();
        playerDocs.forEach(p -> playerList.add(doc2Player(p)));
        return playerList;
    }

    public PlayerCollection(MongoClient mongo) {
        this.players = mongo.getDatabase("foosball").getCollection("players");
    }

    public String CreatePlayer(Player player) {
        Document doc = new Document()
                .append (KEY_NAME, player.getName())
                .append(KEY_INITIALS, player.getInitials())
                .append(KEY_HANDICAP, player.getHandicap());

        InsertOneResult result = players.insertOne(doc);

        if(result.wasAcknowledged()){
            return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue().toString();
        }
        else {
            throw new MongoException("Insertion failed");
        }
    }

    public Boolean UpdatePlayer(StoredPlayer player) {
        Document setData = new Document()
                .append (KEY_NAME, player.getName())
                .append(KEY_INITIALS, player.getInitials())
                .append(KEY_HANDICAP, player.getHandicap());

        Document update = new Document();
        update.append("$set", setData);

        UpdateResult result = players.updateOne(eq(KEY_ID, new ObjectId(player.getId())), update);

        return result.wasAcknowledged();
    }

    public StoredPlayer GetPlayer(String id) {
        Document playerDoc = players.find(eq(KEY_ID, new ObjectId(id))).first();
        if (playerDoc == null) {
            return  null;
        }
        return doc2Player(playerDoc);
    }

    public List<Player> GetPlayerByName(String name) {
        return this.docs2Players(players.find(eq(KEY_NAME, name)));
    }

    public List<Player> GetPlayerByInitials(String initials) {
        return this.docs2Players(players.find(eq(KEY_INITIALS, initials)));
    }

    public List<Player> GetPlayers() {
        return this.docs2Players(players.find());
    }

    public void DeletePlayer(String id) {
        players.deleteOne(eq(KEY_ID, new ObjectId(id)));
    }
}
