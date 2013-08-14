## Eclipse Model Builder Plugin

## About

This is a yet another eclipse plugin to generate fluent builders for POJO/DTO/Model classes. Especially suitable for DTO classes with a lot of fields. It helps to keep an immutability of DTO objects.

## Features
1. Generates builder class with fluent api methods for setting master bean properties (see example below).
1. Works for inner model classes.
1. Generates **from** method to copy values from another bean  (see example below).
1. By making master bean properties private and providing only getters, one can follow an immutability pattern by means of builder object (of generated builder class). See usage section below.

### Generally, inspired by
 - https://github.com/henningjensen/bpep
 - https://github.com/belowm/de.below.bgen

## Usage

### How to install
1. Download the sources, build it with Maven __mvn package__.
1. **Or** [download packaged jar](https://github.com/gd-sviatoslav/eclipse-model-builder-plugin/blob/master/downloads/model-builder-plugin-1.0.0-SNAPSHOT.jar?raw=true).
1. Put the jar file (../plugin/target/model-builder-plugin-1.0.0-SNAPSHOT.jar) into the __eclipse/dropins__ directory.

### Tested on platforms
 - **Indigo** (Eclipse 3.7)
 - **Juno** (Eclipse 4.2)

### How to use

Place cursor inside a model class in the java editor window, right click and select **Source** -> **Generate model builder code...**

Then select which fields you want to expose in the builder. Click **Generate**.

### Example of the result
```
public class Person {
    private String address;

    static class Name {
        private String firstname;
        private String surname;

        public static NameBuilder builder() {
            return new NameBuilder();
        }

        public static class NameBuilder {
            private String firstname;
            private String surname;

            public NameBuilder firstname(String firstname) {
                this.firstname = firstname;
                return this;
            }

            public NameBuilder surname(String surname) {
                this.surname = surname;
                return this;
            }

            public NameBuilder from(Name origin) {
                this.firstname(origin.firstname);
                this.surname(origin.surname);
                return this;
            }

            public Name build() {
                Name m = new Name();
                m.firstname = this.firstname;
                m.surname = this.surname;
                return m;
            }
        }
    }
}
```

### Examples of builder usage
```
Name name = Name.builder().firstname("John").surname("Doe").build();
```
```
Name name2 = Name.builder().from(name).build();
```
### [Download jar](https://github.com/gd-sviatoslav/eclipse-model-builder-plugin/blob/master/downloads/model-builder-plugin-1.0.0-SNAPSHOT.jar?raw=true)

## Enjoy!