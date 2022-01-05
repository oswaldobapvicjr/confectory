package net.obvj.confectory.testdrive.model;

import java.util.List;

public class Customer
{
    private String firstName;
    private String lastName;
    private int age;
    private double height;
    private List<Contact> contactDetails;
    private Address homeAddress;

    /**
     * @return the firstName
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * @return the age
     */
    public int getAge()
    {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age)
    {
        this.age = age;
    }

    /**
     * @return the height
     */
    public double getHeight()
    {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height)
    {
        this.height = height;
    }

    /**
     * @return the contactDetails
     */
    public List<Contact> getContactDetails()
    {
        return contactDetails;
    }

    /**
     * @param contactDetails the contactDetails to set
     */
    public void setContactDetails(List<Contact> contactDetails)
    {
        this.contactDetails = contactDetails;
    }

    /**
     * @return the homeAddress
     */
    public Address getHomeAddress()
    {
        return homeAddress;
    }

    /**
     * @param homeAddress the homeAddress to set
     */
    public void setHomeAddress(Address homeAddress)
    {
        this.homeAddress = homeAddress;
    }

}
