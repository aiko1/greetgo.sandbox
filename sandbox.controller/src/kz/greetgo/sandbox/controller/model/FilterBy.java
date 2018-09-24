package kz.greetgo.sandbox.controller.model;

public enum FilterBy {
    NAME("name"),
    SURNAME("surname"),
    PATRONYMIC("patronymic");

    private final String text;

    FilterBy(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}


