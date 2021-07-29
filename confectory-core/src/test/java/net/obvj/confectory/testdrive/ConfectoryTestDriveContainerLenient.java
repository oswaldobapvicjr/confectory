package net.obvj.confectory.testdrive;

import java.util.Properties;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationContainer;
import net.obvj.confectory.DataFetchStrategy;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.settings.ConfectorySettings;
import net.obvj.confectory.source.StringSource;

public class ConfectoryTestDriveContainerLenient
{
    public static void main(String[] args)
    {
        ConfectorySettings.getInstance().setDefaultDataFetchStrategy(DataFetchStrategy.LENIENT);

        Configuration<Properties> config1 = Configuration.<Properties>builder()
                .namespace("test")
                .source(new StringSource<>("myFileName=config1\n myString=cust1"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config2 = Configuration.<Properties>builder()
                .namespace("test1")
                .precedence(100)
                .source(new StringSource<>("myFileName=config2\n myLong=20000000200000002\n myString=cust2"))
                .mapper(new PropertiesMapper()).build();

        Configuration<Properties> config3 = Configuration.<Properties>builder()
                .namespace("test2")
                .precedence(101)
                .source(new StringSource<>("myFileName=config3\n"))
                .mapper(new PropertiesMapper()).build();

        ConfigurationContainer container = new ConfigurationContainer(config1, config2, config3);

        System.out.println(container.getStringProperty("myFileName"));
        System.out.println(container.getStringProperty("myString"));
        System.out.println(container.getLongProperty("myLong"));
        System.out.println(container.getLongProperty("myLong2"));

    }
}
