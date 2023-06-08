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

import static net.obvj.confectory.settings.ConfectorySettings.INITIAL_DATA_FETCH_STRATEGY;
import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.DataFetchStrategy;

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

    private ConfectorySettings settings = ConfectorySettings.instance();

    @AfterEach
    void reset()
    {
        settings.reset();
    }

    @Test
    void setDefaultDataFetchStrategy_null_exceptionAndNoChangePerformed()
    {
        assertThat(settings.getDefaultDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
        assertThat(() -> settings.setDefaultDataFetchStrategy(null), throwsException(NullPointerException.class)
                .withMessageContaining("DataFetchStrategy must not be null"));
        assertThat(settings.getDefaultDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
    }

    @Test
    void setDefaultDataFetchStrategy_valid_success()
    {
        assertThat(settings.getDefaultDataFetchStrategy(), is(INITIAL_DATA_FETCH_STRATEGY));
        settings.setDefaultDataFetchStrategy(dataFetchStrategy);
        assertThat(settings.getDefaultDataFetchStrategy(), is(dataFetchStrategy));
    }

}
