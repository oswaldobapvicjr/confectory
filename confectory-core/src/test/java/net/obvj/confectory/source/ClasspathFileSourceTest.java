/*
 * Copyright 2022 obvj.net
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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.mapper.StringMapper;

/**
 * Unit tests for the {@link ClasspathFileSource} class.
 *
 * @author Oswaldo Junior (oswaldo.bapvic.jr)
 */
@ExtendWith(MockitoExtension.class)
class ClasspathFileSourceTest
{
    private static final String FILE_NOT_FOUND_PATH = "notfound.xml";
    private static final String FILE1_PATH = "testfiles/file1.txt";
    private static final String FILE1_CONTENT = "line1";

    private static final ClasspathFileSource<String> TEST_SOURCE_FILE_NOT_FOUND = new ClasspathFileSource<>(
            FILE_NOT_FOUND_PATH);
    private static final ClasspathFileSource<String> TEST_SOURCE_FILE1 = new ClasspathFileSource<>(FILE1_PATH);

    private static final StringMapper MAPPER = new StringMapper();

    @Mock
    private URL url;
    @Mock
    private InputStream inputStream;
    @Mock
    private Mapper<String> mapper;
    @InjectMocks
    private ClasspathFileSource<String> classpathFileSource = new ClasspathFileSource<>("");

    @Test
    void load_fileInClassPathAndOptionalFalse_fileContent()
    {
        assertThat(TEST_SOURCE_FILE1.load(MAPPER, false), containsAll(FILE1_CONTENT));
    }

    @Test
    void load_fileNotFoundAndOptionalFalse_configurationSourceException()
    {
        assertThat(() -> TEST_SOURCE_FILE_NOT_FOUND.load(MAPPER, false),
                throwsException(ConfigurationSourceException.class)
                        .withMessage(containsAll("resource not found", FILE_NOT_FOUND_PATH).ignoreCase()));
    }

    @Test
    void load_fileInClassPathAndOptionalTrue_fileContent()
    {
        assertThat(TEST_SOURCE_FILE1.load(MAPPER, true), containsAll(FILE1_CONTENT));
    }

    @Test
    void load_fileNotFoundAndOptionalTrue_null()
    {
        assertThat(TEST_SOURCE_FILE_NOT_FOUND.load(MAPPER, true), equalTo(null));
    }

    @Test
    void toString_validString()
    {
        assertThat(TEST_SOURCE_FILE1.toString(), containsAll(ClasspathFileSource.class.getSimpleName(), FILE1_PATH));
    }

    @Test
    void load_mockedUrl_mapperAppliesInputStream() throws IOException
    {
        when(url.openStream()).thenThrow(new IOException());

        assertThat(() -> classpathFileSource.load(url, mapper),
                throwsException(ConfigurationSourceException.class).withCause(IOException.class));
    }

}
