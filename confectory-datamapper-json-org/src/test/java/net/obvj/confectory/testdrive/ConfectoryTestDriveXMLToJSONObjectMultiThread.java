/*
 * Copyright 2021 obvj.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.obvj.confectory.testdrive;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JsonOrgXMLToJSONObjectMapper;

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
                .mapper(new JsonOrgXMLToJSONObjectMapper()).build();

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
