/*
 * Copyright 2023 obvj.net
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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit test for the {@link ParseException} class.
 *
 * @author oswaldo.bapvic.jr
 */
class ParseExceptionTest
{
    private static final String DETAILED_MESSAGE1 = "detailedMessage1";
    private static final String ROOT_CAUSE_MESSAGE1 = "rootCauseMessage1";

    @Test
    void constructor_validMessage_validMessageAndNoCause()
    {
        assertThat(() ->
        {
            throw new ParseException(DETAILED_MESSAGE1);
        },
                throwsException(ParseException.class)
                .withMessage(DETAILED_MESSAGE1)
                .withCause(null));
    }

    @Test
    void constructor_validCause_validMessageAndCause()
    {
        assertThat(() ->
        {
            throw new ParseException(new IllegalArgumentException(ROOT_CAUSE_MESSAGE1));
        },
                throwsException(ParseException.class)
                .withMessage(endsWith(ROOT_CAUSE_MESSAGE1))
                .withCause(IllegalArgumentException.class));
    }

    @Test
    void constructor_validMessageAndCause_validMessageAndCause()
    {
        assertThat(() ->
        {
            throw new ParseException(new IllegalArgumentException(ROOT_CAUSE_MESSAGE1),
                    DETAILED_MESSAGE1);
        },
                throwsException(ParseException.class)
                .withMessage(DETAILED_MESSAGE1)
                .withCause(IllegalArgumentException.class));
    }

}
