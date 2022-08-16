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
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.mapper.StringMapper;

/**
 * Tests for the {@link DynamicSource} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
@ExtendWith(MockitoExtension.class)
class DynamicSourceTest
{
    private static final String OK = "OK";

    private static final String FILE1_CONTENT = "line1";

    private static final Mapper<String> STRING_MAPPER = new StringMapper();

    @Mock
    private Source<String> delegateSource1;
    @Mock
    private Source<String> delegateSource2;

    private DynamicSource<String> source;

    @Test
    void resolveSource_stringWithFilePrefix_urlSource()
    {
        assertThat(DynamicSource.resolveSource("file://").getClass(), equalTo(URLSource.class));
    }

    @Test
    void resolveSource_stringWithHttpPrefix_urlSource()
    {
        assertThat(DynamicSource.resolveSource("http://").getClass(), equalTo(URLSource.class));
    }

    @Test
    void resolveSource_stringWithClasspathPrefix_classpathFileSource()
    {
        assertThat(DynamicSource.resolveSource("classpath://").getClass(), equalTo(ClasspathFileSource.class));
    }

    @Test
    void resolveSource_stringWithoutKnownPrefix_null()
    {
        assertNull(DynamicSource.resolveSource("test"));
    }

    @Test
    void load_validClasspathFileWithClasspathPrefix_success()
    {
        source = new DynamicSource<>("classpath://testfiles/file1.txt");
        assertThat(source.load(STRING_MAPPER), containsAll(FILE1_CONTENT)); // the file content
    }

    @Test
    void load_validClasspathFileWithoutPrefix_success()
    {
        source = new DynamicSource<>("testfiles/file1.txt");
        assertThat(source.load(STRING_MAPPER), containsAll(FILE1_CONTENT)); // the file content
    }

    @Test
    void load_mockedWithFilePrefix_success()
    {
        source = new DynamicSource<>("file:///mockedfile");
        try (MockedStatic<SourceFactory> mocked = mockStatic(SourceFactory.class))
        {
            // The URLSource will be selected directly based on the "file://" prefix
            mocked.when(() -> SourceFactory.<String>urlSource("file:///mockedfile")).thenReturn(delegateSource1);
            when(delegateSource1.load(STRING_MAPPER)).thenReturn(OK);

            assertThat(source.load(STRING_MAPPER), equalTo(OK));
            verify(delegateSource1, times(1)).load(STRING_MAPPER);
        }
    }

    @Test
    void load_mockedWithHttpPrefix_success()
    {
        source = new DynamicSource<>("http://myhost");
        try (MockedStatic<SourceFactory> mocked = mockStatic(SourceFactory.class))
        {
            // The URLSource will be selected directly based on the "http://" prefix
            mocked.when(() -> SourceFactory.<String>urlSource("http://myhost")).thenReturn(delegateSource1);
            when(delegateSource1.load(STRING_MAPPER)).thenReturn(OK);

            assertThat(source.load(STRING_MAPPER), equalTo(OK));
            verify(delegateSource1, times(1)).load(STRING_MAPPER);
        }
    }

    @Test
    void load_mockedWithoutPrefixAndNotFoundInClasspathButFoundInFileSystem_success()
    {
        source = new DynamicSource<>("mockedfile");
        try (MockedStatic<SourceFactory> mocked = mockStatic(SourceFactory.class))
        {
            /*
             * Since no prefix was specified, the system will try class path and file system. It's not
             * required to try the URL source, since URL must always contain the prefix
             */
            mocked.when(() -> SourceFactory.<String>classpathFileSource("mockedfile")).thenReturn(delegateSource1);
            mocked.when(() -> SourceFactory.<String>fileSource("mockedfile")).thenReturn(delegateSource2);

            when(delegateSource1.load(STRING_MAPPER)).thenThrow(
                    new ConfigurationSourceException(new FileNotFoundException("mocked file not found exception")));
            when(delegateSource2.load(STRING_MAPPER)).thenReturn(OK);

            assertThat(source.load(STRING_MAPPER), equalTo(OK));
            verify(delegateSource1, times(1)).load(STRING_MAPPER);
            verify(delegateSource2, times(1)).load(STRING_MAPPER);
        }
    }

    @Test
    void load_mockedFileWithoutPrefixAndFoundInClasspathButExceptionDuringMapping_exception()
    {
        source = new DynamicSource<>("mockedfile");
        try (MockedStatic<SourceFactory> mocked = mockStatic(SourceFactory.class))
        {
            mocked.when(() -> SourceFactory.<String>classpathFileSource("mockedfile")).thenReturn(delegateSource1);

            when(delegateSource1.load(STRING_MAPPER))
                    .thenThrow(new ConfigurationSourceException("file found but exception raised"));

            assertThat(() -> source.load(STRING_MAPPER), throwsException(ConfigurationSourceException.class));
            verify(delegateSource1, times(1)).load(STRING_MAPPER);
            verify(delegateSource2, never()).load(STRING_MAPPER);
        }
    }


    @Test
    void isFileNotFound_exceptionWithFileNotFoundAsCause_true()
    {
        assertTrue(DynamicSource.isFileNotFound(new ConfigurationSourceException(new FileNotFoundException())));
    }

    @Test
    void isFileNotFound_exceptionWithUnknownCause_false()
    {
        assertFalse(DynamicSource.isFileNotFound(new ConfigurationSourceException(new IOException())));
    }


    @Test
    void isFileNotFound_exceptionWithNoCause_false()
    {
        assertFalse(DynamicSource.isFileNotFound(new ConfigurationSourceException("testMessage")));
    }

}
