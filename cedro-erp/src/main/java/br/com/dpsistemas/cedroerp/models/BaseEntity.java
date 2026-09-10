package br.com.dpsistemas.cedroerp.models;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "STATUS", nullable = false)
    private Boolean status;

    @Column(name = "DATA_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "DATA_ALTERACAO")
    private LocalDateTime dataAlteracao;

    @PrePersist
    protected void prePersist() {

        LocalDateTime agora = LocalDateTime.now();

        this.dataCadastro = agora;

        if (this.status == null) {
            this.status = true;
        }
    }
    public boolean isAtivo() {
        return Boolean.TRUE.equals(status);
    }

    public void ativar() {
        this.status = true;
    }

    public void inativar() {
        this.status = false;
    }
    @PreUpdate
    protected void preUpdate() {
        this.dataAlteracao = LocalDateTime.now();
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

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }
}