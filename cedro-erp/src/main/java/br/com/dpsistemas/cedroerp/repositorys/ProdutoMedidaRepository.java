package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.ProdutoMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoMedidaRepository extends JpaRepository<ProdutoMedida, Long> {

    // Query que agrupa por Classificação (Serrada/Bruta) e Sigla da Unidade (M3, ML, UN)
    @Query("SELECT p.classificacao, u.sigla, SUM(m.quantidadeEstoque) " +
            "FROM ProdutoMedida m " +
            "JOIN m.variacaoBase vb " +
            "JOIN vb.produto p " +
            "JOIN vb.unidadeMedida u " +
            "WHERE p.empresa.id = :idEmpresa AND p.status = true AND m.status = true " +
            "GROUP BY p.classificacao, u.sigla")
    List<Object[]> somarEstoquePorClassificacaoEUnidade(@Param("idEmpresa") Long idEmpresa);

    @Query("SELECT p.tipoProduto.nome, p.especieMadeira.nome, p.classificacao, u.sigla, SUM(m.quantidadeEstoque) " +
            "FROM ProdutoMedida m " +
            "JOIN m.variacaoBase vb " +
            "JOIN vb.produto p " +
            "JOIN vb.unidadeMedida u " +
            "WHERE p.empresa.id = :idEmpresa AND p.status = true AND m.status = true " +
            "GROUP BY p.tipoProduto.nome, p.especieMadeira.nome, p.classificacao, u.sigla " +
            "ORDER BY SUM(m.quantidadeEstoque) DESC")
    List<Object[]> listarTopProdutosEstoque(@Param("idEmpresa") Long idEmpresa);

    // Busca todas as medidas ativas para o Select do Modal
    @Query("SELECT m FROM ProdutoMedida m " +
            "JOIN FETCH m.variacaoBase vb " +
            "JOIN FETCH vb.produto p " +
            "JOIN FETCH vb.unidadeMedida u " +
            "WHERE p.empresa.id = :idEmpresa AND p.status = true AND m.status = true " +
            "ORDER BY p.tipoProduto.nome, p.especieMadeira.nome, vb.larguraCm, vb.espessuraCm, m.comprimentoM")
    List<ProdutoMedida> buscarMedidasParaSelect(@Param("idEmpresa") Long idEmpresa);
}