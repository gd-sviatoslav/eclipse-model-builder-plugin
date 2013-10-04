package org.lekbeser.eclipse.plugin.builder.sample;

public class Person {
    private String name;
    private Address address;

    static class Identity {
        long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    static class Address extends Identity {
        private String city;
        protected int streetNumber;
        String number;
        
    }

}
