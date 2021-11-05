package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.GsonJsonToObjectMapper;
import net.obvj.confectory.testdrive.model.MyBean;

public class ConfectoryTestDriveGsonJsonToObject
{
    public static void main(String[] args)
    {
        Configuration<MyBean> config = Configuration.<MyBean>builder()
                .namespace("test")
                .source("testfiles/agents.json")
                .mapper(new GsonJsonToObjectMapper<>(MyBean.class))
                .build();

        MyBean myBean = config.getBean().get();
        System.out.println(myBean);
        System.out.println(myBean.isEnabled());
        System.out.println(myBean.getAgents().get(0).getClazz());
        System.out.println(myBean.getAgents().get(0).getInterval());
        System.out.println(myBean.getAgents().get(1).getClazz());
        System.out.println(myBean.getAgents().get(1).getInterval());
    }
}