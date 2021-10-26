package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.helper.BeanConfigurationHelper;
import net.obvj.confectory.mapper.model.MyBean;

/**
 * Unit tests for the {@link JacksonJsonToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 0.3.0
 */
class JacksonJsonToObjectMapperTest
{
    private static final String TEST_JSON_SAMPLE1 = "{"
                                                  + "  \"intValue\": 9,"
                                                  + "  \"booleanValue\": true,"
                                                  + "  \"array\": ["
                                                  + "    \"string1\","
                                                  + "    \"string2\""
                                                  + "  ]"
                                                  + "}";

    private JacksonJsonToObjectMapper<MyBean> mapper = new JacksonJsonToObjectMapper<>(MyBean.class);

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        MyBean result = mapper.apply(toInputStream(TEST_JSON_SAMPLE1));
        assertThat(result.intValue, equalTo(9));
        assertThat(result.booleanValue, equalTo(true));
        List<String> array = result.array;
        assertThat(array.size(), equalTo(2));
        assertThat(array.containsAll(Arrays.asList("string1", "string2")), equalTo(true));
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new MyBean()).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}
