package io.lightflame.util;

import com.google.gson.Gson;

/**
 * JsonUtil
 */
public class JsonUtil {

    static private Gson gson = new Gson();

    static public <T>T getObject(String jsonStr, Class<T> clazz){
        return gson.fromJson(jsonStr, clazz);
    }
}