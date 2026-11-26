package br.com.dpsistemas.cedroerp.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produto_variacao_base")
public class ProdutoVariacaoBase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_medida_id", nullable = false)
    private UnidadeMedida unidadeMedida;

    // 🔥 A EMPRESA AQUI
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "largura_cm", nullable = false)
    private BigDecimal larguraCm;

    @Column(name = "espessura_cm", nullable = false)
    private BigDecimal espessuraCm;

    // Relacionamento com as Medidas (Filhos)
    @OneToMany(mappedBy = "variacaoBase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoMedida> medidas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getLarguraCm() {
        return larguraCm;
    }

    public void setLarguraCm(BigDecimal larguraCm) {
        this.larguraCm = larguraCm;
    }

    public BigDecimal getEspessuraCm() {
        return espessuraCm;
    }

    public void setEspessuraCm(BigDecimal espessuraCm) {
        this.espessuraCm = espessuraCm;
    }

    public List<ProdutoMedida> getMedidas() {
        return medidas;
    }

    public void setMedidas(List<ProdutoMedida> medidas) {
        this.medidas = medidas;
    }
}
