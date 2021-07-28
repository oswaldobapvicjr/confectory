package net.obvj.confectory.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.mapper.Mapper;

/**
 * <p>
 * A dynamic {@link Source} implementation which applies a different loading strategy
 * contingent on the path contents.
 * </p>
 * <ul>
 * <li>A path starting with {@code "file://"} will be loaded by the
 * {@link FileSource}</li>
 * <li>A path starting with {@code "classpath://"} will be loaded by the
 * {@link ClasspathFileSource}</li>
 * </ul>
 * <p>
 * For example:
 * <ul>
 * <li>The following instruction creates a dynamic {@code Source} that loads files from
 * the file system:
 *
 * <blockquote>{@code new DynamicSource("file://${TMP}/my-file.properties")}</blockquote>
 * </li>
 *
 * <li>The following instruction creates a dynamic {@code Source} that loads a file
 * resource from the Java classpath:
 *
 * <blockquote>{@code new DynamicSource("classpath://my-file.properties")}</blockquote>
 * </li>
 * </ul>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class DynamicSource<T> extends AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicSource.class);

    /**
     * Builds a new dynamic configuration source from a specific path.
     *
     * @param path the path for this configuration source
     */
    public DynamicSource(String path)
    {
        super(path);
    }

    @Override
    public T load(Mapper<T> mapper)
    {
        LOGGER.info("Searching path: {}", super.source);

        // ****
        // TODO: DynamicSource implementation
        // ****
        return new ClasspathFileSource<T>(super.source).load(mapper);
    }

}
