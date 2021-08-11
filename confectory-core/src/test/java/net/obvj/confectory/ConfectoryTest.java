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
