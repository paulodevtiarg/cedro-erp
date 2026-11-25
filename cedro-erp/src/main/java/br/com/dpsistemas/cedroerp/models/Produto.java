package br.com.dpsistemas.cedroerp.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produto")
public class Produto extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_produto_id", nullable = false)
    private TipoProduto tipoProduto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especie_madeira_id", nullable = false)
    private EspecieMadeira especieMadeira;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_medida_id", nullable = false)
    private UnidadeMedida unidadeMedida;

    @Column(name = "largura_cm", nullable = false)
    private BigDecimal larguraCm;

    @Column(name = "espessura_cm", nullable = false)
    private BigDecimal espessuraCm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoMedida> medidas;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoProduto getTipoProduto() { return tipoProduto; }
    public void setTipoProduto(TipoProduto tipoProduto) { this.tipoProduto = tipoProduto; }

    public EspecieMadeira getEspecieMadeira() { return especieMadeira; }
    public void setEspecieMadeira(EspecieMadeira especieMadeira) { this.especieMadeira = especieMadeira; }

    public UnidadeMedida getUnidadeMedida() { return unidadeMedida; }
    public void setUnidadeMedida(UnidadeMedida unidadeMedida) { this.unidadeMedida = unidadeMedida; }

    public BigDecimal getLarguraCm() { return larguraCm; }
    public void setLarguraCm(BigDecimal larguraCm) { this.larguraCm = larguraCm; }

    public BigDecimal getEspessuraCm() { return espessuraCm; }
    public void setEspessuraCm(BigDecimal espessuraCm) { this.espessuraCm = espessuraCm; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public List<ProdutoMedida> getMedidas() { return medidas; }
    public void setMedidas(List<ProdutoMedida> medidas) { this.medidas = medidas; }

    @Column(length = 255)
    private String imagem;

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }
}