package net.obvj.confectory.source;

import org.apache.commons.lang3.StringUtils;
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
 * <li>If unable to determine the source, the {@link ClasspathFileSource} will be used as
 * a fallback</li>
 * </ul>
 * <p>
 * For example:
 * <ul>
 * <li>The following instruction creates a dynamic {@code Source} that loads files from
 * the file system:
 *
 * <blockquote><pre>new DynamicSource("file://${TMP}/my-file.properties")</pre></blockquote>
 * </li>
 *
 * <li>The following instruction creates a dynamic {@code Source} that loads a file
 * resource from the Java classpath:
 *
 * <blockquote><pre>new DynamicSource("classpath://my-file.properties")}</pre></blockquote>
 * </li>
 * </ul>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class DynamicSource<T> extends AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicSource.class);

    private static final String CLASSPATH_PREFIX = "classpath://";
    private static final String FILE_PREFIX = "file://";

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
        Source<T> source = resolveSource();
        return source.load(mapper);
    }

    private Source<T> resolveSource()
    {
        if (super.source.startsWith(FILE_PREFIX))
        {
            String path = extractPath(super.source, FILE_PREFIX);
            return new FileSource<>(path);
        }
        if (super.source.startsWith(CLASSPATH_PREFIX))
        {
            String path = extractPath(super.source, CLASSPATH_PREFIX);
            return new ClasspathFileSource<>(path);
        }
        return new ClasspathFileSource<>(super.source);
    }

    private String extractPath(String source, String prefix)
    {
        return StringUtils.substringAfter(source, prefix);
    }

}
