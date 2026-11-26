package br.com.dpsistemas.cedroerp.dtos;

import java.math.BigDecimal;

public class ProdutoMedidaDTO {

    private Long id;

    // Referência à mãe (necessário para o Spring fazer o bind em edições)
    private Long produtoVariacaoBaseId;

    private BigDecimal comprimentoM;
    private String sku;
    private BigDecimal precoVenda;
    private BigDecimal quantidadeEstoque;
    private Boolean status = true;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProdutoVariacaoBaseId() { return produtoVariacaoBaseId; }
    public void setProdutoVariacaoBaseId(Long produtoVariacaoBaseId) { this.produtoVariacaoBaseId = produtoVariacaoBaseId; }

    public BigDecimal getComprimentoM() { return comprimentoM; }
    public void setComprimentoM(BigDecimal comprimentoM) { this.comprimentoM = comprimentoM; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

    public BigDecimal getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
}