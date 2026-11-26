package br.com.dpsistemas.cedroerp.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDTO {

    private Long id;

    // Relacionamentos Principais
    private Long tipoProdutoId;
    private Long especieMadeiraId;

    // Dados achatados para visualização
    private String tipoProdutoNome;
    private String especieMadeiraNome;

    private Boolean status = true;
    private String imagem;

    // 🔥 A LISTA DE VARIAÇÕES BASE (As Mães)
    private List<ProdutoVariacaoBaseDTO> variacoesBase = new ArrayList<>();

    // Auditoria
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;

    // Filtros
    private String filtroEspecie;
    private String filtroTipo;
    private Integer filtroStatus = 1;
    private Integer page = 0;
    private Integer size = 10;

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTipoProdutoId() { return tipoProdutoId; }
    public void setTipoProdutoId(Long tipoProdutoId) { this.tipoProdutoId = tipoProdutoId; }

    public Long getEspecieMadeiraId() { return especieMadeiraId; }
    public void setEspecieMadeiraId(Long especieMadeiraId) { this.especieMadeiraId = especieMadeiraId; }

    public String getTipoProdutoNome() { return tipoProdutoNome; }
    public void setTipoProdutoNome(String tipoProdutoNome) { this.tipoProdutoNome = tipoProdutoNome; }

    public String getEspecieMadeiraNome() { return especieMadeiraNome; }
    public void setEspecieMadeiraNome(String especieMadeiraNome) { this.especieMadeiraNome = especieMadeiraNome; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public List<ProdutoVariacaoBaseDTO> getVariacoesBase() { return variacoesBase; }
    public void setVariacoesBase(List<ProdutoVariacaoBaseDTO> variacoesBase) { this.variacoesBase = variacoesBase; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public LocalDateTime getDataAlteracao() { return dataAlteracao; }
    public void setDataAlteracao(LocalDateTime dataAlteracao) { this.dataAlteracao = dataAlteracao; }

    public String getFiltroEspecie() { return filtroEspecie; }
    public void setFiltroEspecie(String filtroEspecie) { this.filtroEspecie = filtroEspecie; }

    public String getFiltroTipo() { return filtroTipo; }
    public void setFiltroTipo(String filtroTipo) { this.filtroTipo = filtroTipo; }

    public Integer getFiltroStatus() { return filtroStatus; }
    public void setFiltroStatus(Integer filtroStatus) { this.filtroStatus = filtroStatus; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = (page == null || page < 0) ? 0 : page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = (size == null || size <= 0) ? 10 : size; }

    public String getDataCadastroFormatada() {
        return dataCadastro == null ? "" : dataCadastro.format(FORMATO_DATA_HORA);
    }

    public String getDataAlteracaoFormatada() {
        return dataAlteracao == null ? "" : dataAlteracao.format(FORMATO_DATA_HORA);
    }
}