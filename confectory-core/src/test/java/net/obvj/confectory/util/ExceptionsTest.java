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

import static net.obvj.junit.utils.TestUtils.assertException;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Exceptions} class.
 *
 * @author Oswaldo Junior (oswaldo.bapvic.jr)
 */
class ExceptionsTest
{
    private static final String MSG_PATTERN = "arg1=%s,arg2=%s";
    private static final String ARG1 = "abc";
    private static final String ARG2 = "123";
    private static final String EXPECTED_MSG = "arg1=abc,arg2=123";

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(Exceptions.class, instantiationNotAllowed().throwing(IllegalStateException.class));
    }

    @Test
    void illegalArgument_messageAndParams_validMessage()
    {
        assertException(IllegalArgumentException.class, EXPECTED_MSG,
                Exceptions.illegalArgument(MSG_PATTERN, ARG1, ARG2));
    }

    @Test
    void illegalArgument_messageAndParamsAndCause_validMessageAndCause()
    {
        assertException(IllegalArgumentException.class, EXPECTED_MSG, NullPointerException.class,
                Exceptions.illegalArgument(new NullPointerException(), MSG_PATTERN, ARG1, ARG2));
    }

    @Test
    void illegalState_messageAndParams_validMessage()
    {
        assertException(IllegalStateException.class, EXPECTED_MSG, Exceptions.illegalState(MSG_PATTERN, ARG1, ARG2));
    }

    @Test
    void illegalState_messageAndParamsAndCause_validMessageAndCause()
    {
        assertException(IllegalStateException.class, EXPECTED_MSG, NullPointerException.class,
                Exceptions.illegalState(new NullPointerException(), MSG_PATTERN, ARG1, ARG2));
    }

}
