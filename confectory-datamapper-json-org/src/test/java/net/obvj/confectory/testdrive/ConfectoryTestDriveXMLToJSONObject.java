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
                .source("testfiles/agents.xml")
                .mapper(new XMLToJSONObjectMapper()).build();

        System.out.println(config.getBean().toString(2));
        System.out.println(config.getBoolean("conf.enabled"));
        System.out.println(config.getString("**.agent[0].interval"));
        System.out.println(config.getString("$.conf.agents.agent[?(@.class=='Agent2')].interval"));
        System.out.println(config.get("$.conf.agents"));
        System.out.println(config.get("$.conf.agents.**.class"));
    }
}
