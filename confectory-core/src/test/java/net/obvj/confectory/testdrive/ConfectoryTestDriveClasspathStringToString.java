package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationBuilder;
import net.obvj.confectory.mapper.StringMapper;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveClasspathStringToString
{
    public static void main(String[] args)
    {
        Configuration<String> config = new ConfigurationBuilder<String>()
                .namespace("test")
                .source(new StringSource<>("teststring"))
                .mapper(new StringMapper())
                .build();

        System.out.println(config.getBean().get());
    }
}
