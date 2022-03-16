package net.obvj.confectory.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.InvalidPathException;

/**
 * Unit tests for the {@link JsonPathExpression} class.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.1.0
 */
class JsonPathExpressionTest
{

    @Test
    void root_rootExpression()
    {
        assertEquals("$", JsonPathExpression.ROOT.toString());
    }

    @Test
    void constructor_validBracketNotationExpression_success()
    {
        assertEquals("$['title']", new JsonPathExpression("$['title']").toString());
    }

    @Test
    void constructor_validDotNotationExpression_success()
    {
        assertEquals("$['name']", new JsonPathExpression("$.name").toString());
    }

    @Test
    void constructor_null_exception()
    {
        assertThrows(IllegalArgumentException.class, () -> new JsonPathExpression(null));
    }

    @Test
    void constructor_invalidExpression_exception()
    {
        assertThrows(InvalidPathException.class, () -> new JsonPathExpression("$."));
    }

    @Test
    void appendKey_additionalFieldOnRoot_success()
    {
        assertEquals("$['agents']", new JsonPathExpression("$").appendKey("agents").toString());
    }

    @Test
    void append_additionalField_success()
    {
        assertEquals("$['agent']['class']", new JsonPathExpression("$.agent").append("class").toString());
    }

    @Test
    void append_additionalExpression_success()
    {
        assertEquals("$['agent']['class']", new JsonPathExpression("$.agent").append("['class']").toString());
    }

    @Test
    void append_elementIndex_success()
    {
        assertEquals("$['agents'][0]", new JsonPathExpression("$.agents").append("[0]").toString());
    }

    @Test
    void appendKey_emptyValue_sameExpression()
    {
        JsonPathExpression expression1 = new JsonPathExpression("$.agent.class");
        assertSame(expression1, expression1.appendKey(""));
    }

    @Test
    void append_emptyValue_sameExpression()
    {
        JsonPathExpression expression1 = new JsonPathExpression("$.agent.class");
        assertSame(expression1, expression1.append(""));
    }

}
