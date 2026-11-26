package br.com.dpsistemas.cedroerp.dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProdutoVariacaoBaseDTO {

    private Long id;
    private Long produtoId; // Referência ao pai

    private Long unidadeMedidaId;
    private String unidadeMedidaSigla;

    private BigDecimal larguraCm;
    private BigDecimal espessuraCm;

    // 🔥 A LISTA DE MEDIDAS (Os Filhos: Comprimento, Preço, etc.)
    private List<ProdutoMedidaDTO> medidas = new ArrayList<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public Long getUnidadeMedidaId() { return unidadeMedidaId; }
    public void setUnidadeMedidaId(Long unidadeMedidaId) { this.unidadeMedidaId = unidadeMedidaId; }

    public String getUnidadeMedidaSigla() { return unidadeMedidaSigla; }
    public void setUnidadeMedidaSigla(String unidadeMedidaSigla) { this.unidadeMedidaSigla = unidadeMedidaSigla; }

    public BigDecimal getLarguraCm() { return larguraCm; }
    public void setLarguraCm(BigDecimal larguraCm) { this.larguraCm = larguraCm; }

    public BigDecimal getEspessuraCm() { return espessuraCm; }
    public void setEspessuraCm(BigDecimal espessuraCm) { this.espessuraCm = espessuraCm; }

    public List<ProdutoMedidaDTO> getMedidas() { return medidas; }
    public void setMedidas(List<ProdutoMedidaDTO> medidas) { this.medidas = medidas; }
}