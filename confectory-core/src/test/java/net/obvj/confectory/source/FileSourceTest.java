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
import static org.mockito.BDDMockito.then;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.mapper.StringMapper;

/**
 * Unit tests for the {@link FileSource} class.
 *
 * @author Oswaldo Junior (oswaldo.bapvic.jr)
 */
@ExtendWith(MockitoExtension.class)
class FileSourceTest
{
    private static final String FILE_NOT_FOUND_PATH = "notfound.properties";
    private static final String FILE_FILE1_TXT = "src/test/resources/testfiles/file1.txt";

    private static final FileSource<String> TEST_SOURCE_FILE_NOT_FOUND = new FileSource<>(FILE_NOT_FOUND_PATH);
    private static final FileSource<String> TEST_SOURCE_FILE_FILE1_TXT = new FileSource<>(FILE_FILE1_TXT);

    private static final StringMapper STRING_MAPPER = new StringMapper();

    @Mock
    private InputStream inputStream;
    @Mock
    private Mapper<String> mapper;
    @InjectMocks
    private FileSource<String> fileSource = new FileSource<>("");

    @Test
    void load_fileNotFoundAndOptionalFalse_configurationSourceException()
    {
        assertThat(() -> TEST_SOURCE_FILE_NOT_FOUND.load(STRING_MAPPER, false),
                throwsException(ConfigurationSourceException.class)
                        .withMessage(containsAll(FILE_NOT_FOUND_PATH).ignoreCase())
                        .withCause(FileNotFoundException.class));
    }

    @Test
    void load_fileNotFoundAndOptionalTrue_null()
    {
        assertThat(TEST_SOURCE_FILE_NOT_FOUND.load(STRING_MAPPER, true), equalTo(null));
    }

    @Test
    void load_file1_success()
    {
        assertThat(TEST_SOURCE_FILE_FILE1_TXT.load(STRING_MAPPER, true), containsAll("line1"));
    }

    @Test
    void load_mockedInputStream_mapperAppliesInputStream() throws IOException
    {
        fileSource.load(inputStream, mapper);
        then(mapper).should().apply(inputStream);
    }

}
