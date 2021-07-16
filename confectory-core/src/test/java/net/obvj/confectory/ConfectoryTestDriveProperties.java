package net.obvj.confectory;

import java.util.Properties;

import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.source.ClasspathFileSource;

public class ConfectoryTestDriveProperties
{
    public static void main(String[] args)
    {
        Configuration<Properties> config = new ConfigurationBuilder<Properties>()
                .namespace("test")
                .source(new ClasspathFileSource<>("testfiles/my-props.properties"))
                .mapper(new PropertiesMapper())
                .build();

        System.out.println(config.getRequiredBean());
        System.out.println(config.getBooleanProperty("web.enable"));
        System.out.println(config.getStringProperty("web.host"));
        System.out.println(config.getIntProperty("web.port"));
        System.out.println(config.getDoubleProperty("web.price"));
        System.out.println(config.getIntProperty("web.test"));

    }
}
