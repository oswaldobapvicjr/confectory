package net.obvj.confectory;

import java.io.InputStream;
import java.util.Optional;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

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
        return bean.orElseThrow(() -> new ConfigurationException("No configuration object available"));
    }

    public boolean getBooleanProperty(String key)
    {
        return helper.getBooleanProperty(key);
    }

    public int getIntProperty(String key)
    {
        return helper.getIntProperty(key);
    }

    public long getLongProperty(String key)
    {
        return helper.getLongProperty(key);
    }

    public double getDoubleProperty(String key)
    {
        return helper.getDoubleProperty(key);
    }

    public String getStringProperty(String key)
    {
        return helper.getStringProperty(key);
    }

}
