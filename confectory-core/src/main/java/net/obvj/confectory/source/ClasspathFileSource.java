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
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;

/**
 * A specialized configuration source implementation for loading a local file resource
 * from the classpath.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class ClasspathFileSource<T> extends URLSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathFileSource.class);

    /**
     * Builds a new configuration source for specific local file resource in the classpath.
     *
     * @param path the classpath file name (or relative path) for this configuration source.
     */
    public ClasspathFileSource(String path)
    {
        super(path);
    }

    @Override
    public T load(Mapper<T> mapper)
    {
        URL url = classpathResourceToURL(super.parameter);
        return super.load(url, mapper);
    }

    private static URL classpathResourceToURL(String resource)
    {
        LOGGER.info("Searching classpath resource: {}", resource);

        URL url = ClasspathFileSource.class.getClassLoader().getResource(resource);
        if (url == null)
        {
            String message = String.format("Classpath resource not found: %s", resource);
            LOGGER.warn(message);
            throw new ConfigurationSourceException(new FileNotFoundException(message));
        }

        LOGGER.debug("Classpath resource found: \"{}\"", url.getPath());
        return url;
    }

}
