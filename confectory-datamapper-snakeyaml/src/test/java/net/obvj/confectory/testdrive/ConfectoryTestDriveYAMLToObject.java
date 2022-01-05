package net.obvj.confectory.testdrive;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.YAMLToObjectMapper;
import net.obvj.confectory.testdrive.model.Customer;

public class ConfectoryTestDriveYAMLToObject
{
    public static void main(String[] args)
    {
        Configuration<Customer> config = Configuration.<Customer>builder()
                .source("testfiles/person.yaml")
                .mapper(new YAMLToObjectMapper<>(Customer.class)).build();

        System.out.println(config.getBean());
        System.out.println(config.getBean().getFirstName());
        System.out.println(config.getBean().getHeight());
        System.out.println(config.getBean().getContactDetails().get(0).getNumber());
        System.out.println(config.getBean().getContactDetails().get(1).getNumber());
        System.out.println(config.getBean().getHomeAddress().getZip());
    }
}
