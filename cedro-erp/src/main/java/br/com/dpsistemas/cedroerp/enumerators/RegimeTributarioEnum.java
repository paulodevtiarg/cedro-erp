package br.com.dpsistemas.cedroerp.enumerators;

public enum RegimeTributarioEnum {
    SIMPLES_NACIONAL("Simples Nacional"),
    LUCRO_PRESUMIDO("Lucro Presumido"),
    LUCRO_REAL("Lucro Real"),
    OUTROS("Outros");

    private final String descricao;

    RegimeTributarioEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
