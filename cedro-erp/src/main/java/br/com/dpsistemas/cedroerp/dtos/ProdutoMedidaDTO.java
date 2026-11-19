package br.com.dpsistemas.cedroerp.dtos;

import java.math.BigDecimal;

public class ProdutoMedidaDTO {

    private Long id;
    private Long produtoId;

    // Dados "achatados" para exibir na tabela
    private String nomeProduto; // Ex: Tábua de Taipa 2x30
    private String sku;

    private BigDecimal comprimentoM;
    private BigDecimal precoVenda;
    private BigDecimal quantidadeEstoque;
    private Boolean status;

    // FILTROS ESPECÍFICOS DE ESTOQUE
    private String filtroSku;
    private String filtroProduto;
    private String filtroTipoMadeira; // Para buscar todas as "Tábuas" ou todos os "Caibros"
    private Integer filtroStatus = 1;
    private Integer filtroEstoqueBaixo; // 1 = Sim (mostrar estoque abaixo de X), 0 = Não
    private Integer page = 0;
    private Integer size = 10;

    // Getters e Setters
    // ...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getComprimentoM() {
        return comprimentoM;
    }

    public void setComprimentoM(BigDecimal comprimentoM) {
        this.comprimentoM = comprimentoM;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public BigDecimal getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getFiltroSku() {
        return filtroSku;
    }

    public void setFiltroSku(String filtroSku) {
        this.filtroSku = filtroSku;
    }

    public String getFiltroProduto() {
        return filtroProduto;
    }

    public void setFiltroProduto(String filtroProduto) {
        this.filtroProduto = filtroProduto;
    }

    public String getFiltroTipoMadeira() {
        return filtroTipoMadeira;
    }

    public void setFiltroTipoMadeira(String filtroTipoMadeira) {
        this.filtroTipoMadeira = filtroTipoMadeira;
    }

    public Integer getFiltroStatus() {
        return filtroStatus;
    }

    public void setFiltroStatus(Integer filtroStatus) {
        this.filtroStatus = filtroStatus;
    }

    public Integer getFiltroEstoqueBaixo() {
        return filtroEstoqueBaixo;
    }

    public void setFiltroEstoqueBaixo(Integer filtroEstoqueBaixo) {
        this.filtroEstoqueBaixo = filtroEstoqueBaixo;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}