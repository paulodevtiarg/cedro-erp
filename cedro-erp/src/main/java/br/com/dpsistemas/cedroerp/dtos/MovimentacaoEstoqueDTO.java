package br.com.dpsistemas.cedroerp.dtos;

import br.com.dpsistemas.cedroerp.enumerators.TipoMovimentacaoEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MovimentacaoEstoqueDTO {

    private Long id;
    private Long produtoMedidaId;

    // Dados achatados para exibição
    private String produtoNome;
    private String sku;
    private String unidadeSigla;

    private TipoMovimentacaoEnum tipoMovimentacao;
    private BigDecimal quantidade;
    private String motivo;
    private LocalDateTime dataMovimentacao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;

    // Getters e Setters
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    // Filtros
    private String filtroProduto;
    private String filtroTipo;
    private Integer page = 0;
    private Integer size = 10;

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String getDataMovimentacaoFormatada() {
        return dataMovimentacao == null ? "" : dataMovimentacao.format(FORMATO_DATA_HORA);
    }

    // Getters e Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProdutoMedidaId() { return produtoMedidaId; }
    public void setProdutoMedidaId(Long produtoMedidaId) { this.produtoMedidaId = produtoMedidaId; }
    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getUnidadeSigla() { return unidadeSigla; }
    public void setUnidadeSigla(String unidadeSigla) { this.unidadeSigla = unidadeSigla; }
    public TipoMovimentacaoEnum getTipoMovimentacao() { return tipoMovimentacao; }
    public void setTipoMovimentacao(TipoMovimentacaoEnum tipoMovimentacao) { this.tipoMovimentacao = tipoMovimentacao; }
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getDataMovimentacao() { return dataMovimentacao; }
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) { this.dataMovimentacao = dataMovimentacao; }
    public String getFiltroProduto() { return filtroProduto; }
    public void setFiltroProduto(String filtroProduto) { this.filtroProduto = filtroProduto; }
    public String getFiltroTipo() { return filtroTipo; }
    public void setFiltroTipo(String filtroTipo) { this.filtroTipo = filtroTipo; }
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }
    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}