package net.obvj.confectory.source;

import static org.mockito.Mockito.*;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

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
    void resolveSource_stringWithFilePrefix_fileSource()
    {
        assertThat(DynamicSource.resolveSource("file://").getClass(), equalTo(FileSource.class));
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
    void load_mockedFileWithFilePrefix_success()
    {
        source = new DynamicSource<>("file://mockedfile");
        try (MockedStatic<SourceFactory> mocked = mockStatic(SourceFactory.class))
        {
            mocked.when(() -> SourceFactory.<String>fileSource("mockedfile")).thenReturn(delegateSource1);
            when(delegateSource1.load(STRING_MAPPER)).thenReturn(OK);

            assertThat(source.load(STRING_MAPPER), equalTo(OK));
            verify(delegateSource1, times(1)).load(STRING_MAPPER);
        }
    }

    @Test
    void load_mockedFileWithoutPrefix_success()
    {
        source = new DynamicSource<>("mockedfile");
        try (MockedStatic<SourceFactory> mocked = mockStatic(SourceFactory.class))
        {
            mocked.when(() -> SourceFactory.<String>classpathFileSource("mockedfile")).thenReturn(delegateSource1);
            mocked.when(() -> SourceFactory.<String>fileSource("mockedfile")).thenReturn(delegateSource2);
            when(delegateSource1.load(STRING_MAPPER)).thenThrow(new ConfigurationSourceException("mocked exception"));
            when(delegateSource2.load(STRING_MAPPER)).thenReturn(OK);

            assertThat(source.load(STRING_MAPPER), equalTo(OK));
            verify(delegateSource1, times(1)).load(STRING_MAPPER);
            verify(delegateSource2, times(1)).load(STRING_MAPPER);
        }
    }

}
