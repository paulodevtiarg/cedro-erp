package br.com.dpsistemas.cedroerp.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UnidadeMedidaDTO {

    private Long id;
    private String sigla;
    private String descricao;
    private Boolean status = true;

    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;

    // FILTROS
    private String filtroSigla;
    private String filtroDescricao;
    private Integer filtroStatus = 1;
    private Integer page = 0;
    private Integer size = 10;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSigla() { return sigla; }
    public void setSigla(String sigla) { this.sigla = sigla; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getFiltroSigla() { return filtroSigla; }
    public void setFiltroSigla(String filtroSigla) { this.filtroSigla = filtroSigla; }

    public String getFiltroDescricao() { return filtroDescricao; }
    public void setFiltroDescricao(String filtroDescricao) { this.filtroDescricao = filtroDescricao; }

    public Integer getFiltroStatus() { return filtroStatus; }
    public void setFiltroStatus(Integer filtroStatus) { this.filtroStatus = filtroStatus; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getDataCadastroFormatada() {
        if (dataCadastro == null) return "";
        return dataCadastro.format(FORMATO_DATA_HORA);
    }

    public String getDataAlteracaoFormatada() {
        if (dataAlteracao == null) return "";
        return dataAlteracao.format(FORMATO_DATA_HORA);
    }

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
}