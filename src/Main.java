import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;

import java.util.List;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        post("/players", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                response.type("application/json");

                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(Player.class, new
                        PlayerDeserializer());
                Gson gson = builder.create();

                Player player = gson.fromJson(request.body(),
                        Player.class);

                String id = playerCollection.CreatePlayer(player);
                return new Gson().toJson(id);
            }
        });


        get("/players/:id", (req,res)->{
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);

                res.type("application/json");
                String id = req.params(":id");
                StoredPlayer players = playerCollection.GetPlayer(id);

                res.status(200);
                res.type("application/json");
                return new Gson().toJson(players);
            }
        });

        get("/players", (req,res)->{
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);

                res.type("application/json");
                String id = req.params(":id");
                List<Player> players = playerCollection.GetPlayers();
                res.status(200);
                res.type("application/json");
                return new Gson().toJson(players);
            }
        });

        // TODO: Search for player by name
        // TODO: Search for player by initial

        delete("/players/:id", (req,res)-> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);

                res.type("application/json");
                String id = req.params(":id");
                playerCollection.DeletePlayer(id);
                res.status(200);
                res.type("application/json");
                return new Gson().toJson("");
            }});


            /*
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

             */

    }
}
