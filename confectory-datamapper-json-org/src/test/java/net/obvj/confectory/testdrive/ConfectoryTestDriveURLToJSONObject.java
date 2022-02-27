package net.obvj.confectory.testdrive;


import org.json.JSONObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JsonOrgJSONObjectMapper;

public class ConfectoryTestDriveURLToJSONObject
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .namespace("test")
                .source("http://ip.jsontest.com")
                .mapper(new JsonOrgJSONObjectMapper())
                .build();

        System.out.println(config.getBean().toString(2));
        System.out.println(config.getString("$.ip"));
    }
}
