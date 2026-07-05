package com.bakersin.model;

public enum ProductUnit {
    PER_KG("per kg"),
    PER_PIECE("per piece"),
    PER_GLASS("per glass"),
    PER_BOX("per box");

    private final String label;

    ProductUnit(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
