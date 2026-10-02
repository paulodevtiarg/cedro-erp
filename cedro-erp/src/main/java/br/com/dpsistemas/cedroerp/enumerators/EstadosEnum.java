package br.com.dpsistemas.cedroerp.enumerators;

public enum EstadosEnum {

    RO("RO", "Rondônia", "11"),
    AC("AC", "Acre", "12"),
    AM("AM", "Amazonas", "13"),
    RR("RR", "Roraima", "14"),
    PA("PA", "Pará", "15"),
    AP("AP", "Amapá", "16"),
    TO("TO", "Tocantins", "17"),
    MA("MA", "Maranhão", "21"),
    PI("PI", "Piauí", "22"),
    CE("CE", "Ceará", "23"),
    RN("RN", "Rio Grande do Norte", "24"),
    PB("PB", "Paraíba", "25"),
    PE("PE", "Pernambuco", "26"),
    AL("AL", "Alagoas", "27"),
    SE("SE", "Sergipe", "28"),
    BA("BA", "Bahia", "29"),
    MG("MG", "Minas Gerais", "31"),
    ES("ES", "Espírito Santo", "32"),
    RJ("RJ", "Rio de Janeiro", "33"),
    SP("SP", "São Paulo", "35"),
    PR("PR", "Paraná", "41"),
    SC("SC", "Santa Catarina", "42"),
    RS("RS", "Rio Grande do Sul", "43"),
    MS("MS", "Mato Grosso do Sul", "50"),
    MT("MT", "Mato Grosso", "51"),
    GO("GO", "Goiás", "52"),
    DF("DF", "Distrito Federal", "53");

    private final String codigo;
    private final String nome;
    private final String codigoIbge;

    EstadosEnum(String codigo, String nome, String codigoIbge) {
        this.codigo = codigo;
        this.nome = nome;
        this.codigoIbge = codigoIbge;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public static EstadosEnum fromCodigo(String codigo) {
        for (EstadosEnum estado : EstadosEnum.values()) {
            if (estado.getCodigo().equalsIgnoreCase(codigo)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado inválido: " + codigo);
    }

    public static EstadosEnum fromCodigoIbge(String codigoIbge) {
        for (EstadosEnum estado : EstadosEnum.values()) {
            if (estado.getCodigoIbge().equals(codigoIbge)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código IBGE inválido: " + codigoIbge);
    }
}