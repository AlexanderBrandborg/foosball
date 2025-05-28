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
    private PlayerCollection players; // This collection needs knowledge about players
    private MongoCollection<Document> matches;

    public MatchCollection(MongoClient mongo){
        this.players = new PlayerCollection(mongo);
        this.matches = mongo.getDatabase("foosball").getCollection("matches");
    }

    private Document match2Doc(Match match){
        return new Document()
                .append ("homePlayer1Id", match.getHomeTeam().player1.getId())
                .append ("homePlayer2Id", match.getHomeTeam().player2.getId())
                .append ("awayPlayer1Id", match.getAwayTeam().player1.getId())
                .append ("awayPlayer2Id", match.getAwayTeam().player2.getId())
                .append("homeScore", match.getHomeScore())
                .append("awayScore", match.getAwayScore());
    }

    private Document match2Doc(StoredMatch match){
        return new Document()
                .append("_id", match.getId())
                .append ("homePlayer1Id", match.getHomeTeam().player1.getId())
                .append ("homePlayer2Id", match.getHomeTeam().player2.getId())
                .append ("awayPlayer1Id", match.getAwayTeam().player1.getId())
                .append ("awayPlayer2Id", match.getAwayTeam().player2.getId())
                .append("homeScore", match.getHomeScore())
                .append("awayScore", match.getAwayScore());
    }


    // TODO: Make it more explicit that this func actually does a lookup?
    private StoredMatch doc2Match(Document doc){
        String id = doc.getObjectId("_id").toString();
        StoredPlayer homePlayer1 = players.GetPlayer(doc.getString("homePlayer1Id"));
        StoredPlayer homePlayer2 = players.GetPlayer(doc.getString("homePlayer2Id"));
        StoredPlayer awayPlayer1 = players.GetPlayer(doc.getString("awayPlayer1Id"));
        StoredPlayer awayPlayer2 = players.GetPlayer(doc.getString("awayPlayer2Id"));
        Integer homeScore = doc.getInteger("homeScore");
        Integer awayScore = doc.getInteger("awayScore");

        return  new StoredMatch(id, new Team(homePlayer1, homePlayer2), new Team(awayPlayer1, awayPlayer2), homeScore, awayScore);
    }

    public String CreateMatch(String homePlayer1Id, String homePlayer2Id, String awayPlayer1Id, String awayPlayer2Id, Integer homeScore, Integer awayScore){
        StoredPlayer homePlayer1 = players.GetPlayer(homePlayer1Id);
        StoredPlayer homePlayer2 = players.GetPlayer(homePlayer2Id);
        StoredPlayer awayPlayer1 = players.GetPlayer(awayPlayer1Id);
        StoredPlayer awayPlayer2 = players.GetPlayer(awayPlayer2Id);

        if(homePlayer1 == null || homePlayer2 == null || awayPlayer1 == null || awayPlayer2 == null){
            throw new NullPointerException("homePlayer1 or homePlayer2 or awayPlayer1 or awayPlayer2 is null");
        }

        Match match = new Match(new Team(homePlayer1, homePlayer2), new Team(awayPlayer1, awayPlayer2), homeScore, awayScore);
        InsertOneResult result = matches.insertOne(match2Doc(match));

        if(result.wasAcknowledged()){
            return Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue().toString();
        }
        else {
            throw new MongoException("Insertion failed");
        }
    }

    public void UpdateMatch(StoredMatch match){
        // TODO: Roll these into a single update to the db?

        // Reflects changes in handicap.
        players.UpdatePlayer(match.getHomeTeam().player1);
        players.UpdatePlayer(match.getHomeTeam().player2);
        players.UpdatePlayer(match.getAwayTeam().player1);
        players.UpdatePlayer(match.getAwayTeam().player2);

        Document setData = new Document()
                .append("homeScore", match.getHomeScore())
                .append("awayScore", match.getAwayScore());

        Document update = new Document();
        update.append("$set", setData);

        // TODO: Roll back player changes. If the match update fails?
        UpdateResult result = matches.updateOne(eq("_id", new ObjectId(match.getId())), update);
        if(!result.wasAcknowledged()){
            throw new MongoException("Insertion failed");
        }
    }

    public StoredMatch getMatch(String id){
        Document matchDoc = matches.find(eq("_id", new ObjectId(id))).first();
        if (matchDoc == null) {
            return  null;
        }
        return doc2Match(matchDoc);

    }

}
