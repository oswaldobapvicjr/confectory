package net.obvj.confectory;

import net.obvj.confectory.mapper.StringMapper;
import net.obvj.confectory.source.ClasspathFileSource;

public class ConfectoryTestDriveString
{
    public static void main(String[] args)
    {
        Configuration<String> config = new ConfigurationBuilder<String>()
                .namespace("test")
                .source(new ClasspathFileSource<>("testfiles/my-props.properties"))
                .mapper(new StringMapper())
                .build();

        System.out.println(config.getRequiredBean());
        System.out.println(config.getBooleanProperty("test"));
    }
}
