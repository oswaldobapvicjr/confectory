package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.ConfigurationBuilder;
import net.obvj.confectory.mapper.StringMapper;

public class ConfectoryTestDriveFileString
{
    public static void main(String[] args)
    {
        Configuration<String> config = new ConfigurationBuilder<String>()
                .namespace("test")
                .source("file://${TEMP}/file1.txt")
                .mapper(new StringMapper())
                .optional()
                .build();

        System.out.println(config.getSource());
        System.out.println(config.getBean());
    }
}
