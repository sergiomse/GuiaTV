package com.sergiomse.guiatv;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sergiomse.guiatv.model.Link;
import com.sergiomse.guiatv.model.Program;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergio on 02/08/2015.
 */
public class ProgramSerializer extends TypeAdapter<Program> {

    final Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, Program value) throws IOException {

    }

    @Override
    public Program read(JsonReader in) throws IOException {
        Program program = new Program();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    program.setId(in.nextInt());
                    break;
                case "start":
                    program.setStart(DateTime.parse(in.nextString()));
                    break;
                case "finish":
                    program.setFinish(DateTime.parse(in.nextString()));
                    break;
                case "name":
                    program.setName(in.nextString());
                    break;
                case "details":
                    program.setDetails(in.nextString());
                    break;
                case "links":
                    program.setLinks((Link[]) gson.fromJson(in, Link[].class));
                    //                    program.setDetails(in.pe);
                    break;
            }
        }
        in.endObject();

        return program;
    }

//    @Override
//    public JsonElement serialize(Program src, Type typeOfSrc, JsonSerializationContext context) {
//        JsonObject dest = new JsonObject();
//        dest.add("id", new JsonPrimitive(src.getId()));
//        dest.add("start", new JsonPrimitive(src.getStartDate().toString()));
//        dest.add("finish", new JsonPrimitive(src.getFinishDate().toString()));
//        dest.add("name", new JsonPrimitive(src.getName()));
//        dest.add("details", new JsonPrimitive(src.getDetails()));
//        dest.add("links", context.serialize(src.getLinks()));
//        return dest;
//    }
}

