import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main {
    public static void main(String[] args) {

        // Gotta read up on this Pojo thing, it's weird
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
            MongoDatabase db = mongoClient.getDatabase("foosball");
            MongoCollection<Player> players = db.getCollection("players", Player.class);

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
        }
        }
    }
