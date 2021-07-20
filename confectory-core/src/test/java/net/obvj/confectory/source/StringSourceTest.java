package net.obvj.confectory.source;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.then;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.mapper.Mapper;
import net.obvj.confectory.mapper.StringMapper;

/**
 * Unit tests for the {@link StringSource} class.
 *
 * @author Oswaldo Junior (oswaldo.bapvic.jr)
 */
@ExtendWith(MockitoExtension.class)
class StringSourceTest
{
    private static final String STRING1 = "string1";
    private static final StringSource<String> STRING_SOURCE1 = new StringSource<String>(STRING1);
    private static final StringMapper STRING_MAPPER = new StringMapper();

    @Mock
    private InputStream inputStream;
    @Mock
    private Mapper<String> mapper;
    @InjectMocks
    private StringSource<String> stringSource = new StringSource<>(STRING1);

    @Test
    void constructor_nullString_exception()
    {
        assertThat(() -> new StringSource<String>(null),
                throwsException(NullPointerException.class).withMessage("The source string must not be null"));
    }

    @Test
    void load_validString_loadedSuccessfully()
    {
        assertThat(STRING_SOURCE1.load(STRING_MAPPER), equalTo(STRING1));
    }

    @Test
    void loadOptionally_validString_loadedSuccessfully()
    {
        assertThat(STRING_SOURCE1.loadOptionally(STRING_MAPPER).get(), equalTo(STRING1));
    }

    @Test
    void load_mockedInputStream_mapperAppliesInputStream() throws IOException
    {
        stringSource.load(inputStream, mapper);
        then(mapper).should().apply(inputStream);
    }

}
