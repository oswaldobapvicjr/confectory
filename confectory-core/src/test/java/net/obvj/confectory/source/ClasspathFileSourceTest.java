package net.obvj.confectory.source;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationSourceException;
import net.obvj.confectory.mapper.StringMapper;

/**
 * Unit tests for the {@link ClasspathFileSource} class.
 *
 * @author Oswaldo Junior (oswaldo.bapvic.jr)
 */
class ClasspathFileSourceTest
{
    private static final String FILE_NOT_FOUND_PATH = "notfound.xml";
    private static final String FILE1_PATH = "testfiles/file1.txt";
    private static final String FILE1_CONTENT = "line1";

    private static final ClasspathFileSource<String> TEST_SOURCE_FILE_NOT_FOUND = new ClasspathFileSource<>(FILE_NOT_FOUND_PATH);
    private static final ClasspathFileSource<String> TEST_SOURCE_FILE1 = new ClasspathFileSource<>(FILE1_PATH);

    private static final StringMapper MAPPER = new StringMapper();

    @Test
    void load_fileInClassPath_fileContent()
    {
        assertThat(TEST_SOURCE_FILE1.load(MAPPER), containsAll(FILE1_CONTENT));
    }

    @Test
    void load_fileNotFound_configurationSourceException()
    {
        assertThat(() -> TEST_SOURCE_FILE_NOT_FOUND.load(MAPPER), throwsException(ConfigurationSourceException.class)
                .withMessageContaining("resource not found", FILE_NOT_FOUND_PATH));
    }

    @Test
    void loadQuietly_fileInClassPath_fileContent()
    {
        Optional<String> content = TEST_SOURCE_FILE1.loadQuietly(MAPPER);
        assertTrue(content.isPresent());
        assertThat(content.get(), containsAll(FILE1_CONTENT));
    }

    @Test
    void loadQuietly_fileNotFound_empty()
    {
        assertThat(TEST_SOURCE_FILE_NOT_FOUND.loadQuietly(MAPPER), is(Optional.empty()));
    }

    @Test
    void toString_validString()
    {
        assertThat(TEST_SOURCE_FILE1.toString(), containsAll(ClasspathFileSource.class.getSimpleName(), FILE1_PATH));
    }

}
