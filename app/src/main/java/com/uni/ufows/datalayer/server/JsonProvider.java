package com.uni.ufows.datalayer.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Created by Sammy Shwairy on 2/28/2016.
 */
public class JsonProvider {

    public static <T> T getObject(String data, Type type) throws Exception {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            Gson gson = builder.create();



            T object = gson.fromJson(data, type);
            return object;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
