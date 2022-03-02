package net.obvj.confectory.internal.merger;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.internal.helper.PropertiesConfigurationHelper;
import net.obvj.confectory.mapper.DummyMapper;
import net.obvj.confectory.mapper.PropertiesMapper;
import net.obvj.confectory.source.DummySource;
import net.obvj.confectory.source.StringSource;

/**
 * Unit tests for the {@link PropertiesConfigurationMerger}.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.1.0
 */
@ExtendWith(MockitoExtension.class)
class PropertiesConfigurationMergerTest
{
    private static final String STRING1 = "conflictingKey=leftSideValue\n"
                                        + "leftSideKey=leftSideValue\n";

    private static final String STRING2 = "conflictingKey=rightSideValue\n"
                                        + "rightSideKey=rightSideValue\n";

    private static final Configuration<Properties> CONFIG1 = Configuration.<Properties>builder()
            .source(new StringSource<>(STRING1)).precedence(1).namespace("ns1").mapper(new PropertiesMapper())
            .build();

    private static final Configuration<Properties> CONFIG2 = Configuration.<Properties>builder()
            .source(new StringSource<>(STRING2)).precedence(9).namespace("ns2").mapper(new PropertiesMapper())
            .build();

    @Mock
    private Configuration<Properties> configMock;

    private ConfigurationMerger<Properties> merger = new PropertiesConfigurationMerger();

    @Test
    void merge_config1ToTheLeftAndConfig2ToTheRight_success()
    {
        Configuration<Properties> result = merger.merge(CONFIG1, CONFIG2);
        assertMergeOfConfig1AndConfig2(result);
    }

    @Test
    void merge_config2ToTheLeftAndConfig1ToTheRight_success()
    {
        Configuration<Properties> result = merger.merge(CONFIG2, CONFIG1);
        assertMergeOfConfig1AndConfig2(result);
    }

    private void assertMergeOfConfig1AndConfig2(Configuration<Properties> result)
    {
        // Check configuration data
        assertThat(result.get("leftSideKey"), equalTo(CONFIG1.get("leftSideKey")));
        assertThat(result.get("rightSideKey"), equalTo(CONFIG2.get("rightSideKey")));
        assertThat(result.get("conflictingKey"), equalTo(CONFIG2.get("conflictingKey"))); // higher precedence

        // Check configuration metadata
        assertThat(result.getNamespace(), equalTo(CONFIG2.getNamespace()));
        assertThat(result.getPrecedence(), equalTo(CONFIG2.getPrecedence()));
        assertCommonConfigurationMetadata(result);
    }

    private void assertCommonConfigurationMetadata(Configuration<Properties> result)
    {
        assertThat(result.getSource().getClass(), equalTo(DummySource.class));
        assertThat(result.getMapper().getClass(), equalTo(DummyMapper.class));
        assertThat(result.getMapper().configurationHelper(result.getBean()).getClass(),
                equalTo(PropertiesConfigurationHelper.class));
    }

    @Test
    void merge_config1Null_nullPointerException()
    {
        assertThat(() -> merger.merge(null, CONFIG2),
                throwsException(NullPointerException.class).withMessageContaining("must not be null"));
    }

    @Test
    void merge_config2Null_nullPointerException()
    {
        assertThat(() -> merger.merge(CONFIG1, null),
                throwsException(NullPointerException.class).withMessageContaining("must not be null"));
    }

    @Test
    void merge_config1PropertiesNull_newConfigBeanSameAsConfig2()
    {
        when(configMock.getBean()).thenReturn(null);
        Configuration<Properties> result = merger.merge(configMock, CONFIG2);
        assertSame(CONFIG2.getBean(), result.getBean());
        assertNotSame(CONFIG2, result);
        assertCommonConfigurationMetadata(result);
    }

    @Test
    void merge_config2PropertiesNull_newConfigBeanSameAsConfig1()
    {
        when(configMock.getBean()).thenReturn(null);
        Configuration<Properties> result = merger.merge(CONFIG1, configMock);
        assertSame(CONFIG1.getBean(), result.getBean());
        assertNotSame(CONFIG1, result);
        assertCommonConfigurationMetadata(result);
    }

}
