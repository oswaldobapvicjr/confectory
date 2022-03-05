package net.obvj.confectory.testdrive;

import java.util.Properties;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.merger.PropertiesConfigurationMerger;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveClasspathPropertiesMerger
{
    public static void main(String[] args)
    {
        Configuration<Properties> config1 = Configuration.<Properties>builder()
                .namespace("test").precedence(10)
                .source(new StringSource<>("booleanValue=false\n"
                                         + "stringValue=string1\n"
                                         + "intValue=9\n"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config2 = Configuration.<Properties>builder()
                .namespace("test").precedence(20)
                .source(new StringSource<>("booleanValue=true\n"
                                         + "stringValue=string2\n"
                                         + "doubleValue=9.88\n"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config = new PropertiesConfigurationMerger().merge(config1, config2);

        System.out.println(config1);
        System.out.println(config2);
        System.out.println(config);
        System.out.println(config.getBoolean("booleanValue"));
        System.out.println(config.getString("stringValue"));
        System.out.println(config.getInteger("intValue"));
        System.out.println(config.getDouble("doubleValue"));
    }
}
