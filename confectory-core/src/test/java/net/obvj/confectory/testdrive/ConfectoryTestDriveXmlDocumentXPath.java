/*
 * Copyright 2023 obvj.net
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

package net.obvj.confectory.testdrive;

import org.w3c.dom.Document;

import net.obvj.confectory.Configuration;
import net.obvj.confectory.mapper.DocumentMapper;
import net.obvj.confectory.source.SourceFactory;

public class ConfectoryTestDriveXmlDocumentXPath
{
    public static void main(String[] args)
    {
        String xml = "<root xmlns:foo=\"http://www.foo.org/\" xmlns:bar=\"http://www.bar.org\">\r\n"
                + "    <employees>\r\n"
                + "        <employee id=\"1\">Johnny Dapp</employee>\r\n"
                + "        <employee id=\"2\">Al Pacino</employee>\r\n"
                + "        <employee id=\"3\">Robert De Niro</employee>\r\n"
                + "        <employee id=\"4\">Kevin Spacey</employee>\r\n"
                + "        <employee id=\"5\">Denzel Washington</employee>\r\n"
                + "        \r\n"
                + "    </employees>\r\n"
                + "    <foo:companies>\r\n"
                + "        <foo:company id=\"9\">Microsoft</foo:company>\r\n"
                + "        <foo:company id=\"10\">IBM</foo:company>\r\n"
                + "        <foo:company id=\"11\">Apple</foo:company>\r\n"
                + "        <foo:company id=\"12\">Oracle</foo:company>\r\n"
                + "        <foo:company id=\"13\"></foo:company>\r\n"
                + "    </foo:companies>\r\n"
                + "</root>";

        Configuration<Document> config = Configuration.<Document>builder()
                .source(SourceFactory.stringSource(xml))
                .mapper(new DocumentMapper())
                .build();

        System.out.println(config.getString("//employee[last()]"));
        System.out.println(config.getString("//employee[@id=3]"));
        System.out.println(config.getString("//company[last()]"));
        System.out.println(config.getString("//company[last()-1]"));
        System.out.println(config.getString("//company[@id=8]"));
        System.out.println(config.getInteger("//company[1]/@id"));

    }
}
