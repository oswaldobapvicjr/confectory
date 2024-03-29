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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.obvj.performetrics.Counter;
import net.obvj.performetrics.Stopwatch;
import net.obvj.performetrics.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.util.StringUtils;

/**
 * A specialized configuration source implementation for loading a local file from the
 * file system.
 * <p>
 * The path may contain system environment variables, which must be placed between
 * <code>"${"</code> and <code>"}"</code>. For example: <code>"${TEMP}/file.txt"</code>
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class FileSource<T> extends AbstractSource<T> implements Source<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSource.class);

    /**
     * Builds a new configuration source for specific local file from the file system.
     *
     * @param path the file path for this configuration source.
     */
    public FileSource(String path)
    {
        super(StringUtils.expandEnvironmentVariables(path));
    }

    @Override
    public T load(Mapper<T> mapper)
    {
        LOGGER.info("Searching file: {}", super.parameter);

        Stopwatch stopwatch = Stopwatch.createStarted(Counter.Type.WALL_CLOCK_TIME);
        try (InputStream inputStream = new FileInputStream(super.parameter))
        {
            LOGGER.debug("Appplying mapper {}", mapper.getClass());
            T mappedObject = load(inputStream, mapper);

            stopwatch.stop();
            Duration elapsedTime = stopwatch.elapsedTime();

            LOGGER.info("File {} loaded successfully", super.parameter);
            LOGGER.info("File loaded in {}", elapsedTime);
            return mappedObject;
        }
        catch (IOException exception)
        {
            throw new ConfigurationSourceException(exception, "Unable to load file: %s", super.parameter);
        }
    }

}
