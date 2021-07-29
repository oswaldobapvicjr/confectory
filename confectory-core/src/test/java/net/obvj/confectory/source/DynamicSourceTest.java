package net.obvj.confectory.source;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private static final Mapper<String> STRING_MAPPER = new StringMapper();

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
        assertThat(source.load(STRING_MAPPER), containsAll("line1")); // the file content
    }

    @Test
    void load_validClasspathFileWithoutPrefix_success()
    {
        source = new DynamicSource<>("testfiles/file1.txt");
        assertThat(source.load(STRING_MAPPER), containsAll("line1")); // the file content
    }
}
