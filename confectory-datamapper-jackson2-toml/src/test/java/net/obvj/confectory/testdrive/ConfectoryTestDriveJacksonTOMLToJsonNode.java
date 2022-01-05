package net.obvj.confectory.testdrive;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JacksonTOMLToJsonNodeMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveJacksonTOMLToJsonNode
{
    public static void main(String[] args)
    {
        Configuration<JsonNode> config = Configuration.<JsonNode>builder()
                .source(SourceFactory.classpathFileSource("application.toml"))
                .mapper(new JacksonTOMLToJsonNodeMapper())
                .build();

        System.out.println(config.getBean().toPrettyString());
        System.out.println(config.getString("title"));
        System.out.println(config.getString("$.database.server"));
        System.out.println(config.getInteger("$.database.ports[0]"));
        System.out.println(config.getString("$.servers.alpha.ip"));
    }
}
