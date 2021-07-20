package net.obvj.confectory.source;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.then;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

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

    private static final FileSource<String> TEST_SOURCE_FILE_NOT_FOUND = new FileSource<>(FILE_NOT_FOUND_PATH);

    private static final StringMapper STRING_MAPPER = new StringMapper();

    @Mock
    private InputStream inputStream;
    @Mock
    private Mapper<String> mapper;
    @InjectMocks
    private FileSource<String> fileSource = new FileSource<>("");


    @Test
    void load_fileNotFound_configurationSourceException()
    {
        assertThat(() -> TEST_SOURCE_FILE_NOT_FOUND.load(STRING_MAPPER),
                throwsException(ConfigurationSourceException.class)
                .withMessage(containsAll(FILE_NOT_FOUND_PATH).ignoreCase()).withCause(FileNotFoundException.class));
    }

    @Test
    void loadOptionally_fileNotFound_empty()
    {
        assertThat(TEST_SOURCE_FILE_NOT_FOUND.loadOptionally(STRING_MAPPER), is(Optional.empty()));
    }

    @Test
    void load_mockedInputStream_mapperAppliesInputStream() throws IOException
    {
        fileSource.load(inputStream, mapper);
        then(mapper).should().apply(inputStream);
    }

}
