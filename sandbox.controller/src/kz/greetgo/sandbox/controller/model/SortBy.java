package kz.greetgo.sandbox.controller.model;

public enum SortBy {
    CHARM("charm"),
    FIO("surname"),
    NAME("name"),
    AGE("age"),
    TOTALBALANCE("totalBalance"),
    MAXBALANCE("maxBalance"),
    MINBALANCE("minBalance");

    private final String text;

    SortBy(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}