package org.lekbeser.eclipse.plugin.builder.sample;

public class Sample {
    private String address;

    static class Name {
        private String firstname;
        private String surname;

        public static class Builder {
            private String firstname;
            private String surname;

            public Builder firstname(String firstnameParam) {
                this.firstname = firstnameParam;
                return this;
            }

            public Builder surname(String surnameParam) {
                this.surname = surnameParam;
                return this;
            }

            public Name build() {
                Name name = new Name();
                name.firstname = firstname;
                name.surname = surname;
                return name;
            }
        }
        
    }

    public static class PersonBuilder {

        public Sample build() {
            Sample result = new Sample(null);

            return result;
        }
    }

    public static PersonBuilder builder() {
        return new PersonBuilder();
    }

    public static class Builder {
        private String address;

        public Builder() {
        }

        public Builder(Sample bean) {
            this.address = bean.address;
        }

        public Builder address(String addressParam) {
            this.address = addressParam;
            return this;
        }

        public Sample build() {
            return new Sample(this);
        }
    }

    public Sample(Builder builder) {
        this.address = builder.address;
    }

}
