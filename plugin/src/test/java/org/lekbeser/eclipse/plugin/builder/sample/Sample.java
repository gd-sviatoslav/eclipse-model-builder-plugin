package org.lekbeser.eclipse.plugin.builder.sample;

public class Sample {
    private String address;

    static class Gold {
        private String money;

        public static GoldBuilder builder() {
            return new GoldBuilder();
        }

        public static class GoldBuilder {
            private String money;

            public GoldBuilder money(String money) {
                this.money = money;
                return this;
            }

            public GoldBuilder from(Gold gold) {
                return money(gold.money);
            }

            public Gold build() {
                Gold gold = new Gold();
                gold.money = money;
                return gold;
            }
        }

    }

}
