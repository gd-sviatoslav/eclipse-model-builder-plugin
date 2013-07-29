## Eclipse Model Builder Plugin

**Plugin is under construction. But seems to be stable.**

-----

### About

This is a yet another eclipse plugin to generate fluent builders for POJO/DTO/Model classes. Especially suitable for DTO classes with a lot of fields. It helps to keep an immutability of DTO objects.

### Features
1. __TBD__

### Generally, inspired by
1. https://github.com/henningjensen/bpep
1. https://github.com/belowm/de.below.bgen

### Usage

#### How to install
1. Download the sources, build it with Maven 'mvn package'.
1. Put the result jar file in the eclipse/dropins directory.

#### How to use

Place cursor inside a model class in the java editor window, right click and select Source -> Generate model builder code...
Then select which fields you want to expose in the builder. Click 'Generate'.

#### The result

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