package net.obvj.confectory.testdrive.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Agent
{
    @JsonProperty("class")
    private String clazz;
    @JsonProperty
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
