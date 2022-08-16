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

package net.obvj.confectory.source;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsAll;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.containsNone;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.allOf;
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

import net.obvj.confectory.Configuration;
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
    private static final String STRING_CONTENTS = "stringContents";
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
    void load_validStringAndOptionalFalse_loadedSuccessfully()
    {
        assertThat(STRING_SOURCE1.load(STRING_MAPPER, false), equalTo(STRING1));
    }

    @Test
    void load_validStringAndOptionalTrue_loadedSuccessfully()
    {
        assertThat(STRING_SOURCE1.load(STRING_MAPPER, true), equalTo(STRING1));
    }

    @Test
    void load_mockedInputStream_mapperAppliesInputStream() throws IOException
    {
        stringSource.load(inputStream, mapper);
        then(mapper).should().apply(inputStream);
    }

    @Test
    void toString_validString()
    {
        assertThat(new StringSource<>(STRING_CONTENTS).toString().replaceAll("\"", ""),
                allOf(containsAll("StringSource"),
                      containsNone(STRING_CONTENTS)));
    }

}
