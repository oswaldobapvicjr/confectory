package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.PropertiesToObjectMapper;
import net.obvj.confectory.testdrive.model.MyProperties;

public class ConfectoryTestDriveClasspathPropertiesToObject
{
    public static void main(String[] args)
    {
        Configuration<MyProperties> config = Configuration.<MyProperties>builder()
                .namespace("test")
                .source("testfiles/my-props.properties")
                .mapper(new PropertiesToObjectMapper<>(MyProperties.class))
                .lazy()
                .build();

        MyProperties myProperties = config.getBean();
        System.out.println(myProperties.isEnabled());
        System.out.println(myProperties.getHost());
        System.out.println(myProperties.getPort());
        System.out.println(myProperties.getPrice());
        System.out.println(myProperties.getMyString());
        System.out.println(myProperties.getMyOtherString());

    }
}
