package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.INIToObjectMapper;
import net.obvj.confectory.testdrive.model.MyApp;

public class ConfectoryTestDriveINIToObject
{
    public static void main(String[] args)
    {
        Configuration<MyApp> config = Configuration.<MyApp>builder()
                .source("testfiles/my-app.ini")
                .mapper(new INIToObjectMapper<MyApp>(MyApp.class))
                .build();

        MyApp app = config.getBean();
        System.out.println(app.getTitle());
        System.out.println(app.getOwner().getName());
        System.out.println(app.getOwner().getOrganization());
        System.out.println(app.getOwner().getHeight());
        System.out.println(app.getDatabase().getServer());
        System.out.println(app.getDatabase().getPort());
        System.out.println(app.getDatabase().isReadOnly());
        System.out.println(app.getDatabase().getMode());
        System.out.println("===END===");

    }
}
