package net.obvj.confectory.testdrive;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.GsonJsonObjectMapper;

public class ConfectoryTestDriveGsonMergeTwoConfigs
{
    public static void main(String[] args)
    {
        Configuration<JsonObject> config1 = Configuration.<JsonObject>builder()
                .source("testfiles/agents.json")
                .precedence(100)
                .mapper(new GsonJsonObjectMapper())
                .build();

        Configuration<JsonObject> config2 = Configuration.<JsonObject>builder()
                .source("testfiles/agents2.json")
                .precedence(99)
                .mapper(new GsonJsonObjectMapper())
                .build();

        Configuration<JsonObject> config = config1.merge(config2);

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(config.getBean()));
        System.out.println(config.getBoolean("enabled"));
        System.out.println(config.get("$.agents[?(@.class=='Agent1')]"));
        System.out.println(config.get("$.agents[?(@.class=='Agent2')]"));
        System.out.println(config.get("$.agents[?(@.class=='Agent3')]"));
    }
}
