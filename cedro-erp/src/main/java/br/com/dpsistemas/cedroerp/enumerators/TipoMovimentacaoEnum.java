package br.com.dpsistemas.cedroerp.enumerators;

public enum TipoMovimentacaoEnum {
    ENTRADA("Entrada"),
    SAIDA("Saída"),
    AJUSTE("Ajuste");

    private final String descricao;

    TipoMovimentacaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}