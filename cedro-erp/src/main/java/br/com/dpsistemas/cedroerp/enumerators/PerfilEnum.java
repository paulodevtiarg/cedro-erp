package br.com.dpsistemas.cedroerp.enumerators;

public enum PerfilEnum {
    ADMIN("Administrador"),
    GESTOR("Gestor"),
    USUARIO("Usuario"),
    VENDEDOR("Vendedor");

    private final String descricao;

    PerfilEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
