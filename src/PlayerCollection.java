import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class PlayerCollection {
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_INITIALS = "initials";
    private static final String KEY_HANDICAP = "handicap";

    private final MongoCollection<Document> players;

    private StoredPlayer doc2Player(Document playerDoc) throws FoosballException {
        return new StoredPlayer(
                playerDoc.getObjectId(KEY_ID).toString(),
                playerDoc.getString(KEY_NAME),
                playerDoc.getString(KEY_INITIALS),
                playerDoc.getInteger(KEY_HANDICAP)
        );
    }

    private List<StoredPlayer> docs2Players(Iterable<Document> playerDocs) throws FoosballException{
        List<StoredPlayer> playerList = new ArrayList<>();
        for (Document p : playerDocs) {
            playerList.add(doc2Player(p));
        }
        return playerList;
    }

    private Document player2Doc(Player p) throws FoosballException {
        return new Document()
                .append (KEY_NAME, p.getName())
                .append(KEY_INITIALS, p.getInitials())
                .append(KEY_HANDICAP, p.getHandicap());
    }

    public PlayerCollection(MongoClient mongo) {
        this.players = mongo.getDatabase("foosball").getCollection("players");
    }

    public String CreatePlayer(Player player) throws FoosballException {
        InsertOneResult result = players.insertOne(player2Doc(player));

        if(result.wasAcknowledged()){
            return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue().toString();
        }
        else {
            throw new MongoException("Insertion failed");
        }
    }

    public void UpdatePlayer(StoredPlayer player) throws FoosballException {
        Document setData = player2Doc(player);
        Document update = new Document();
        update.append("$set", setData);

        UpdateResult result = players.updateOne(eq(KEY_ID, new ObjectId(player.getId())), update);

        if (!result.wasAcknowledged()){
            throw new MongoException("Update failed");
        }
    }

    private void isValidId(String id) throws FoosballException {
        if(!ObjectId.isValid(id)){
            throw new FoosballException("Invalid player id", 400);
        }
    }

    public StoredPlayer GetPlayer(String id) throws FoosballException {
        isValidId(id);
        Document playerDoc = players.find(eq(KEY_ID, new ObjectId(id))).first();
        if (playerDoc == null) {
            return  null;
        }
        return doc2Player(playerDoc);
    }

    public List<StoredPlayer> GetSortedPlayers(String name, String initials) throws FoosballException {
        Bson filter = new Document();
        if(name != null){
            filter = and(filter, eq("name", name));
        }
        if(initials != null){
            filter = and(filter, eq("initials", initials));
        }
        FindIterable<Document> playerDocs = players.find(filter);

        List<StoredPlayer> playerList = this.docs2Players(playerDocs);
        playerList.sort(Comparator.comparing(StoredPlayer::getHandicap).reversed());
        return playerList;
    }

    public void DeletePlayer(String id) throws FoosballException {
        isValidId(id);
        if(GetPlayer(id) == null){
            throw new FoosballException("Player not found", 404);
        };
        players.deleteOne(eq(KEY_ID, new ObjectId(id)));
    }
}
