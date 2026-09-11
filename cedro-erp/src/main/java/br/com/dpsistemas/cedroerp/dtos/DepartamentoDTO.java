package br.com.dpsistemas.cedroerp.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DepartamentoDTO {

    private Long id;


    private Long idEmpresa;

    @NotBlank(message = "O nome do departamento é obrigatório.")
    @Size(max = 255, message = "O nome deve possuir no máximo 255 caracteres.")
    private String nome;


    private String descricao;

    private Boolean status;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAlteracao;

    private String filtroNome;
    private Integer filtroStatus = 1;
    private Integer page = 0;
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

    public String getDataAlteracaoFormatada() {
        if (dataAlteracao == null) {
            return "";
        }

        return dataAlteracao.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
    }

    public String getDataCadastroFormatada() {
        if (dataCadastro == null) {
            return "";
        }

        return dataCadastro.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
    }
}