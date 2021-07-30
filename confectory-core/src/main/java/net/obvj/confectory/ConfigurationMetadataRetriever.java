package net.obvj.confectory;

import net.obvj.confectory.helper.nullvalue.NullValueProvider;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * A base interface for objects that retrieve configuration metadata, such as
 * Configuration {@code Source}, {@code Mapper}, and other attributes.
 *
 * @param <T> the target configuration data type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface ConfigurationMetadataRetriever<T>
{

    /**
     * Returns the namespace defined for this {@code Configuration} object.
     *
     * @return the namespace defined for this {@code Configuration}
     */
    String getNamespace();

    /**
     * Returns the precedence value defined for this {@code Configuration} object.
     *
     * @return an integer number representing the order of importance given to this
     *         {@code Configuration}
     */
    int getPrecedence();

    /**
     * Returns the {@code Source} associated with this {@code Configuration} object.
     *
     * @return a {@link Source} instance
     */
    Source<T> getSource();

    /**
     * Returns the {@code Mapper} associated with this {@code Configuration} object.
     *
     * @return a {@link Mapper} instance
     */
    Mapper<T> getMapper();

    /**
     * Returns a flag indicating whether this {@code Configuration} setup is optional.
     *
     * @return {@code true} if this {@code Configuration} setup is optional; {@code false},
     *         otherwise
     */
    boolean isOptional();

    /**
     * Returns the {@code NullValueProvider} associated with this {@code Configuration}.
     *
     * @return a {@link NullValueProvider} instance
     */
    NullValueProvider getNullValueProvider();
}
