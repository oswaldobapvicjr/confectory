/*
 * Copyright 2022 obvj.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.obvj.confectory.mapper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;

import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.mapper.model.Bean;
import net.obvj.confectory.mapper.model.MyBeanWithDate;

/**
 * Unit tests for the {@link JacksonTOMLToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.1.0
 */
class JacksonTOMLToObjectMapperTest
{
    private static final String TEST_TOML_SAMPLE1
            = "intValue = 9\n"
            + "booleanValue = true\n"
            + "array = [\"string1\", \"string2\"]\n"
            + "[section]\n"
            + "string = \"mySectionStringValue1\"\n";

    private static final String DATE1 = "2023-04-30T10:11:12.345";

    private static final String TEST_TOML_SAMPLE2
            = "product = \"Notebook\"\n"
            + "releaseDate= \"" + DATE1 + "\"\n";;

    private Mapper<Bean> mapper = new JacksonTOMLToObjectMapper<>(Bean.class);

    @AfterEach
    private void cleanup()
    {
        JacksonTOMLToObjectMapper.resetModulesCache();
    }

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        Bean result = mapper.apply(toInputStream(TEST_TOML_SAMPLE1));
        assertThat(result.intValue, equalTo(9));
        assertThat(result.booleanValue, equalTo(true));
        List<String> array = result.array;
        assertThat(array.size(), equalTo(2));
        assertThat(array.containsAll(Arrays.asList("string1", "string2")), equalTo(true));
        assertThat(result.section.getString(), equalTo("mySectionStringValue1"));
    }

    @Test
    void apply_tomlSample2WithModuleSupport_validObject() throws IOException
    {
        MyBeanWithDate result = new JacksonTOMLToObjectMapper<>(MyBeanWithDate.class)
                .apply(toInputStream(TEST_TOML_SAMPLE2));

        assertThat(result.product, equalTo("Notebook"));
        assertThat(result.releaseDate, equalTo(LocalDateTime.parse(DATE1)));
    }

    @Test
    void apply_tomlSample2WithoutModuleSupport_exception()
    {
        Mapper<MyBeanWithDate> mapper = new JacksonTOMLToObjectMapper<>(
                MyBeanWithDate.class, true);
        try
        {
            mapper.apply(toInputStream(TEST_TOML_SAMPLE2));
        }
        catch (IOException exception)
        {
            assertThat(exception.getClass(), equalTo(InvalidDefinitionException.class));
            assertThat(exception.getMessage(),
                    containsAll("`java.time.LocalDateTime` not supported by default",
                            "add Module \"com.fasterxml.jackson.datatype:jackson-datatype-jsr310\""));
        }
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new Bean()).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}
