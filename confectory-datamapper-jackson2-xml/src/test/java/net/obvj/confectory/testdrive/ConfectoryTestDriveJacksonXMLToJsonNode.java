package net.obvj.confectory.testdrive;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JacksonXMLToJsonNodeMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveJacksonXMLToJsonNode
{
    public static void main(String[] args)
    {
        String xml = "<root><foo attrib=\"attribValue\">bar</foo></root>";

        Configuration<JsonNode> config = Configuration.<JsonNode>builder()
                .namespace("test")
                .source(SourceFactory.stringSource(xml))
                .mapper(new JacksonXMLToJsonNodeMapper())
                .build();

        System.out.println(config.getBean().get().toPrettyString());
        System.out.println(config.getString("$.foo.attrib"));
    }
}
