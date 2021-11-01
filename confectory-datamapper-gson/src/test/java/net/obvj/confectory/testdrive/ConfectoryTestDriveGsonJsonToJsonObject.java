package net.obvj.confectory.testdrive;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.GsonJsonObjectMapper;

public class ConfectoryTestDriveGsonJsonToJsonObject
{
    public static void main(String[] args)
    {
        Configuration<JsonObject> config = Configuration.<JsonObject>builder()
                .namespace("test")
                .source("testfiles/agents.json")
                .mapper(new GsonJsonObjectMapper())
                .build();

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(config.getBean().get()));
        System.out.println(config.getBoolean("enabled"));
        System.out.println(config.getString("$.agents[0].class"));
        System.out.println(config.getString("$.agents[0].interval"));
        System.out.println(config.getString("$.agents[1:].class"));
        System.out.println(config.getString("$.agents[?(@.class=='Agent2')].interval"));
    }
}
