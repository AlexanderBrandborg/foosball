import com.google.gson.Gson;
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

        post("/players", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);
                response.type("application/json");

                PlayerInput playerInput = new Gson().fromJson(request.body(), PlayerInput.class);
                Player player = new Player(playerInput);

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

                res.type("application/json");
                return new Gson().toJson(players);
            }
        });

        get("/players", (req,res)->{
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                PlayerCollection playerCollection = new PlayerCollection(mongoClient);

                res.type("application/json");
                List<StoredPlayer> players = playerCollection.GetPlayers();
                res.status(200);
                res.type("application/json");
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
                    res.status(200);
                    res.type("application/json");
                    return new Gson().toJson(players);
                }
                else if (name == null && initials != null){
                    List<StoredPlayer> players = playerCollection.GetPlayerByInitials(initials);
                    res.status(200);
                    res.type("application/json");
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

                res.type("application/json");
                String id = req.params(":id");
                playerCollection.DeletePlayer(id);
                res.status(200);
                res.type("application/json");
                return new Gson().toJson("");
            }});

        post("/matches", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                MatchCollection matchCollection = new MatchCollection(mongoClient);
                response.type("application/json");

                MatchInput matchInput = new Gson().fromJson(request.body(), MatchInput.class);
                String id = matchCollection.CreateMatch(matchInput.homePlayer1Id, matchInput.homePlayer2Id, matchInput.awayPlayer1Id, matchInput.awayPlayer2Id, null, null);

                return new Gson().toJson(id);
            }
        });

        patch("/matches/:id", (request, response) -> {
            try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
                MatchCollection matchCollection = new MatchCollection(mongoClient);
                ScoreInput scoreInput = new Gson().fromJson(request.body(), ScoreInput.class);

                String id = request.params(":id");
                StoredMatch match = matchCollection.getMatch(id);

                response.type("application/json");
                match.recordOutcome(scoreInput.homeScore, scoreInput.awayScore);
                matchCollection.UpdateMatch(match);

                return new Gson().toJson(id);
            }
        });
    }
}
