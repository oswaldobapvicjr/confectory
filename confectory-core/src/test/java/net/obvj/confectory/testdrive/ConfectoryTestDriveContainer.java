package net.obvj.confectory.testdrive;

import java.util.Properties;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationContainer;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveContainer
{
    public static void main(String[] args)
    {
        Configuration<Properties> config1 = Configuration.<Properties>builder()
                .namespace("test")
                .source(new StringSource<>("myFileName=config1\n myString=cust1"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config2 = Configuration.<Properties>builder()
                .namespace("test")
                .precedence(100)
                .source(new StringSource<>("myFileName=config2\n myLong=20000000200000002\n myString=cust2"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config3 = Configuration.<Properties>builder()
                .namespace("test")
                .precedence(101)
                .source(new StringSource<>("myFileName=config3\n"))
                .mapper(new PropertiesMapper()).build();

        ConfigurationContainer container = new ConfigurationContainer(config1, config2, config3);

        System.out.println(container.getString("test", "myFileName"));
        System.out.println(container.getString("test", "myString"));
        System.out.println(container.getLong("test", "myLong"));
        System.out.println(container.getLong("test", "myLong2"));

        System.out.println("*****");

        Configuration<Properties> config4 = Configuration.<Properties>builder()
                .namespace("test")
                .precedence(102)
                .source(new StringSource<>("myString=cust4\n"))
                .mapper(new PropertiesMapper()).build();

        container.add(config4);

        System.out.println(container.getString("test", "myFileName"));
        System.out.println(container.getString("test", "myString"));
        System.out.println(container.getLong("test", "myLong"));
        System.out.println(container.getLong("test", "myLong2"));

        System.out.println("*****");

        container.clear();

        System.out.println(container.getString("test", "myFileName"));
        System.out.println(container.getString("test", "myString"));
        System.out.println(container.getLong("test", "myLong"));
        System.out.println(container.getLong("test", "myLong2"));
    }
}
