/*
 * Copyright 2021 obvj.net
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.internal.helper.BeanConfigurationHelper;
import net.obvj.confectory.testdrive.model.Customer;

/**
 * Unit tests for the {@link YAMLToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 1.1.0
 */
class YAMLToObjectMapperTest
{
    private static final String TEST_YAML_SAMPLE1
            = "firstName: John\n"
            + "height: 1.75\n"
            + "homeAddress:\n"
            + "  line: line 1\n"
            + "contactDetails: []\n"
            + "\n";

    private Mapper<Customer> mapper = new YAMLToObjectMapper<>(Customer.class);

    private ByteArrayInputStream toInputStream(String content)
    {
        return new ByteArrayInputStream(content.getBytes());
    }

    @Test
    void apply_validInputStream_validJsonNode() throws IOException
    {
        Customer result = mapper.apply(toInputStream(TEST_YAML_SAMPLE1));
        assertThat(result.getFirstName(), equalTo("John"));
        assertThat(result.getHeight(), equalTo(1.75));
        assertThat(result.getHomeAddress().getLine(), equalTo("line 1"));
        assertThat(result.getContactDetails(), equalTo(Collections.emptyList()));
    }

    @Test
    void configurationHelper_beanConfigurationHelper()
    {
        assertThat(mapper.configurationHelper(new Customer()).getClass(), equalTo(BeanConfigurationHelper.class));
    }
}
