package net.obvj.confectory.mapper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.helper.BeanConfigurationHelper;
import net.obvj.confectory.mapper.model.Bean;

/**
 * Unit tests for the {@link JacksonYAMLToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.1.0
 */
class JacksonTOMLToObjectMapperTest
{
    private static final String TEST_YAML_SAMPLE1
            = "intValue = 9\n"
            + "booleanValue = true\n"
            + "array = [\"string1\", \"string2\"]\n"
            + "[section]\n"
            + "string = \"mySectionStringValue1\"\n"
            + "\n";

    private Mapper<Bean> mapper = new JacksonTOMLToObjectMapper<>(Bean.class);

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        Bean result = mapper.apply(toInputStream(TEST_YAML_SAMPLE1));
        assertThat(result.intValue, equalTo(9));
        assertThat(result.booleanValue, equalTo(true));
        List<String> array = result.array;
        assertThat(array.size(), equalTo(2));
        assertThat(array.containsAll(Arrays.asList("string1", "string2")), equalTo(true));
        assertThat(result.section.getString(), equalTo("mySectionStringValue1"));
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new Bean()).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}