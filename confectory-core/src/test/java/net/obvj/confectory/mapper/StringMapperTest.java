package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.helper.BeanConfigurationHelper;

/**
 * Unit tests for the {@link StringMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class StringMapperTest
{
    private static final String TEST_CONTENT = "content";

    private StringMapper mapper = new StringMapper();

    @Test
    void apply_validInputStream_loadedSuccessfully() throws IOException
    {
        String result = mapper.apply(new ByteArrayInputStream(TEST_CONTENT.getBytes()));
        assertThat(result, equalTo(TEST_CONTENT));
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(TEST_CONTENT).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}
