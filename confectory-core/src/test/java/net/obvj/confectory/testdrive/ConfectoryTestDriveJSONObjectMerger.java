package net.obvj.confectory.testdrive;

import net.minidev.json.JSONObject;
import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JSONObjectMapper;
import net.obvj.confectory.merger.JsonMergeOption;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveJSONObjectMerger
{
    public static void main(String[] args)
    {
        Configuration<JSONObject> config1 = Configuration.<JSONObject>builder()
                .namespace("test").precedence(10)
                .source(new StringSource<>("{\n"
                        + "  \"agents\": [\n"
                        + "    {\n"
                        + "      \"class\": \"Agent1\",\n"
                        + "      \"interval\": \"*/2 * * * *\"\n"
                        + "    },\n"
                        + "    {\n"
                        + "      \"class\": \"Agent2\",\n"
                        + "      \"interval\": \"90s\"\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}n"))
                .mapper(new JSONObjectMapper()).build();

        Configuration<JSONObject> config2 = Configuration.<JSONObject>builder()
                .namespace("test").precedence(20)
                .source(new StringSource<>("{\n"
                        + "  \"enabled\": true,\n"
                        + "  \"agents\": [\n"
                        + "    {\n"
                        + "      \"class\": \"Agent2\",\n"
                        + "      \"interval\": \"10s\"\n"
                        + "    }\n"
                        + "  ]\n"
                        + "}"))
                .mapper(new JSONObjectMapper()).build();


        Configuration<JSONObject> config = config1.merge(config2,
                JsonMergeOption.distinctKey("$.agents", "class"));

        System.out.println(config.getBean());
    }
}
