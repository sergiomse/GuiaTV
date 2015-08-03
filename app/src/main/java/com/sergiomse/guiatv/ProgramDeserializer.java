package com.sergiomse.guiatv;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sergiomse.guiatv.model.Program;

import java.lang.reflect.Type;

/**
 * Created by sergiomse@gmail.com on 03/08/2015.
 */
public class ProgramDeserializer implements JsonDeserializer<Program> {
    @Override
    public Program deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return null;
    }
}
