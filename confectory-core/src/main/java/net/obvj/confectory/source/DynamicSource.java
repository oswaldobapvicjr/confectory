/*
 * Copyright 2021 obvj.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.obvj.confectory.source;

import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;

/**
 * <p>
 * A dynamic {@link Source} implementation which applies a different loading strategy
 * contingent on the path contents.
 * </p>
 * <ul>
 * <li>A path starting with {@code "file://"} or {@code "http://"} will be loaded by the
 * {@link URLSource}</li>
 * <li>A path starting with {@code "classpath://"} will be loaded by the
 * {@link ClasspathFileSource}</li>
 * </ul>
 * <p>
 * For example:
 * <ul>
 * <li>The following instruction creates a dynamic {@code Source} that loads files from
 * the file system:
 *
 * <blockquote><pre>new DynamicSource("file:///path/my-file.properties")</pre></blockquote>
 * </li>
 *
 * <li>The following instruction creates a dynamic {@code Source} that loads a file
 * resource from the Java classpath:
 *
 * <blockquote><pre>new DynamicSource("classpath://my-file.properties")</pre></blockquote>
 * </li>
 * </ul>
 * <p>
 * If unable to determine the source by prefix, the system will do best efforts
 * to load the object by applying different {@link Source} implementations.
 * </p>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 *
 * @see FileSource
 * @see ClasspathFileSource
 */
public class DynamicSource<T> extends AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicSource.class);

    private static final String CLASSPATH_PREFIX = "classpath://";
    private static final String FILE_PREFIX = "file://";
    private static final String HTTP_PREFIX = "http://";

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
        LOGGER.info("Searching path: {}", super.parameter);
        Source<T> source = resolveSource(super.parameter);
        return source != null ? source.load(mapper) : trySources(mapper);
    }

    /**
     * Try to determine the {@link Source} implementation based on the path contents.
     *
     * @param path the path to be checked
     * @return the {@link Source} to be applied; or {@code null} if unable to be determined
     */
    protected static <T> Source<T> resolveSource(String path)
    {
        if (path.startsWith(CLASSPATH_PREFIX))
        {
            String pathPart = extractPath(path, CLASSPATH_PREFIX);
            return SourceFactory.classpathFileSource(pathPart);
        }
        if (StringUtils.startsWithAny(path, FILE_PREFIX, HTTP_PREFIX))
        {
            return SourceFactory.urlSource(path);
        }
        return null;
    }

    private static String extractPath(String source, String prefix)
    {
        return StringUtils.substringAfter(source, prefix);
    }

    /**
     * Try different sources (best effort in case the {@link Source} can not be determined).
     *
     * @param mapper the {@link Mapper} to be applied
     * @return the mapped object
     * @throws ConfigurationSourceException if unable to load
     */
    private T trySources(Mapper<T> mapper)
    {
        LOGGER.debug("Trying to load as ClasspathFileSource...");
        try
        {
            return SourceFactory.<T>classpathFileSource(super.parameter).load(mapper);
        }
        catch (ConfigurationSourceException exception)
        {
            if (isFileNotFound(exception))
            {
                LOGGER.debug("Not found in classpath. Trying as FileSource...");
                return SourceFactory.<T>fileSource(super.parameter).load(mapper);
            }
            // The exception was raised due to problems in the mapping part,
            // so we do not need to try a different source.
            throw exception;
        }
    }

    protected static boolean isFileNotFound(ConfigurationSourceException exception)
    {
        Throwable cause = exception.getCause();
        return cause != null && FileNotFoundException.class.isAssignableFrom(cause.getClass());
    }

}
