package br.com.dpsistemas.cedroerp.enumerators;

public enum TipoPessoaEnum {
    FISICA("Pessoa Física"),
    JURIDICA("Pessoa Jurídica");


    private final String descricao;

    TipoPessoaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
