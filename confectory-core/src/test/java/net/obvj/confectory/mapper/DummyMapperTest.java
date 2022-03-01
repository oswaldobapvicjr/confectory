package net.obvj.confectory.mapper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.minidev.json.JSONObject;
import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.internal.helper.JsonSmartConfigurationHelper;

/**
 * Unit tests for the {@link DummyMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
class DummyMapperTest
{
    private Mapper<JSONObject> dummyJSONObjectMapper = new DummyMapper<>(JsonSmartConfigurationHelper::new);

    @Test
    void apply_configurationException() throws IOException
    {
        assertThat(() ->
        {
            try
            {
                dummyJSONObjectMapper.apply(new ByteArrayInputStream("".getBytes()));
            }
            catch (IOException exception)
            {
                fail("Unexpected exception", exception);
            }
        }, throwsException(ConfigurationException.class).withMessage("Dummy mapper"));
    }

    @Test
    void configurationHelper_json_jsonSmartConfigurationHelper()
    {
        assertThat(dummyJSONObjectMapper.configurationHelper(new JSONObject()).getClass(),
                equalTo(JsonSmartConfigurationHelper.class));
    }
}
