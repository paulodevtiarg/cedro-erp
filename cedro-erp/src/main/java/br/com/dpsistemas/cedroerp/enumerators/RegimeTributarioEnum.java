package br.com.dpsistemas.cedroerp.enumerators;

public enum RegimeTributarioEnum {
    SIMPLES_NACIONAL("SIMPLES NACIONAL"),
    LUCRO_PRESUMIDO("LUCRO PRESUMIDO"),
    LUCRO_REAL("LUCRO REAL"),
    OUTROS("OUTROS");

    private final String descricao;

    RegimeTributarioEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
