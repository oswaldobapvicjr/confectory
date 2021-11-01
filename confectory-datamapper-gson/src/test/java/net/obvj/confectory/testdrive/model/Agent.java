package net.obvj.confectory.testdrive.model;

import com.google.gson.annotations.SerializedName;

public class Agent
{
    @SerializedName("class")
    private String clazz;
    private String interval;

    /**
     * @return the clazz
     */
    public String getClazz()
    {
        return clazz;
    }

    /**
     * @param clazz the clazz to set
     */
    public void setClazz(String clazz)
    {
        this.clazz = clazz;
    }

    /**
     * @return the interval
     */
    public String getInterval()
    {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(String interval)
    {
        this.interval = interval;
    }

}
