package br.com.dpsistemas.cedroerp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CnaeListaDTO {
    @JsonProperty("ListaCnae")
    private List<CnaeDTO> listaCnae;

    public List<CnaeDTO> getListaCnae() {
        return listaCnae;
    }

    public void setListaCnae(List<CnaeDTO> listaCnae) {
        this.listaCnae = listaCnae;
    }
}
