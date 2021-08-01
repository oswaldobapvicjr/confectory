package net.obvj.confectory;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.instantiationNotAllowed;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import net.obvj.confectory.settings.ConfectorySettings;

/**
 * Unit tests for the {@link Confectory} class.
 * 
 * @author FernandoNSC (Fernando Tiannamen)
 */
public class ConfectoryTest
{

    ConfigurationContainer configurationContainer;

    @Before
    public void setup()
    {
        configurationContainer = mock(ConfigurationContainer.class);
    }

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
    public void settings_sameInstanceDefaultConfiguration()
    {
        assertEquals(Confectory.settings(), ConfectorySettings.getInstance());
    }

    @Test
    public void setDefatultContainer_notNull_updatedSuccessfully()
    {
        ConfigurationContainer oldContainer = Confectory.container();
        Confectory.setDefaultContainer(configurationContainer);
        assertNotEquals(oldContainer, configurationContainer);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            Confectory.setDefaultContainer(null);
        });

        String expectedMessage = "The Configuration Container must not be null.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
