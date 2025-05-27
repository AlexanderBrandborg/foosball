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
            Player player = new Player(playerCollection.GetNewId(), "Bob", "BO", 1);

            String playerId = playerCollection.SetPlayer(player);

            System.out.println(playerId);
            Player player1 = playerCollection.GetPlayer(playerId);
            System.out.println(player);
            //playerCollection.DeletePlayer(playerId);

            /*
            MongoDatabase db = mongoClient.getDatabase("foosball");
            MongoCollection<Document> players = db.getCollection("players");

            Player newPlayer = new Player("Alexander", "AB", 10);
            InsertOneResult result = players.insertOne(newPlayer);


            // Ah. It needs to be an object id.
            ObjectId id = Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue();

            Player foundPlayer = players.find(eq("_id", id)).first();

            assert foundPlayer != null;
            foundPlayer.increaseHandicap();

            players.findOneAndReplace(eq("_id", foundPlayer.getId()), foundPlayer);

            // Sorting players by handicap
            Iterable<Player> playersList = players.find();
            List<Player> list = new ArrayList<>();
            playersList.iterator().forEachRemaining(list::add);

            list.sort(Comparator.comparing(Player::getHandicap).reversed());

            int a = 5;

            // TODO: Maybe I should just remove the dependency on Pojo

            Document doc = new Document("name", "John Doe")
                    .append("age", 30)
                    .append("address", new Document("street", "123 Main St")
                            .append("city", "Anytown"));
            players.insertOne(doc);

            // Tasks
            // Lock down which updates can be made
            // Create the REST API
            // Create the match DB

             */
        }
        }
    }
