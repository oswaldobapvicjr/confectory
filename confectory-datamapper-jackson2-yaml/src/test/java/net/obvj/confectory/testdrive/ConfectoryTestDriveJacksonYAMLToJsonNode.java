package net.obvj.confectory.testdrive;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JacksonYAMLToJsonNodeMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveJacksonYAMLToJsonNode
{
    public static void main(String[] args)
    {
        String yaml = "root:\n"
                    + "  foo: bar\n";

        Configuration<JsonNode> config = Configuration.<JsonNode>builder()
                .namespace("test")
                .source(SourceFactory.stringSource(yaml))
                .mapper(new JacksonYAMLToJsonNodeMapper())
                .build();

        System.out.println(config.getBean().toPrettyString());
        System.out.println(config.getString("$.root.foo"));
    }
}
