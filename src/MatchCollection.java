import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MatchCollection {
    private MongoCollection<Document> players; // This collection needs knowledge about players
    private MongoCollection<Document> matches;

    // How to handle collection updates to players? Should they be updated from here?
    public MatchCollection(MongoClient mongo){
        this.players = mongo.getDatabase("foosball").getCollection("players");
        this.matches = mongo.getDatabase("foosball").getCollection("matches");
    }

    private Document match2Doc(Match match){
        return new Document("_id", new ObjectId(match.getId()))
                .append ("homePlayer1id", match.getHomeTeam().player1.getId())
                .append ("homePlayer2id", match.getHomeTeam().player2.getId())
                .append ("awayPlayer1id", match.getAwayTeam().player1.getId())
                .append ("awayPlayer2id", match.getAwayTeam().player2.getId())
                .append("homeScore", match.getHomeScore())
                .append("awayScore", match.getAwayScore());
    }

    /*
    // TODO: Aaaaaah. If I want to get a match, and be able to set handicaps on players. I need to query the players too.
    // TODO: Wait. Since players can get deleted, that means I need to handle cases where the players don't exist. Maybe 'deleted' players just need a tombstone?
    private Match doc2Match(Document doc){
        return new Match(playerDoc.getObjectId("_id").toString(), playerDoc.getString("name"), playerDoc.getString("initials"), playerDoc.getInteger("handicap"));

        return new Document("_id", new ObjectId(match.getId()))
                .append ("homePlayer1id", match.getHomeTeam().player1.getId())
                .append ("homePlayer2id", match.getHomeTeam().player2.getId())
                .append ("awayPlayer1id", match.getAwayTeam().player1.getId())
                .append ("awayPlayer2id", match.getAwayTeam().player2.getId())
                .append("homeScore", match.getHomeScore())
                .append("awayScore", match.getAwayScore());
    }
     */

    public void SetMatch(Match match){
        // Should just check that the ids exists for the match and players. Logic about handicaps should already be accounted for.
    }

}
