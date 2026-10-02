package br.com.dpsistemas.cedroerp.dtos;

import java.util.Objects;

public class MunicipioDTO {

    private Long id;
    private String codigoIbge;
    private String nome;
    private String uf;

    public MunicipioDTO() {
    }

    public MunicipioDTO(Long id, String codigoIbge, String nome, String uf) {
        this.id = id;
        this.codigoIbge = codigoIbge;
        this.nome = nome;
        this.uf = uf;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MunicipioDTO that = (MunicipioDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}