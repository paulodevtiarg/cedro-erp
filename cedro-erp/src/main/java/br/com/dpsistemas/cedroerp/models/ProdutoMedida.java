package br.com.dpsistemas.cedroerp.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto_medida")
public class ProdutoMedida extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Agora a Medida aponta para a Variação Base, e não mais para o Produto direto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_variacao_base_id", nullable = false)
    private ProdutoVariacaoBase variacaoBase;

    // 🔥 A EMPRESA AQUI
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Column(name = "comprimento_m")
    private BigDecimal comprimentoM; // Nulo se for venda em m³

    @Column(name = "preco_venda", nullable = false)
    private BigDecimal precoVenda;

    @Column(name = "quantidade_estoque", nullable = false)
    private BigDecimal quantidadeEstoque;

    @Column(length = 50, unique = true)
    private String sku;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdutoVariacaoBase getVariacaoBase() {
        return variacaoBase;
    }

    public void setVariacaoBase(ProdutoVariacaoBase variacaoBase) {
        this.variacaoBase = variacaoBase;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public BigDecimal getComprimentoM() {
        return comprimentoM;
    }

    public void setComprimentoM(BigDecimal comprimentoM) {
        this.comprimentoM = comprimentoM;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public BigDecimal getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}