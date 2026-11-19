package br.com.dpsistemas.cedroerp.dtos;

public class UnidadeMedidaDTO {

    private Long id;
    private String sigla;
    private String descricao;
    private Boolean status;

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
}