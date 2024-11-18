package org.felipesantos.felipesantos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("fazer")
    A_FAZER,
    @JsonProperty("progresso")
    EM_PROGRESSO,
    @JsonProperty("concluido")
    CONCLUIDO;
}
