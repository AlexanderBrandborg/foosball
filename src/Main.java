import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;

public class Main {
    public static void main(String[] args) {

        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            PlayerCollection playerCollection = new PlayerCollection(mongoClient);
            MatchCollection matchCollection = new MatchCollection(mongoClient);

            Player player1 = new Player("Alex", "A", 1);
            Player player2 = new Player("Anders", "AN", 1);
            Player player3 = new Player("Bob", "BO", 1);
            Player player4 = new Player("Bobbers", "BOB", 1);

            String player1Id = playerCollection.CreatePlayer(player1);
            String player2Id = playerCollection.CreatePlayer(player2);
            String player3Id = playerCollection.CreatePlayer(player3);
            String player4Id = playerCollection.CreatePlayer(player4);

            String matchId = matchCollection.CreateMatch(player1Id, player2Id, player3Id, player4Id, null, null);
            StoredMatch match = matchCollection.getMatch(matchId);
            match.recordOutcome(2, 1);
            matchCollection.UpdateMatch(match);
        }
    }
}
