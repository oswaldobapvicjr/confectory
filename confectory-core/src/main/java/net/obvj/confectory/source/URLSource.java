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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.performetrics.Counter;
import net.obvj.performetrics.Stopwatch;
import net.obvj.performetrics.util.Duration;

/**
 * A specialized configuration source implementation that loads contents from a URL, that
 * can be a local file or a Web resource.
 * <p>
 * The syntax of a URL is defined by RFC-2396 (Uniform Resource Identifiers: Generic
 * Syntax), and amended by RFC-2732 (Format for Literal IPv6 Addresses in URLs).
 * <p>
 * Examples of valid URLs:
 * <ul>
 * <li>{@code file:///etc/fstab} (local host)</li>
 * <li>{@code file://ftp.server.com/pub/files/foo.xml}</li>
 * <li>{@code http://www.server.com:1080/myservice/config.json}</li>
 * <li>{@code http://192.168.0.10:1080/config?field1=value1&field2=value2}</li>
 * </ul>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.4.0
 */
public class URLSource<T> extends AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(URLSource.class);

    /**
     * Builds a new configuration source for a specific URL.
     *
     * @param string the string to parse as URL for this configuration source
     * @throws ConfigurationSourceException if the specified URL can not be parsed
     */
    public URLSource(String string)
    {
        super(string);
    }

    @Override
    public T load(Mapper<T> mapper)
    {
        return load(parseURL(super.parameter), mapper);
    }

    protected T load(URL url, Mapper<T> mapper)
    {
        Stopwatch stopwatch = Stopwatch.createStarted(Counter.Type.WALL_CLOCK_TIME);
        try (InputStream inputStream = url.openStream())
        {
            LOGGER.info("Loading \"{}\"", url);
            LOGGER.debug("Applying mapper {}", mapper.getClass());

            T mappedObject = mapper.apply(inputStream);

            stopwatch.stop();
            Duration elapsedTime = stopwatch.elapsedTime();

            LOGGER.info("Resource loaded successfully in {}", elapsedTime);
            return mappedObject;
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load resource: %s", url);
        }
    }

    protected static URL parseURL(String string)
    {
        try
        {
            return new URL(string);
        }
        catch (MalformedURLException exception)
        {
            throw new ConfigurationSourceException(exception,
                    "Invalid URL: %s (Either no legal protocol could be found in the string or the URL could not be parsed)",
                    string);
        }
    }

}
