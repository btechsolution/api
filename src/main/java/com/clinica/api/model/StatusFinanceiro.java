package com.clinica.api.model;

public enum StatusFinanceiro {
    PAGO("Pago"),
    PENDENTE("Pendente"),
    ATRASADO("Atrasado");

    private final String descricao;

    StatusFinanceiro(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}