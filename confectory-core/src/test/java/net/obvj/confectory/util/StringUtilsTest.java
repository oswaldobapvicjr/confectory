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

package net.obvj.confectory.util;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAny;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for the {@link StringUtils} class.
 *
 * @author oswaldo.bapvic.jr
 */
@ExtendWith(MockitoExtension.class)
class StringUtilsTest
{
    private static final String TEST = "test";
    private static final String TEST_$_UNKNOWN = "test=${unknown}";

    @Mock
    private Supplier<String> supplier;

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(StringUtils.class, instantiationNotAllowed());
    }

    @Test
    void expandVariables_stringWithoutVariables_sameString()
    {
        assertThat(StringUtils.expandEnvironmentVariables(TEST), equalTo(TEST));
    }

    @Test
    void expandVariables_stringWithUnknownVariable_sameString()
    {
        assertThat(StringUtils.expandEnvironmentVariables(TEST_$_UNKNOWN), equalTo(TEST_$_UNKNOWN));
    }

    @Test
    void expandVariables_stringWithKnownVariable_sameString()
    {
        assertThat(StringUtils.expandEnvironmentVariables("test=${PATH}"),
                (allOf(startsWith("test="), not(containsAny(("${PATH}"))))));
    }

    @Test
    void defaultIfEmpty_stringNotEmpty_supplierNotUsed()
    {
        assertThat(StringUtils.defaultIfEmpty(TEST, supplier), equalTo(TEST));
        verifyNoInteractions(supplier);
    }

    @Test
    void defaultIfEmpty_stringEmpty_supplierUsed()
    {
        assertThat(StringUtils.defaultIfEmpty(null, () -> TEST), equalTo(TEST));
        assertThat(StringUtils.defaultIfEmpty("", () -> TEST), equalTo(TEST));
    }

}
