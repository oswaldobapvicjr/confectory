package net.obvj.confectory.mapper;

import java.util.function.Function;

import net.obvj.confectory.ConfigurationMappingException;
import net.obvj.confectory.helper.ConfigurationHelper;

/**
 * The base interface for a configuration mapper.
 *
 * @param <I> the configuration input type
 * @param <O> the configuration output type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 */
public interface Mapper<I, O> extends Function<I, O>
{
    /**
     * Applies this {@code Mapper} into the given input.
     *
     * @param input the input source
     * @return the mapped object
     *
     * @throws ConfigurationMappingException in case of exceptions during the mapping
     */
    @Override
    O apply(I input);

    /**
     * Creates a new {@link ConfigurationHelper} instance recommended by this {@code Mapper}.
     *
     * @param object the configuration object to be used by the helper
     * @return a new {@link ConfigurationHelper} instance
     */
    ConfigurationHelper<O> configurationHelper(O object);

}
