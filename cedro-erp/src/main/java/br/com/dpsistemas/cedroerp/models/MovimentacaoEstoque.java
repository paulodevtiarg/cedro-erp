package br.com.dpsistemas.cedroerp.models;

import br.com.dpsistemas.cedroerp.enumerators.TipoMovimentacaoEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao_estoque")
public class MovimentacaoEstoque extends BaseEntity { // Herda o ID e a Data de Cadastro/Alteração


    // 🔥 O ID ADICIONADO AQUI
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_medida_id", nullable = false)
    private ProdutoMedida produtoMedida;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimentacao", nullable = false, length = 20)
    private TipoMovimentacaoEnum tipoMovimentacao;

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao;

    @Column(length = 255)
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    // Getters e Setters...
    public ProdutoMedida getProdutoMedida() { return produtoMedida; }
    public void setProdutoMedida(ProdutoMedida produtoMedida) { this.produtoMedida = produtoMedida; }

    public TipoMovimentacaoEnum getTipoMovimentacao() { return tipoMovimentacao; }
    public void setTipoMovimentacao(TipoMovimentacaoEnum tipoMovimentacao) { this.tipoMovimentacao = tipoMovimentacao; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public LocalDateTime getDataMovimentacao() { return dataMovimentacao; }
    public void setDataMovimentacao(LocalDateTime dataMovimentacao) { this.dataMovimentacao = dataMovimentacao; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}