package br.com.dpsistemas.cedroerp.enumerators;

public enum UnidadeMedidaEnum {
    UN("UN", "Unidade"),
    M3("M3", "Metro Cúbico"),
    M2("M2", "Metro Quadrado"),
    M("M", "Metro Linear"),
    KG("KG", "Quilograma"),
    DZ("DZ", "Dúzia"),
    CX("CX", "Caixa"),
    PCT("PCT", "Pacote");

    private final String sigla;
    private final String descricao;

    UnidadeMedidaEnum(String sigla, String descricao) {
        this.sigla = sigla;
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public String getDescricao() {
        return descricao;
    }
}
