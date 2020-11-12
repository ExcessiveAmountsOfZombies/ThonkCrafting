package com.epherical.crafting.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {

    public static JsonPrimitive getValue(JsonObject object, String key) {
        if (object.has(key)) return object.getAsJsonPrimitive(key);
        else throw new JsonSyntaxException("Could not find value for key: " + key);
    }

    public static JsonArray getArrayValue(JsonObject object, String key) {
        if (object.has(key)) return object.getAsJsonArray(key);
        else throw new JsonSyntaxException("Could not find value for key: " + key);
    }
}
