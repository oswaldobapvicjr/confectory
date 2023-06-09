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

package net.obvj.confectory.settings;

import static net.obvj.confectory.settings.ConfectorySettings.*;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.DataFetchStrategy;
import net.obvj.confectory.util.ObjectFactory;

/**
 * Unit tests for the {@link ConfectorySettings}.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 0.1.0
 */
@ExtendWith(MockitoExtension.class)
class ConfectorySettingsTest
{
    @Mock
    private DataFetchStrategy dataFetchStrategy;
    @Mock
    private ObjectFactory objectFactory;

    private ConfectorySettings settings = ConfectorySettings.instance();

    @AfterEach
    void reset()
    {
        settings.reset();
    }

    @Test
    void setDataFetchStrategy_null_exceptionAndNoChangePerformed()
    {
        assertThat(settings.getDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
        assertThat(() -> settings.setDataFetchStrategy(null),
                throwsException(NullPointerException.class)
                        .withMessageContaining("DataFetchStrategy must not be null"));
        assertThat(settings.getDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
    }

    @Test
    void setDataFetchStrategy_valid_success()
    {
        assertThat(settings.getDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
        settings.setDataFetchStrategy(dataFetchStrategy);
        assertThat(settings.getDataFetchStrategy(), is(dataFetchStrategy));
    }

    @Test
    void setDefaultObjectFactory_null_exceptionAndNoChangePerformed()
    {
        assertThat(settings.getObjectFactory(), is(INITIAL_OBJECT_FACTORY));
        assertThat(() -> settings.setObjectFactory(null),
                throwsException(NullPointerException.class)
                        .withMessageContaining("ObjectFactory must not be null"));
        assertThat(settings.getObjectFactory(), is(INITIAL_OBJECT_FACTORY));
    }

    @Test
    void setObjectFactory_valid_success()
    {
        assertThat(settings.getObjectFactory(), is(INITIAL_OBJECT_FACTORY));
        settings.setObjectFactory(objectFactory);
        assertThat(settings.getObjectFactory(), is(objectFactory));
    }

}
