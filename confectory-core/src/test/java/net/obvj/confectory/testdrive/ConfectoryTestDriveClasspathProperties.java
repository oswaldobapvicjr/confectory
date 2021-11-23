package net.obvj.confectory.testdrive;

import java.util.Properties;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.PropertiesMapper;

public class ConfectoryTestDriveClasspathProperties
{
    public static void main(String[] args)
    {
        Configuration<Properties> config = Configuration.<Properties>builder()
                .namespace("test")
                .source("testfiles/my-props.properties")
                .mapper(new PropertiesMapper())
                .lazy()
                .build();

        System.out.println(config.getBean());
        System.out.println(config.getBoolean("web.enable"));
        System.out.println(config.getString("web.host"));
        System.out.println(config.getInteger("web.port"));
        System.out.println(config.getDouble("web.price"));
        System.out.println(config.getInteger("web.test"));

    }
}
