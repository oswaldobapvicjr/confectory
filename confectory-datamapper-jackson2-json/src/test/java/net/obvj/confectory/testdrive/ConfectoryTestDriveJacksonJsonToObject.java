package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.JacksonJsonToObjectMapper;
import net.obvj.confectory.testdrive.model.MyBean;

public class ConfectoryTestDriveJacksonJsonToObject
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
    }
}
