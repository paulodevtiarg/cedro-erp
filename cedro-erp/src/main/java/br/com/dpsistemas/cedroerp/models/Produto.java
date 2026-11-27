package br.com.dpsistemas.cedroerp.models;

import br.com.dpsistemas.cedroerp.enumerators.ClassificacaoProdutoEnum;
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

    // 🔥 A EMPRESA AQUI
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(length = 255)
    private String imagem;

    // Relacionamento com as Bases (Mães)
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoVariacaoBase> variacoesBase;

    @Enumerated(EnumType.STRING)
    @Column(name = "classificacao", nullable = false, length = 30)
    private ClassificacaoProdutoEnum classificacao;

    public ClassificacaoProdutoEnum getClassificacao() { return classificacao; }
    public void setClassificacao(ClassificacaoProdutoEnum classificacao) { this.classificacao = classificacao; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoProduto getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProduto tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    public EspecieMadeira getEspecieMadeira() {
        return especieMadeira;
    }

    public void setEspecieMadeira(EspecieMadeira especieMadeira) {
        this.especieMadeira = especieMadeira;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public List<ProdutoVariacaoBase> getVariacoesBase() {
        return variacoesBase;
    }

    public void setVariacoesBase(List<ProdutoVariacaoBase> variacoesBase) {
        this.variacoesBase = variacoesBase;
    }
}