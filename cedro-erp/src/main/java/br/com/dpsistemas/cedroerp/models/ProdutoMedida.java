package br.com.dpsistemas.cedroerp.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produto_medida")
public class ProdutoMedida extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "comprimento_m")
    private BigDecimal comprimentoM; // Pode ser nulo

    @Column(length = 50, unique = true)
    private String sku;

    @Column(name = "preco_venda", nullable = false)
    private BigDecimal precoVenda;

    @Column(name = "quantidade_estoque", nullable = false)
    private BigDecimal quantidadeEstoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public BigDecimal getComprimentoM() { return comprimentoM; }
    public void setComprimentoM(BigDecimal comprimentoM) { this.comprimentoM = comprimentoM; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

    public BigDecimal getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
}