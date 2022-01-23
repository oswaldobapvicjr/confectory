package net.obvj.confectory.testdrive.model;

import net.obvj.confectory.util.Property;

public class MyProperties
{
    @Property("web.enable")
    private boolean enabled;

    @Property("web.host")
    private String host;

    @Property("web.port")
    private int port;

    @Property("web.price")
    private double price;

    private String myString;
    private String myOtherString;

    /**
     * @return the enabled
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * @return the host
     */
    public String getHost()
    {
        return host;
    }

    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @return the price
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * @return the myString
     */
    public String getMyString()
    {
        return myString;
    }

    /**
     * @return the myOtherString
     */
    public String getMyOtherString()
    {
        return myOtherString;
    }

}
