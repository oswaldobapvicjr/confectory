package net.obvj.confectory.testdrive;

import java.util.Properties;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationBuilder;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveClasspathPropertiesOptional
{
    public static void main(String[] args)
    {
        Configuration<Properties> config = new ConfigurationBuilder<Properties>()
                .namespace("test")
                .source(SourceFactory.classpathFileSource("testfiles/nofile.properties"))
                .mapper(new PropertiesMapper())
                .optional()
                .build();

        System.out.println(config.getBean());
        System.out.println(config.getBooleanProperty("web.enable"));
        System.out.println(config.getStringProperty("web.host"));
        System.out.println(config.getIntProperty("web.port"));
        System.out.println(config.getDoubleProperty("web.price"));
        System.out.println(config.getIntProperty("web.test"));

    }
}
