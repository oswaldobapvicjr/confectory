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

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link SourceFactory} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
class SourceFactoryTest
{

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(SourceFactory.class, instantiationNotAllowed().throwing(IllegalStateException.class)
                .withMessage("Instantiation not allowed"));
    }

    @Test
    void fileSource_fileSourceClass()
    {
        assertThat(SourceFactory.fileSource("").getClass(), equalTo(FileSource.class));
    }

    @Test
    void classpathFileSource_classpathFileSourceClass()
    {
        assertThat(SourceFactory.classpathFileSource("").getClass(), equalTo(ClasspathFileSource.class));
    }

    @Test
    void stringSource_stringSourceClass()
    {
        assertThat(SourceFactory.stringSource("").getClass(), equalTo(StringSource.class));
    }


}
