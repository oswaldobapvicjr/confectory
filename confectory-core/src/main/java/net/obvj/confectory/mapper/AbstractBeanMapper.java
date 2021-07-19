package net.obvj.confectory.mapper;

import java.io.InputStream;

import net.obvj.confectory.helper.ConfigurationHelper;
import net.obvj.confectory.helper.BeanConfigurationHelper;

/**
 * An abstract {@code Mapper} for implementations intended to load the contents of an
 * {@link InputStream} into user-defined beans.
 * <p>
 * Since such implementations require no configuration helper, calling
 * {@link AbstractBeanMapper#configurationHelper(T)} returns an
 * {@link BeanConfigurationHelper}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class AbstractBeanMapper<T> implements Mapper<InputStream, T>
{

    /**
     * @return an {@link BeanConfigurationHelper}, since the configuration for this
     *         type of {@code Mapper} is intended to be retrieved by the user-defined bean.
     */
    @Override
    public ConfigurationHelper<T> configurationHelper(T source)
    {
        return new BeanConfigurationHelper<>(source);
    }

}
