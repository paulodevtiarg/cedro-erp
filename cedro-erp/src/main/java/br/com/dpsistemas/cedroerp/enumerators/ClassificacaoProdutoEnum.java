package br.com.dpsistemas.cedroerp.enumerators;

public enum ClassificacaoProdutoEnum {
    MADEIRA_SERRADA("Madeira Serrada"),
    MADEIRA_BRUTA("Madeira Bruta");

    private final String descricao;

    ClassificacaoProdutoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}