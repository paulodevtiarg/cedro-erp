package br.com.dpsistemas.cedroerp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EspecieMadeiraDTO {

    // ==================================================
    // CAMPOS PRINCIPAIS
    // ==================================================
    private Long id;

    @NotBlank(message = "O nome da espécie é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    private Boolean status = true;

    // ==================================================
    // CAMPOS DE AUDITORIA
    // ==================================================
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ==================================================
    // FILTROS E PAGINAÇÃO
    // ==================================================
    private String filtroNome;
    private Integer filtroStatus = 1;
    private Integer page = 0;
    private Integer size = 10;

    // ==================================================
    // MÉTODOS FORMATADOS
    // ==================================================
    public String getDataCadastroFormatada() {
        if (dataCadastro == null) return "";
        return dataCadastro.format(FORMATO_DATA_HORA);
    }

    public String getDataAlteracaoFormatada() {
        if (dataAlteracao == null) return "";
        return dataAlteracao.format(FORMATO_DATA_HORA);
    }

    // ==================================================
    // GETTERS E SETTERS
    // ==================================================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome != null ? nome.trim() : null;
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
        this.filtroNome = filtroNome != null ? filtroNome.trim() : null;
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
        this.page = (page == null || page < 0) ? 0 : page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = (size == null || size <= 0) ? 10 : size;
    }
}