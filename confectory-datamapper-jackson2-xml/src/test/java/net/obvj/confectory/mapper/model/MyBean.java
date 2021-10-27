package net.obvj.confectory.mapper.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyBean
{
    @JsonProperty
    public int intValue;
    @JsonProperty
    public boolean booleanValue;
    @JsonProperty("array")
    public List<String> array;

}
