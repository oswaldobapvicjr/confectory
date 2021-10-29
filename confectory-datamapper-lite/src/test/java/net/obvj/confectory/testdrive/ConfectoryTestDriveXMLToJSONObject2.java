package net.obvj.confectory.testdrive;

import org.json.JSONObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.XMLToJSONObjectMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveXMLToJSONObject2
{
    public static void main(String[] args)
    {
        String xml = "<root><foo>bar</foo></root>";

        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .namespace("test")
                .source(SourceFactory.stringSource(xml))
                .mapper(new XMLToJSONObjectMapper())
                .build();

        System.out.println(config.getBean().get().toString(2));
        System.out.println(config.getString("$.root.foo"));
    }
}