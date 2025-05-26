import com.mongodb.client.*;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }

        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");
        MongoCollection<Document> collection = database.getCollection("yourCollectionName");

        Document doc = new Document("name", "John Doe")
                .append("age", 30)
                .append("address", new Document("street", "123 Main St")
                        .append("city", "Anytown"));
        collection.insertOne(doc);

        FindIterable<Document> iterable = collection.find(new Document("age", 30));
        for (Document document : iterable) {
            System.out.println(document.toJson());
        }

        collection.updateMany(eq("age", 30), new Document("$set", new Document("age", 31)));

        collection.deleteOne(eq("name", "John Doe"));



    }
}