package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationBuilder;
import net.obvj.confectory.mapper.StringMapper;
import net.obvj.confectory.source.ClasspathFileSource;

public class ConfectoryTestDriveClasspathString
{
    public static void main(String[] args)
    {
        Configuration<String> config = new ConfigurationBuilder<String>()
                .namespace("test")
                .source(new ClasspathFileSource<>("testfiles/my-props.properties"))
                .mapper(new StringMapper())
                .build();

        System.out.println(config.getBean());
        System.out.println(config.getBooleanProperty("test"));
    }
}
