package net.obvj.confectory.testdrive;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.GsonJsonObjectMapper;

public class ConfectoryTestDriveURLToGsonJsonObject
{
    public static void main(String[] args)
    {
        Configuration<JsonObject> config = Configuration.<JsonObject>builder()
                .namespace("test")
                .source("http://time.jsontest.com")
                .mapper(new GsonJsonObjectMapper())
                .build();

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(config.getBean()));
        System.out.println(config.getString("$.date"));
        System.out.println(config.getLong("$.milliseconds_since_epoch"));
        System.out.println(config.getString("$.time"));
    }
}
