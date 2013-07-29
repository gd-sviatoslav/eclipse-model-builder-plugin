# Eclipse Model Builder Plugin

**Plugin is under construction..**

### TODOes
1. store generation settings as plugin settings
1. keep cursor position after builder code is added
1. generate static method builder() which returnes Builder object for enclosing model classes
1. generate builders for inner classes in a parent scope
1. support inner classes old ~Builder removal
1. copy methods: from/to builder and from/to enclosing class
1. generate build methods by super class fields (or hierarchy of builders)
1. downloadable jar

-----

## About

This is a yet another eclipse plugin to generate fluent builders for POJO/DTO/Model classes. Especially suitable for DTO classes with a lot of fields. It helps to keep an immutability of DTO objects.

## Features
1. <TBD>

## Generally, inspired by
1. https://github.com/henningjensen/bpep
1. https://github.com/belowm/de.below.bgen

## Usage

### How to install
1. Download the sources, build it with Maven 'mvn package'.
1. Put the result jar file in the eclipse/dropins directory.

### How to use

Place cursor inside a model class in the java editor window, right click and select Source -> Generate Model Builder Code...
Then select which fields you want to expose in the builder. Click 'Generate'.
