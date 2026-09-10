package br.com.dpsistemas.cedroerp.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class DepartamentoDTO {

    private Long id;

    @NotNull(message = "A empresa é obrigatória.")
    private Long idEmpresa;

    @NotBlank(message = "O nome do departamento é obrigatório.")
    @Size(max = 255, message = "O nome deve possuir no máximo 255 caracteres.")
    private String nome;

    @NotBlank(message = "A descrição é obrigatória.")
    @Size(max = 255, message = "A descrição deve possuir no máximo 255 caracteres.")
    private String descricao;

    private Boolean status;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAlteracao;

    private String filtroNome;

    /*
     * null = Todos
     * 0    = Inativos
     * 1    = Ativos
     */
    private Integer filtroStatus;

    /*
     * Página atual.
     * Spring Data começa em zero.
     */
    private Integer page = 0;

    /*
     * Quantidade de registros:
     * 5, 10, 20 ou 30
     */
    private Integer size = 10;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

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

    public String getFiltroNome() {
        return filtroNome;
    }

    public void setFiltroNome(String filtroNome) {
        this.filtroNome = filtroNome;
    }

    public Integer getFiltroStatus() {
        return filtroStatus;
    }

    public void setFiltroStatus(Integer filtroStatus) {
        this.filtroStatus = filtroStatus;
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