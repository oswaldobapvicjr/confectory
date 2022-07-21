package net.obvj.confectory.merger.options;

import static net.obvj.confectory.util.StringUtils.requireNonBlankAndTrim;

import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;

import com.jayway.jsonpath.InvalidPathException;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.merger.GenericJsonConfigurationMerger;
import net.obvj.confectory.util.JsonPathExpression;

/**
 * An object that configures how to merge JSON objects.
 *
 * @author oswaldo.bapvic.jr
 * @since 2.3.0
 * @see GenericJsonConfigurationMerger
 */
public class JsonMergeOption implements MergeOption
{
    private Optional<Pair<String, JsonPathExpression>> distinctKey = Optional.empty();

    /**
     * Specifies a distinct key for objects inside an array identified by a given
     * {@code JsonPath}.
     * <p>
     * For example, consider the following document: <blockquote>
     *
     * <pre>
     * {
     *   "params": [
     *     {
     *       "param": "name",
     *       "value": "John Doe"
     *     },
     *     {
     *       "param": "age",
     *       "value": 33
     *     }
     *   ]
     * }
     * </pre>
     *
     * </blockquote>
     *
     * <p>
     * A {@code MergeOption} of key {@code "param"} associated with the {@code JsonPath}
     * expression {@code "$.params"} tells the algorithm to check for distinct objects
     * identified by the value inside the {@code "param"} field during the merge of the
     * {@code "$.params"} array.
     * <p>
     * In other words, if two JSON documents contain different objects identified by the same
     * key inside that array, then only the object from the highest-precedence
     * {@link Configuration} will be selected.
     * <p>
     * A {@code JsonPath} expression can be specified using either dot- or bracket-notation,
     * but complex expressions containing filters, script, subscript, or union operations, are
     * not supported.
     *
     * @param key      the key to be considered unique for objects inside an array; not blank
     * @param jsonPath a {@code JsonPath} expression that identifies the array; not empty
     *
     * @return a new {@link JsonMergeOption} with the specified pair of distinct key and path
     *
     * @throws IllegalArgumentException if one of the parameters is null or blank
     * @throws InvalidPathException     if the specified JsonPath expression is invalid
     */
    public static JsonMergeOption distinctKey(String key, String jsonPath)
    {
        return distinctKey(key, new JsonPathExpression(jsonPath));
    }

    /**
     * Specifies a distinct key for objects inside an array identified by a given
     * {@code JsonPathExpression}.
     * <p>
     * For example, consider the following document: <blockquote>
     *
     * <pre>
     * {
     *   "params": [
     *     {
     *       "param": "name",
     *       "value": "John Doe"
     *     },
     *     {
     *       "param": "age",
     *       "value": 33
     *     }
     *   ]
     * }
     * </pre>
     *
     * </blockquote>
     *
     * <p>
     * A {@code MergeOption} of key {@code "param"} associated with the {@code JsonPath}
     * expression {@code "$.params"} tells the algorithm to check for distinct objects
     * identified by the value inside the {@code "param"} field during the merge of the
     * {@code "$.params"} array.
     * <p>
     * In other words, if two JSON documents contain different objects identified by the same
     * key inside that array, then only the object from the highest-precedence
     * {@link Configuration} will be selected.
     * <p>
     * <b>Note:</b> Complex {@code JsonPath} expressions containing filters, script,
     * subscript, or union operations, are not supported.
     *
     * @param key  the key to be considered unique for objects inside an array; not blank
     * @param path a {@link JsonPathExpression} that identifies the array; not empty
     *
     * @return a new {@link JsonMergeOption} with the specified pair of distinct key and path
     *
     * @throws IllegalArgumentException if one of the specified parameters is null or blank
     * @throws InvalidPathException     if the specified JsonPath expression is invalid
     */
    public static JsonMergeOption distinctKey(String key, JsonPathExpression path)
    {
        String trimmedKey = requireNonBlankAndTrim(key, "The key must not be null or blank");
        JsonMergeOption option = new JsonMergeOption();
        option.distinctKey = Optional.of(Pair.of(trimmedKey, path));
        return option;
    }

    /**
     * @return a pair of distinct key and path
     */
    public Optional<Pair<String, JsonPathExpression>> getDistinctKey()
    {
        return distinctKey;
    }

}
