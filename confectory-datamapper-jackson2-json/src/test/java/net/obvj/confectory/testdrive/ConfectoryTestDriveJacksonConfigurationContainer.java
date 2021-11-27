package net.obvj.confectory.testdrive;

import com.fasterxml.jackson.databind.JsonNode;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationContainer;
import net.obvj.confectory.DataFetchStrategy;
import net.obvj.confectory.mapper.JacksonJsonNodeMapper;
import net.obvj.confectory.mapper.JacksonJsonToObjectMapper;
import net.obvj.confectory.testdrive.model.MyBean;

public class ConfectoryTestDriveJacksonConfigurationContainer
{
    public static void main(String[] args)
    {
        Configuration<MyBean> config = Configuration.<MyBean>builder()
                .namespace("test")
                .source("testfiles/agents.json")
                .mapper(new JacksonJsonToObjectMapper<>(MyBean.class))
                .build();

        MyBean myBean = config.getBean();
        System.out.println(myBean);
        System.out.println(myBean.isEnabled());
        System.out.println(myBean.getAgents().get(0).getInterval());
        System.out.println(myBean.getAgents().get(1).getInterval());

        Configuration<JsonNode> config2 = Configuration.<JsonNode>builder()
                .namespace("test")
                .source("testfiles/agents.json")
                .mapper(new JacksonJsonNodeMapper())
                .build();

        System.out.println(config2.getBean().toPrettyString());

        ConfigurationContainer container = new ConfigurationContainer(DataFetchStrategy.LENIENT, config, config2);
        System.out.println(container.getBoolean("$.enabled"));

    }
}
