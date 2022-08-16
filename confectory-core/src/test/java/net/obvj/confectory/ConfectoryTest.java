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

package net.obvj.confectory;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.settings.ConfectorySettings;

/**
 * Unit tests for the {@link Confectory} class.
 *
 * @author FernandoNSC (Fernando Tiannamen)
 */
class ConfectoryTest
{

    @Test
    void constructor_instantiationNotAllowed()
    {
        assertThat(Confectory.class, instantiationNotAllowed());
    }

    @Test
    void ensure_global_default_configuration_exists()
    {
        assertNotNull(Confectory.container());
    }

    @Test
    void settings_sameInstanceDefaultConfiguration()
    {
        assertThat(Confectory.settings(), equalTo(ConfectorySettings.getInstance()));
    }

}
