import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;

import java.util.ArrayList;
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
                    halt(400, "{\"result\":\"ERR\",\"errMsg\":\"POST/PATCH require a non-empty body\"");
                }
                try {
                    JsonParser.parseString(req.body());
                } catch (JsonSyntaxException e) {
                    halt(400, "{\"result\":\"ERR\",\"errMsg\":\"POST/PATCH require a valid json body\"");
                }
            }
        });

        post("/players", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                try {
                    PlayerInput playerInput = new Gson().fromJson(request.body(), PlayerInput.class);
                    Player player = new Player(playerInput);
                    String id = playerCollection.CreatePlayer(player);
                    return new Gson().toJson(id);
                } catch (IllegalArgumentException e) {
                    response.status(400);
                    return new Gson().toJson(e.getMessage());
                }
            }
        });

        get("/players/:id", (req,res)->{
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                String id = req.params(":id");
                try {
                    StoredPlayer player = playerCollection.GetPlayer(id);
                    if(player == null){
                        res.status(404);
                        // TODO: Create a standard error msg?
                        return new Gson().toJson("Player not found");
                    }
                    return new Gson().toJson(player);
                } catch (IllegalArgumentException e) {
                    res.status(400);
                    return new Gson().toJson(e.getMessage());
                }
            }
        });

        get("/players", (req,res)->{
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                List<StoredPlayer> players = playerCollection.GetSortedPlayers();
                return new Gson().toJson(players);
            }
        });

        get("/players/search/", (req,res)->{
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                String name = req.queryParams("name");
                String initials = req.queryParams("initials");

                if(name == null && initials == null){
                    res.status(200);
                    return new Gson().toJson(new ArrayList<StoredPlayer>());
                }
                else if(name != null && initials == null){
                    List<StoredPlayer> players = playerCollection.GetPlayerByName(name);
                    return new Gson().toJson(players);
                }
                else if (name == null && initials != null){
                    List<StoredPlayer> players = playerCollection.GetPlayerByInitials(initials);
                    return new Gson().toJson(players);
                }
                else {
                    res.status(400);
                    return new Gson().toJson(new ArrayList<StoredPlayer>());
                }
            }
        });

        delete("/players/:id", (req,res)-> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                try {
                    String id = req.params(":id");
                    playerCollection.DeletePlayer(id);
                    return new Gson().toJson(new JsonObject());
                } catch (IllegalArgumentException e) {
                    // TODO: Differentiate on notFound and illegalId here
                    res.status(400);
                    return new Gson().toJson(e.getMessage());
                }
            }});

        post("/matches", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                MatchCollection matchCollection = new MatchCollection(mongoClient);
                try {
                    MatchInput matchInput = new Gson().fromJson(request.body(), MatchInput.class);
                    String id = matchCollection.CreateMatch(matchInput.homePlayer1Id, matchInput.homePlayer2Id,
                            matchInput.awayPlayer1Id, matchInput.awayPlayer2Id);
                    return new Gson().toJson(id);
                } catch (NullPointerException | IllegalArgumentException e)  {
                    response.status(400);
                    return new Gson().toJson(e.getMessage());
                }
            }
        });

        patch("/matches/:id", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                MatchCollection matchCollection = new MatchCollection(mongoClient);

                try {
                    ScoreInput scoreInput = new Gson().fromJson(request.body(), ScoreInput.class);
                    String id = request.params(":id");
                    StoredMatch match = matchCollection.getMatch(id);
                    if (match == null) {
                        response.status(404);
                        return new Gson().toJson("Match not found");
                    }
                    match.recordOutcome(scoreInput.homeScore, scoreInput.awayScore);
                    matchCollection.UpdateMatch(match);

                    return new Gson().toJson(id);
                } catch (IllegalArgumentException e) {
                    response.status(400);
                    return new Gson().toJson(e.getMessage());
                }
            }
        });
    }
}
