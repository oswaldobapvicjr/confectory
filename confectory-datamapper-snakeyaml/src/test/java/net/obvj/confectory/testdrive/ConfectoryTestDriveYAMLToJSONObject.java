package net.obvj.confectory.testdrive;

import net.minidev.json.JSONObject;
import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.YAMLToJSONObjectMapper;

public class ConfectoryTestDriveYAMLToJSONObject
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .source("testfiles/person.yaml")
                .mapper(new YAMLToJSONObjectMapper())
                .build();

        System.out.println();
        System.out.println(config.getBean().toJSONString());
        System.out.println();
        System.out.println(config.getString("firstName"));
        System.out.println(config.getDouble("height"));
        System.out.println(config.getString("$.contactDetails[?(@.type=='landline')].number"));
        System.out.println(config.getString("$.homeAddress.line"));
        System.out.println(config.getString("$.homeAddress.city"));

    }
}
