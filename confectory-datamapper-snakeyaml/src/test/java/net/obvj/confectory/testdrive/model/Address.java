package net.obvj.confectory.testdrive.model;

public class Address
{
    private String line;
    private String city;
    private String state;
    private int zip;

    /**
     * @return the line
     */
    public String getLine()
    {
        return line;
    }

    /**
     * @param line the line to set
     */
    public void setLine(String line)
    {
        this.line = line;
    }

    /**
     * @return the city
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState()
    {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * @return the zip
     */
    public int getZip()
    {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(int zip)
    {
        this.zip = zip;
    }
}
