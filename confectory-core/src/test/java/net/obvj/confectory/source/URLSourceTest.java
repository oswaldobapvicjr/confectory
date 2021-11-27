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
 * Unit tests for the {@link URLSource} class.
 *
 * @author Oswaldo Junior (oswaldo.bapvic.jr)
 */
@ExtendWith(MockitoExtension.class)
class URLSourceTest
{
    private static final String VALID_URL = "http://obvj.net/confectory";
    private static final String MALFORMED_URL = "stringWithoutProtocol";
    private static final String URL1_CONTENT = "line1";

    private static final URLSource<String> SOURCE_MALFORMED_URL = new URLSource<>(MALFORMED_URL);

    private static final StringMapper MAPPER = new StringMapper();

    @Mock
    private URL url;
    @Mock
    private InputStream inputStream;
    @Mock
    private Mapper<String> mapper;
    @InjectMocks
    private URLSource<String> urlSource = new URLSource<>(VALID_URL);

    @Test
    void load_malformedURLAndOptionalFalse_configurationSourceException()
    {
        assertThat(() -> SOURCE_MALFORMED_URL.load(MAPPER, false),
                throwsException(ConfigurationSourceException.class)
                        .withMessage(containsAll("invalid URL", MALFORMED_URL).ignoreCase()));
    }

    @Test
    void load_malformedURLAndOptionalTrue_null()
    {
        assertThat(SOURCE_MALFORMED_URL.load(MAPPER, true), equalTo(null));
    }

    @Test
    void load_mockedUrl_mapperAppliesInputStream() throws IOException
    {
        when(url.openStream()).thenReturn(inputStream);
        when(mapper.apply(inputStream)).thenReturn(URL1_CONTENT);

        assertThat(urlSource.load(url, mapper), equalTo(URL1_CONTENT));
    }

    @Test
    void load_mockedUrlAndThrowsIOException_configurationSourceException() throws IOException
    {
        when(url.openStream()).thenThrow(new IOException());

        assertThat(() -> urlSource.load(url, mapper),
                throwsException(ConfigurationSourceException.class).withCause(IOException.class));
    }

    @Test
    void parseURL_validURL_success()
    {
        assertThat(URLSource.parseURL(VALID_URL).toString(), equalTo(VALID_URL));
    }

}
