package br.com.dpsistemas.cedroerp.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VERSAO_SISTEMA")
public class VersaoSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String versao; // Ex: "1.0.2026"

    @Column(name = "notas_versao", columnDefinition = "TEXT")
    private String notasVersao; // O changelog completo

    @Column(name = "data_versao", nullable = false)
    private LocalDateTime dataVersao;

    // Getters e Setters omitidos para brevidade

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getNotasVersao() {
        return notasVersao;
    }

    public void setNotasVersao(String notasVersao) {
        this.notasVersao = notasVersao;
    }

    public LocalDateTime getDataVersao() {
        return dataVersao;
    }

    public void setDataVersao(LocalDateTime dataVersao) {
        this.dataVersao = dataVersao;
    }
}