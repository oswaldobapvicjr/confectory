package net.obvj.confectory.testdrive;

import org.json.JSONObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.XMLToJSONObjectMapper;

public class ConfectoryTestDriveXMLToJSONObject
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .namespace("test")
                .source("testfiles/agents.xml").mapper(new XMLToJSONObjectMapper())
                .build();

        System.out.println(config.getBean().get().toString(2));
    }
}
