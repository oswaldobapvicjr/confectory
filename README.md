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
- Accept **multiple configuration choices** (e.g.: XML, JSON, or YAML) and define **precedence levels** for each one

---

## Planned releases

<table>
  <tr>
    <th>Version</th>
    <th>Release</th>
    <th>Key features</th>
  </tr>
  <tr>
    <td><b>0.1.0 :heavy_check_mark:</b></td>
    <td>2021-08</td>
    <td>
      <li>Configuration Builder</li>
      <li>Local file sources</li>
      <li><b>Properties</b> format parsing</li>
    </td>
  </tr>
  <tr>
    <td><b>0.2.0 :heavy_check_mark:</b></td>
    <td>2021-09</td>
    <td>
      <li><b>XML and JSON</b> files parsing</li>
      <li>Data fetch using JSONPath</li>
    </td>
  </tr>
  <tr>
    <td><b>0.3.0 :heavy_check_mark:</b></td>
    <td>2021-10</td>
    <td>
      <li><b>XML, JSON and YAML</b> files parsing</li>
      <li>Bean mapping using Jackson 2</li>
    </td>
  </tr>
  <tr>
    <td><b>0.4.0 :heavy_check_mark:</b></td>
    <td>2021-11</td>
    <td>
      <li>JSON files parsing using GSON</li>
      <li><b>URL</b> file source</li>
      <li>Lazy loading</li>
    </td>
  </tr>
</table>


## Contributing

If you want to contribute to the **Confectory** project, check the [issues](http://obvj.net/confectory/issues) page, or write an e-mail to [oswaldo@obvj.net](mailto:oswaldo@obvj.net).

**Confectory** uses [GitHub Actions](https://docs.github.com/actions) for CI/CD.

---

The Confectory logo and the file-factory animation were created with [Inkscape](http://www.inkscape.org) and [Natron](https://natrongithub.github.io), both **free and open-source** Software tools.
