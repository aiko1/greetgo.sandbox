package kz.greetgo.sandbox.controller.model;

public enum SortBy {
    NAME("name"),
    SURNAME("surname"),
    PATRONYMIC("patronymic"),
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
