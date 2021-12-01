![confectory-logo](resources/confectory-logo.svg)

[![License](https://img.shields.io/badge/license-apache%202.0-brightgreen.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub Workflow Status](https://img.shields.io/github/workflow/status/oswaldobapvicjr/confectory/Java%20CI%20with%20Maven)](https://github.com/oswaldobapvicjr/confectory/actions/workflows/maven.yml)
[![Coverage](https://img.shields.io/codecov/c/github/oswaldobapvicjr/confectory)](https://codecov.io/gh/oswaldobapvicjr/confectory)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.obvj/confectory-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.obvj/confectory-core)
[![Javadoc](https://javadoc.io/badge2/net.obvj/confectory-core/javadoc.svg)](https://javadoc.io/doc/net.obvj/confectory-core)


The modular, multi-format configuration framework for Java applications.

![confectory-animation](resources/confectory-animation.gif)

---

## Overview

**Confectory** provides a comprehensive API for handling system configuration data. Use it to accept and combine configuration data from multiple **sources** and **formats**.

## Examples

#### Loading configuration data from a local Properties file in the classpath:

````java
Configuration<Properties> config = Configuration.<Properties>builder()
        .source(SourceFactory.classpathFileSource("myapp.properties"))
        .mapper(new PropertiesMapper())
        .build();
````

#### Loading configuration data from a JSON document in the file system:

````java
Configuration<JSONObject> config = Configuration.<JSONObject>builder()
        .source(SourceFactory.fileSource("${HOME}/myapp/myconf.json"))
        .mapper(new JSONObjectMapper())
        .build();
````

> Then access document data using [JSONPath expressions](https://goessner.net/articles/JsonPath/index.html#e2):
> ````java
> String value = config.getString("$.store.name");
> ````

#### Loading XML document from an HTTP server as JSON:

````java
Configuration<JSONObject> config = Configuration.<JSONObject>builder()
        .source(SourceFactory.httpSource("http://myserver:8080/config"))
        .mapper(new XmlToJSONObjectMapper())
        .build();
````

## Features

- Easy configuration setup
- Seamless data query using **JSONPath** or user-defined beans
- Support for the **best providers** available in the community (e.g.: Jackson, GSON)
- Accept **multiple configuration formats** (e.g.: XML, JSON, or YAML) and define **precedence levels** for each one


## How to include it

Confectory was designed to work with the lowest-possible number of transitive dependencies. So, we offer separate modules that can be selected according to the client needs, optimizing your application:

<table>

<tr>
<th>Module</th>
<th>Provider(s)</th>
<th>Properties</th>
<th>XML</th>
<th>JSON</th>
<th>YAML</th>
</tr>

<tr>
<td><b>confectory-core</b></td>
<td>Java only (JDK8+)</td>
<td>:heavy_check_mark:</td>
<td>--</td>
<td>--</td>
<td>--</td>
</tr>

<tr>
<td><b>confectory-datamapper-lite</b></td>
<td>json.org</td>
<td>:heavy_check_mark:</td>
<td>:heavy_check_mark:</td>
<td>:heavy_check_mark:</td>
<td>--</td>
</tr>

<tr>
<td><b>confectory-datamapper-jackson2-json</b></td>
<td>Jackson 2</td>
<td>--</td>
<td>--</td>
<td>:heavy_check_mark:</td>
<td>--</td>
</tr>
<tr>
<td><b>confectory-datamapper-jackson2-xml</b></td>
<td>Jackson + XML Mapper</td>
<td>--</td>
<td>:heavy_check_mark:</td>
<td>:heavy_check_mark:</td>
<td>--</td>
</tr>

<tr>
<td><b>confectory-datamapper-jackson2-yaml</b></td>
<td>Jackson + YAML Mapper</td>
<td>--</td>
<td>--</td>
<td>:heavy_check_mark:</td>
<td>:heavy_check_mark:</td>
</tr>

<tr>
<td><b>confectory-datamapper-gson</b></td>
<td>GSON</td>
<td>--</td>
<td>--</td>
<td>:heavy_check_mark:</td>
<td>--</td>
</tr>

</table>

## Contributing

If you want to contribute to the **Confectory** project, check the [issues](http://obvj.net/confectory/issues) page, or write an e-mail to [oswaldo@obvj.net](mailto:oswaldo@obvj.net).

**Confectory** uses [GitHub Actions](https://docs.github.com/actions) for CI/CD.

---

The Confectory logo and the file-factory animation were created with [Inkscape](http://www.inkscape.org) and [Natron](https://natrongithub.github.io), both **free and open-source** Software tools.
