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

import net.obvj.confectory.Configuration;
import net.obvj.confectory.merger.options.MergeOption;

/**
 * Base abstraction for merging two {@link Configuration} objects of the same type
 * {@code <T>}.
 *
 * @param <T> the source and target {@code Configuration} type
 *
 * @author oswaldo.bapvic.jr (Oswaldo Junior)
 * @since 2.1.0
 */
public interface ConfigurationMerger<T>
{
    /**
     * Combines two {@code Configuration} objects into a new {@code Configuration}.
     * <p>
     * <ul>
     * <li>The resulting object will receive all the elements of the input
     * {@code Configuration} objects</li>
     * <li>In case of conflicting keys, the values at the highest-precedence
     * {@code Configuration} will be selected</li>
     * <li>The metadata of the highest-precedence {@code Configuration} (namespace and
     * precedence) will be applied to the new {@code Configuration}</li>
     * </ul>
     *
     * @param config1      the first {@code Configuration}; not null
     * @param config2      the second {@code Configuration}; not null
     * @param mergeOptions an array of options on how to merge the objects (optional)
     *
     * @return a new {@code Configuration} resulting from the combination of {@code config1}
     *         and {@code config2}
     */
    Configuration<T> merge(final Configuration<T> config1, final Configuration<T> config2,
            final MergeOption... mergeOptions);
}
