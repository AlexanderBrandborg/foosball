import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MatchCollection {
    private MongoCollection<Document> players; // This collection needs knowledge about players
    private MongoCollection<Document> matches;

    // How to handle collection updates to players? Should they be updated from here?
    public MatchCollection(MongoClient mongo){
        this.players = mongo.getDatabase("foosball").getCollection("players");
        this.matches = mongo.getDatabase("foosball").getCollection("matches");
    }

    public void SetMatch(Match match){
        // Should just check that the ids exists for the match and players. Logic about handicaps should already be accounted for.
    }

}
