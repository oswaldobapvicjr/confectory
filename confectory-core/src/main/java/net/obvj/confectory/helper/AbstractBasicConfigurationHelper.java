package net.obvj.confectory.helper;

import java.util.Objects;

import net.obvj.confectory.helper.nullvalue.NullValueProvider;
import net.obvj.confectory.settings.ConfectorySettings;

/**
 * An abstract Configuration Helper object providing common infrastructure for concrete
 * implementations.
 *
 * @param <T> the source type which configuration data is to be retrieved
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public abstract class AbstractBasicConfigurationHelper<T> implements ConfigurationHelper<T>
{
    protected NullValueProvider nullValueProvider = ConfectorySettings.getInstance().getDefaultNullValueProvider();

    @Override
    public void setNullValueProvider(NullValueProvider provider)
    {
        this.nullValueProvider = Objects.requireNonNull(provider, "null is not allowed");
    }

}
