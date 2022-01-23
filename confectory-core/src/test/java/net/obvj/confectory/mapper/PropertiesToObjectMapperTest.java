package net.obvj.confectory.mapper;

import static net.obvj.junit.utils.matchers.AdvancedMatchers.throwsException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import net.obvj.confectory.ConfigurationException;
import net.obvj.confectory.helper.BeanConfigurationHelper;
import net.obvj.confectory.util.Property;

/**
 * Unit tests for the {@link PropertiesToObjectMapper} class.
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 1.2.0
 */
class PropertiesToObjectMapperTest
{
    private static final String STRING1 = "string1";
    private static final int INT1 = 1910;

    private static final String TEST_PROPERTIES_CONTENT
                    = "booleanValue=true\n"
                    + "stringValue=string1\n"
                    + "intValue=1910\n";

    static class MyBeanNoExplicitMapping
    {
        Boolean booleanValue;
        String stringValue;
        Integer intValue;

        public MyBeanNoExplicitMapping() {}
    }

    static class MyBeanExplicitMapping
    {
        @Property("booleanValue") boolean b;
        @Property("stringValue") String s;
        @Property("intValue") int i;

        public MyBeanExplicitMapping() {}
    }

    static class MyBeanHybrid
    {
        @Property("booleanValue")
        boolean b;
        @Property
        String stringValue;
        int intValue;
        double unknownDouble;

        public MyBeanHybrid() {}
    }

    static class MyBeanAllFieldsTransient
    {
        transient boolean booleanValue;
        transient String stringValue;
        transient int intValue;

        public MyBeanAllFieldsTransient() {}
    }

    static class MyBeanPrivateConstructor
    {
        private MyBeanPrivateConstructor() {}
    }

    private static ByteArrayInputStream newInputStream()
    {
        return new ByteArrayInputStream(TEST_PROPERTIES_CONTENT.getBytes());
    }

    @Test
    void apply_beanWithNoExplicitMapping_success() throws IOException
    {
        MyBeanNoExplicitMapping bean = new PropertiesToObjectMapper<>(MyBeanNoExplicitMapping.class)
                .apply(newInputStream());

        assertThat(bean.booleanValue, equalTo(true));
        assertThat(bean.stringValue, equalTo(STRING1));
        assertThat(bean.intValue, equalTo(INT1));
    }

    @Test
    void apply_beanWithExplicitMapping_success() throws IOException
    {
        MyBeanExplicitMapping bean = new PropertiesToObjectMapper<>(MyBeanExplicitMapping.class)
                .apply(newInputStream());

        assertThat(bean.b, equalTo(true));
        assertThat(bean.s, equalTo(STRING1));
        assertThat(bean.i, equalTo(INT1));
    }

    @Test
    void apply_hybridBean_success() throws IOException
    {
        MyBeanHybrid bean = new PropertiesToObjectMapper<>(MyBeanHybrid.class)
                .apply(newInputStream());

        assertThat(bean.b, equalTo(true));
        assertThat(bean.stringValue, equalTo(STRING1));
        assertThat(bean.intValue, equalTo(INT1));
        assertThat(bean.unknownDouble, equalTo(0.0));
    }

    @Test
    void apply_beanWithAllFieldsTransient_noDataCopied() throws IOException
    {
        MyBeanAllFieldsTransient bean = new PropertiesToObjectMapper<>(MyBeanAllFieldsTransient.class)
                .apply(newInputStream());

        assertThat(bean.booleanValue, equalTo(false));
        assertThat(bean.stringValue, equalTo(null));
        assertThat(bean.intValue, equalTo(0));
    }

    @Test
    void apply_beanWithPrivateConstructor_configurationException()
    {
        assertThat(() ->
        {
            try
            {
                new PropertiesToObjectMapper<>(MyBeanPrivateConstructor.class)
                        .apply(newInputStream());
            }
            catch (IOException e)
            {
                throw new AssertionError("IOException happened, but ConfigurationException was expected", e);
            }
        }, throwsException(ConfigurationException.class).withCause(ReflectiveOperationException.class));
    }

    @Test
    void configurationHelper_propertiesConfigurationHelper()
    {
        assertThat(new PropertiesToObjectMapper<>(Object.class).configurationHelper(new Object()).getClass(),
                equalTo(BeanConfigurationHelper.class));
    }
}
