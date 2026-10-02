package br.com.dpsistemas.cedroerp.dtos;

import java.util.Objects;

public class MunicipioSelectDTO {

    private Long id;
    private String codigoIbge;
    private String nome;

    public MunicipioSelectDTO() {
    }

    public MunicipioSelectDTO(Long id, String codigoIbge, String nome) {
        this.id = id;
        this.codigoIbge = codigoIbge;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MunicipioSelectDTO that = (MunicipioSelectDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}