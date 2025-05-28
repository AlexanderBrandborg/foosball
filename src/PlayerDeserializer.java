import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

public class PlayerDeserializer  implements
        JsonDeserializer<Player> {

    // TODO: Enforce no other fields?
    public Player deserialize(JsonElement jsonElement, Type
            type, JsonDeserializationContext jsonDeserializationContext) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        String initials = jsonObject.get("initials").getAsString();
        if (jsonObject.has("handicap")){
            return new Player(name, initials, jsonObject.get("handicap").getAsInt());
        }
        return new Player(name, initials);
    }
}

