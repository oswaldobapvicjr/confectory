package net.obvj.confectory;

import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.source.Source;

/**
 * A base interface for retrieving configuration metadata, such as Configuration
 * {@code Source}, {@code Mapper}, and other attributes.
 *
 * @param <T> the target configuration data type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public interface ConfigurationMetadata<T>
{

    /**
     * @return the namespace
     */
    String getNamespace();

    /**
     * @return the precedence
     */
    int getPrecedence();

    /**
     * @return the source
     */
    Source<T> getSource();

    /**
     * @return the mapper
     */
    Mapper<T> getMapper();

    /**
     * @return the optional
     */
    boolean isOptional();

}
