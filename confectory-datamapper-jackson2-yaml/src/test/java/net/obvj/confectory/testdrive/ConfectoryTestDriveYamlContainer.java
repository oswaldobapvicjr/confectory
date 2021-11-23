package net.obvj.confectory.testdrive;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationContainer;
import net.obvj.confectory.mapper.JacksonYAMLToJsonNodeMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveYamlContainer
{
    public static void main(String[] args)
    {
        String yaml1 = "enabled: false\nstring: value1";
        String yaml2 = "enabled: true\nstring: value2";

        ConfigurationContainer config = new ConfigurationContainer(
                Configuration.<JsonNode>builder().source(SourceFactory.stringSource(yaml1))
                        .mapper(new JacksonYAMLToJsonNodeMapper()).lazy().precedence(10).build(),
                Configuration.<JsonNode>builder().source(SourceFactory.stringSource(yaml2))
                        .mapper(new JacksonYAMLToJsonNodeMapper()).lazy().precedence(5).build());

        System.out.println(config.getBoolean("$.enabled"));
        System.out.println(config.getBoolean("$.enablede"));

    }
}
