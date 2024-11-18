package org.felipesantos.felipesantos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Prioridade {
    @JsonProperty("baixa")
    BAIXA(0),
    @JsonProperty("média")
    MEDIA(1),
    @JsonProperty("alta")
    ALTA(2);

    private final int valor;

    Prioridade(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
