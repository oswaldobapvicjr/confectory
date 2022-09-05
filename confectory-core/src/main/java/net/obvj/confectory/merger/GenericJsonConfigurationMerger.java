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

package net.obvj.confectory.merger;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.util.ConfigurationComparator;
import net.obvj.jsonmerge.JsonMergeOption;
import net.obvj.jsonmerge.JsonMerger;
import net.obvj.jsonmerge.provider.JsonProvider;

/**
 * A generic {@code ConfigurationMerger} that combines two {@link Configuration} objects
 * of type JSON.
 * <p>
 * The operation is provider-agnostic and depends on a specialized {@link JsonProvider}
 * which must be specified via constructor.
 * <p>
 * The resulting JSON document from the merge operation shall contain all exclusive
 * objects from source documents and in case of In case of key collisions (i.e., the same
 * key appears in both documents), the following rules will be applied:
 * <ul>
 * <li>for simple values, such as strings, numbers and boolean values, the value from the
 * highest-precedence {@code Configuration} will be selected;</li>
 * <li>if the value is a JSON object in <b>both</b> JSON sources, the two objects will be
 * merged recursively; if the types are not compatible (e.g.: JSON object in one side and
 * simple value or array in the other), then a copy of the object from the
 * highest-precedence {@code Configuration} will be selected as fallback;</li>
 * <li>if the value is a JSON array in <b>both</b> JSON sources, then all elements from
 * both two arrays will be copied <b>distinctively</b> (i.e., repeated elements will not
 * be copied to the resulting JSON document); if the types are not compatible (e.g.: JSON
 * array in one side and simple value or complex object in the other), then a copy of the
 * object from the highest-precedence {@code Configuration} will be selected as
 * fallback</li>
 * </ul>
 * <p>
 * <b>Note: </b> For advanced merge options, refer to {@link JsonMergeOption}.
 *
 * @see ConfigurationMerger
 * @see JsonProvider
 * @see JsonMergeOption
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public class GenericJsonConfigurationMerger<T> extends AbstractConfigurationMerger<T>
{

    private final JsonProvider<T> jsonProvider;

    /**
     * Creates a new JSON Configuration Merger for a specific provider.
     *
     * @param jsonProvider the {@link JsonProvider} to use; not {@code null}
     * @throws NullPointerException if the specified JsonProvider is null
     */
    public GenericJsonConfigurationMerger(JsonProvider<T> jsonProvider)
    {
        this.jsonProvider = requireNonNull(jsonProvider, "The JsonProvider cannot be null");
    }

    @Override
    T doMerge(Configuration<T> config1, Configuration<T> config2, JsonMergeOption... mergeOptions)
    {
        List<T> jsonObjects = sortJsonObjects(config1, config2);
        JsonMerger<T> merger = new JsonMerger<>(jsonProvider);
        return merger.merge(jsonObjects.get(0), jsonObjects.get(1), mergeOptions);
    }

    private List<T> sortJsonObjects(Configuration<T> config1, Configuration<T> config2)
    {
        return asList(config1, config2).stream().sorted(new ConfigurationComparator())
                .map(this::getJsonObjectSafely).collect(Collectors.toList());
    }

    private T getJsonObjectSafely(Configuration<T> config)
    {
        return getBeanSafely(config, () -> (T) jsonProvider.newJsonObject());
    }

}
