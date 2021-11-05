package net.obvj.confectory.testdrive;


import org.json.JSONObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JSONObjectMapper;

public class ConfectoryTestDriveURLToJSONObject
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .namespace("test")
                .source("http://ip.jsontest.com")
                .mapper(new JSONObjectMapper())
                .build();

        System.out.println(config.getBean().get().toString(2));
        System.out.println(config.getString("$.ip"));
    }
}
