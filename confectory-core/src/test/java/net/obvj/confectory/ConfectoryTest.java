package net.obvj.confectory;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import net.obvj.confectory.settings.ConfectorySettings;

/**
 * Unit tests for the {@link Confectory} class.
 * 
 * @author FernandoNSC (Fernando Tiannamen)
 */
public class ConfectoryTest
{

    ConfigurationContainer configurationContainer = new ConfigurationContainer();
    
    @Test
    public void constructor_instantiationNotAllowed()
    {
        assertThat(Confectory.class, instantiationNotAllowed());
    }

    @Test
    public void ensure_global_default_configuration_exists()
    {
        assertNotNull(Confectory.container());
    }

    @Test
    public void ensure_facade_dataFetchStrategy_and_defaultNullValueProvider()
    {
        assertEquals(Confectory.settings().getDefaultDataFetchStrategy(), ConfectorySettings.getInstance().getDefaultDataFetchStrategy());
        assertEquals(Confectory.settings().getDefaultNullValueProvider(), ConfectorySettings.getInstance().getDefaultNullValueProvider());
    }

    @Test
    public void ensure_global_default_configuration_is_updated()
    {
        ConfigurationContainer oldContainer = Confectory.container();
        Confectory.setDefaultContainer(configurationContainer);
        assertNotEquals(oldContainer, configurationContainer);
    }

}
