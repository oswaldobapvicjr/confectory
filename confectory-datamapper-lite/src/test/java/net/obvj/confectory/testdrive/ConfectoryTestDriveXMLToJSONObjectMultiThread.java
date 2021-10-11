package net.obvj.confectory.testdrive;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.XMLToJSONObjectMapper;

public class ConfectoryTestDriveXMLToJSONObjectMultiThread
{
    public static void main(String[] args)
    {
        File out = new File("out.txt");

        List<String> list = Arrays.asList("**.agent[0].class",
                "**.agent[0].interval",
                "$.conf.agents.agent[?(@.class=='Agent2')].interval",
                "**.agent[-1:].class");

        Configuration<JSONObject> config = Configuration.<JSONObject>builder()
                .source("testfiles/agents.xml")
                .mapper(new XMLToJSONObjectMapper()).build();

        Runnable runnable = () ->
        {
            int numOfRepetitions = 100;
            for (int i = 0; i < numOfRepetitions; i++)
            {
                for (int j = 0; j < list.size(); j++)
                {
                    if (j == list.size())
                    {
                        j = 0;
                    }
                    String path = list.get(j);
                    String value = config.getString(path);
                    try
                    {
                        FileUtils.writeStringToFile(out, path + ">>" + value + "\n", StandardCharsets.US_ASCII, true);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        int numOfThreads = 100;
        for (int i = 0; i < numOfThreads; i++)
        {
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }
}
