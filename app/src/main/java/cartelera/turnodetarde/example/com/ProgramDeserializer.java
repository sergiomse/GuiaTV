package cartelera.turnodetarde.example.com;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import cartelera.turnodetarde.example.com.model.Program;

/**
 * Created by turno de tarde on 24/07/2015.
 */
public class ProgramDeserializer implements JsonDeserializer<Program> {

    @Override
    public Program deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
        JsonElement channel = json.getAsJsonObject().get("channel");
        return null;
    }

}
