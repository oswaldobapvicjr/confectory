package net.obvj.confectory.mapper;

import java.util.function.Function;

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
    ConfigurationHelper<O> configurationHelper(O object);
}
