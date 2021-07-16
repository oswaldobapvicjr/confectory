package net.obvj.confectory;

import java.io.InputStream;
import java.util.Optional;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;
import net.obvj.confectory.util.Exceptions;

public class Configuration<T>
{
    private final String namespace;
    private final short precedence;
    private final Source<T> source;
    private final Mapper<InputStream, T> mapper;

    private final Optional<T> bean;
    private final ConfigurationHelper<T> helper;

    protected Configuration(ConfigurationBuilder<T> builder)
    {
        this.namespace = builder.namespace;
        this.precedence = builder.precedence;
        this.source = builder.source;
        this.mapper = builder.mapper;
        this.bean = load(builder);
        this.helper = mapper.configurationHelper(bean.get());
    }

    private Optional<T> load(ConfigurationBuilder<T> builder)
    {
        if (builder.optional)
        {
            return source.loadQuietly(mapper);
        }
        T value = source.load(mapper);
        return Optional.ofNullable(value);
    }

    /**
     * @return the namespace
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * @return the precedence
     */
    public short getPrecedence()
    {
        return precedence;
    }

    /**
     * @return the source
     */
    public Source<T> getSource()
    {
        return source;
    }

    /**
     * @return the value
     */
    public Optional<T> getBean()
    {
        return bean;
    }

    public T getRequiredBean()
    {
        return bean.orElseThrow(() -> Exceptions.configuration("No configuration object available"));
    }

    public boolean getBooleanProperty(String path)
    {
        return helper.getBooleanProperty(path);
    }

    public int getIntProperty(String path)
    {
        return helper.getIntProperty(path);
    }

    public long getLongProperty(String path)
    {
        return helper.getLongProperty(path);
    }

    public float getFloatProperty(String path)
    {
        return helper.getFloatProperty(path);
    }

    public double getDoubleProperty(String path)
    {
        return helper.getDoubleProperty(path);
    }

    public String getStringProperty(String path)
    {
        return helper.getStringProperty(path);
    }

}
