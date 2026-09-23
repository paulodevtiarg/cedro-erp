package br.com.dpsistemas.cedroerp.enumerators;

public enum EstadoCivilEnum {

    SOLTEIRO("S", "Solteiro(a)"),
    CASADO("C", "Casado(a)"),
    DIVORCIADO("D", "Divorciado(a)"),
    VIUVO("V", "Viúvo(a)"),
    SEPARADO("SE", "Separado(a)"),
    UNIAO_ESTAVEL("UE", "União Estável"),
    NAO_INFORMADO("NI", "Não Informado"),
    OUTRO("O", "Outro");

    private final String codigo;
    private final String descricao;

    EstadoCivilEnum(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EstadoCivilEnum fromCodigo(String codigo) {

        for (EstadoCivilEnum estadoCivil : EstadoCivilEnum.values()) {

            if (estadoCivil.getCodigo().equals(codigo)) {
                return estadoCivil;
            }
        }

        throw new IllegalArgumentException(
                "Código de Estado Civil inválido: " + codigo
        );
    }
}