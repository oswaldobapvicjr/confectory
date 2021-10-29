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

package net.obvj.confectory.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

/**
 * A specialized {@code Mapper} that loads the contents of a {@code Source} (e.g.: file,
 * URL) as a {@code String} using the JVM default {@code Charset}, typically for
 * testing/troubleshooting or manual handling purposes.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
public class StringMapper extends AbstractBeanMapper<String> implements Mapper<String>
{

    @Override
    public String apply(InputStream inputStream) throws IOException
    {
        return IOUtils.toString(inputStream, Charset.defaultCharset());
    }

}
