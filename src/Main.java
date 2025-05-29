import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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

        // Middleware
        before((req, res) -> {
            // Check body is valid json
            if (req.requestMethod().equals("POST") || req.requestMethod().equals("PATCH")) {
                if(req.body() == null || req.body().trim().isEmpty()){
                    halt(400, "POST/PATCH require a non-empty body");
                }
                try {
                    JsonParser.parseString(req.body());
                } catch (JsonSyntaxException e) {
                    halt(400, "POST/PATCH require a valid json body");
                }
            }
        });

        post("/players", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                PlayerInput playerInput = new Gson().fromJson(request.body(), PlayerInput.class);
                Player player = new Player(playerInput);
                String id = playerCollection.CreatePlayer(player);
                return new Gson().toJson(id);
            }
        });

        get("/players/:id", (req,res)->{
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                String id = req.params(":id");
                StoredPlayer player = playerCollection.GetPlayer(id);
                if(player == null){
                    throw  new FoosballException("Player not found", 404);
                }
                return new Gson().toJson(player);
            }
        });

        get("/players", (req,res)->{
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                String name = req.queryParams("name");
                String initials = req.queryParams("initials");
                List<StoredPlayer> players = playerCollection.GetSortedPlayers(name, initials);
                return new Gson().toJson(players);
            }
        });

        // TODO: Create patch for updating a player

        delete("/players/:id", (req,res)-> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                    String id = req.params(":id");
                    playerCollection.DeletePlayer(id);
                    return new Gson().toJson(new JsonObject());
            }});

        post("/matches", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                MatchCollection matchCollection = new MatchCollection(mongoClient, playerCollection);
                MatchInput matchInput = new Gson().fromJson(request.body(), MatchInput.class);
                String id = matchCollection.CreateMatch(matchInput.homePlayer1Id, matchInput.homePlayer2Id,
                        matchInput.awayPlayer1Id, matchInput.awayPlayer2Id);
                return new Gson().toJson(id);
            }
        });

        // NOTE: This path allows us to set scores, even when some players in the match have been deleted
        patch("/matches/:id", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                MatchCollection matchCollection = new MatchCollection(mongoClient, playerCollection);
                ScoreInput scoreInput = new Gson().fromJson(request.body(), ScoreInput.class);
                String id = request.params(":id");
                StoredMatch match = matchCollection.getMatch(id);
                if (match == null) {
                    throw new FoosballException("Match not found", 404);
                }
                match.recordOutcome(scoreInput.homeScore, scoreInput.awayScore);
                matchCollection.UpdateMatch(match);

                return new Gson().toJson(id);
            }
        });

        // TODO: If you were to differentiate more on subtypes, you could also add the decision about status code to this layer
        exception(FoosballException.class, (e, request, response) -> {
            response.status(e.statusCode);
            response.body(e.getMessage());
        });

    }
}
