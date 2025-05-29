import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class MatchCollection {
    private static final String KEY_ID = "_id";
    private static final String KEY_HOMEPLAYER1ID = "homePlayer1Id";
    private static final String KEY_HOMEPLAYER2ID = "homePlayer2Id";
    private static final String KEY_AWAYPLAYER1ID = "awayPlayer1Id";
    private static final String KEY_AWAYPLAYER2ID = "awayPlayer2Id";
    private static final String KEY_HOMESCORE = "homeScore";
    private static final String KEY_AWAYSCORE = "awayScore";

    private PlayerCollection players; // This collection needs knowledge about players
    private MongoCollection<Document> matches;

    public MatchCollection(MongoClient mongo){
        this.players = new PlayerCollection(mongo);
        this.matches = mongo.getDatabase("foosball").getCollection("matches");
    }

    private StoredMatch doc2Match(Document doc) throws FoosballException {
        String id = doc.getObjectId(KEY_ID).toString();
        StoredPlayer homePlayer1 = players.GetPlayer(doc.getString(KEY_HOMEPLAYER1ID));
        StoredPlayer homePlayer2 = players.GetPlayer(doc.getString(KEY_HOMEPLAYER2ID));
        StoredPlayer awayPlayer1 = players.GetPlayer(doc.getString(KEY_AWAYPLAYER1ID));
        StoredPlayer awayPlayer2 = players.GetPlayer(doc.getString(KEY_AWAYPLAYER2ID));
        Integer homeScore = doc.getInteger(KEY_HOMESCORE);
        Integer awayScore = doc.getInteger(KEY_AWAYSCORE);

        return new StoredMatch(id, new Team(homePlayer1, homePlayer2), new Team(awayPlayer1, awayPlayer2), homeScore, awayScore);
    }

    public String CreateMatch(String homePlayer1Id, String homePlayer2Id, String awayPlayer1Id, String awayPlayer2Id) throws FoosballException {
        StoredPlayer homePlayer1 = players.GetPlayer(homePlayer1Id);
        StoredPlayer homePlayer2 = players.GetPlayer(homePlayer2Id);
        StoredPlayer awayPlayer1 = players.GetPlayer(awayPlayer1Id);
        StoredPlayer awayPlayer2 = players.GetPlayer(awayPlayer2Id);

        if(homePlayer1 == null || homePlayer2 == null || awayPlayer1 == null || awayPlayer2 == null){
            throw new FoosballException("homePlayer1 or homePlayer2 or awayPlayer1 or awayPlayer2 is null", 400);
        }

        Match match = new Match(new Team(homePlayer1, homePlayer2), new Team(awayPlayer1, awayPlayer2));
        Document doc =  new Document()
                .append (KEY_HOMEPLAYER1ID, match.getHomeTeam().player1.getId())
                .append (KEY_HOMEPLAYER2ID, match.getHomeTeam().player2.getId())
                .append (KEY_AWAYPLAYER1ID, match.getAwayTeam().player1.getId())
                .append (KEY_AWAYPLAYER2ID, match.getAwayTeam().player2.getId())
                .append(KEY_HOMESCORE, null)
                .append(KEY_AWAYSCORE, null);

        InsertOneResult result = matches.insertOne(doc);

        if(result.wasAcknowledged()){
            return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue().toString();
        }
        else {
            throw new MongoException("Insertion failed");
        }
    }

    public void UpdateMatch(StoredMatch match){
        // Reflects changes in handicap.
        // TODO: Handle case where player is null
        players.UpdatePlayer(match.getHomeTeam().player1);
        players.UpdatePlayer(match.getHomeTeam().player2);
        players.UpdatePlayer(match.getAwayTeam().player1);
        players.UpdatePlayer(match.getAwayTeam().player2);

        // Then update the match with new scores
        Document setData = new Document()
                .append("homeScore", match.getHomeScore())
                .append("awayScore", match.getAwayScore());

        Document update = new Document();
        update.append("$set", setData);
        UpdateResult result = matches.updateOne(eq("_id", new ObjectId(match.getId())), update);
        if(!result.wasAcknowledged()){
            throw new MongoException("Insertion failed");
        }
    }

    public StoredMatch getMatch(String id) throws FoosballException {
        Document matchDoc = matches.find(eq("_id", new ObjectId(id))).first();
        if (matchDoc == null) {
            return  null;
        }
        return doc2Match(matchDoc);
    }
}
