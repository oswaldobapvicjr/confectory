package net.obvj.confectory.testdrive;

import net.minidev.json.JSONObject;
import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.INIToJSONObjectMapper;

public class ConfectoryTestDriveINIToJSONObject
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .source("testfiles/my-app.ini")
                .mapper(new INIToJSONObjectMapper())
                .build();

        System.out.println(config.getBean());
        System.out.println(config.getString("$.title"));
        System.out.println(config.getString("$.owner.name"));
        System.out.println(config.getDouble("$.owner.height"));
        System.out.println(config.getInteger("$.database.port"));
        System.out.println(config.getBoolean("$.database.read_only"));

    }
}
