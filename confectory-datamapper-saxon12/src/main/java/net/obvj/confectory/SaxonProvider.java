/*
 * Copyright 2024 obvj.net
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

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.UnprefixedElementMatchingPolicy;
import net.sf.saxon.s9api.XPathCompiler;

public class SaxonProvider
{
    private static final Processor PROCESSOR = new Processor(false);

    private SaxonProvider()
    {
        throw new IllegalStateException("Instantiation not allowed");
    }

    public static Processor getProcessor()
    {
        return PROCESSOR;
    }

    /**
     * Create a custom {@link XPathCompiler} for XPath expressions, with special settings.
     * <p>
     * The compiler is preset with the {@link UnprefixedElementMatchingPolicy#ANY_NAMESPACE}.
     *
     * @return a new, custom {@link XPathCompiler}
     */
    public static XPathCompiler newXPathCompiler()
    {
        XPathCompiler compiler = PROCESSOR.newXPathCompiler();
        compiler.setUnprefixedElementMatchingPolicy(UnprefixedElementMatchingPolicy.ANY_NAMESPACE);
        return compiler;
    }

}
