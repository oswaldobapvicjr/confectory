package net.obvj.confectory;

import java.io.InputStream;

import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

public class ConfigurationBuilder<T>
{
    protected String namespace;
    protected short precedence;
    protected Source<T> source;
    protected Mapper<InputStream, T> mapper;
    protected boolean optional;

    public ConfigurationBuilder<T> namespace(String namespace)
    {
        this.namespace = namespace;
        return this;
    }

    public ConfigurationBuilder<T> precedence(short precedence)
    {
        this.precedence = precedence;
        return this;
    }

    public ConfigurationBuilder<T> source(Source<T> source)
    {
        this.source = source;
        return this;
    }

    public ConfigurationBuilder<T> mapper(Mapper<InputStream, T> mapper)
    {
        this.mapper = mapper;
        return this;
    }

    public ConfigurationBuilder<T> optional(boolean optional)
    {
        this.optional = optional;
        return this;
    }

    public Configuration<T> build()
    {
        return new Configuration<>(this);
    }

}
