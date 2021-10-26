package net.obvj.confectory.testdrive;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JacksonJsonNodeMapper;

public class ConfectoryTestDriveJacksonJsonToJsonNode
{
    public static void main(String[] args)
    {
        Configuration<JsonNode> config = Configuration.<JsonNode>builder()
                .namespace("test")
                .source("testfiles/agents.json")
                .mapper(new JacksonJsonNodeMapper())
                .build();

        System.out.println(config.getBean().get().toPrettyString());
        System.out.println(config.getBoolean("enabled"));
        System.out.println(config.getString("$.agents[0].interval"));
        System.out.println(config.getString("$.agents[?(@.class=='Agent2')].interval"));
    }
}
