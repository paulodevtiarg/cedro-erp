package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.MovimentacaoEstoque;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long>, JpaSpecificationExecutor<MovimentacaoEstoque> {



    // Busca o histórico de um produto específico
    Page<MovimentacaoEstoque> findByProdutoMedidaIdAndEmpresa_IdOrderByDataMovimentacaoDesc(Long idProdutoMedida, Long idEmpresa, Pageable pageable);

    @Query("SELECT m FROM MovimentacaoEstoque m " +
            "WHERE m.empresa.id = :idEmpresa " +
            "AND (:dataInicio IS NULL OR CAST(m.dataMovimentacao AS date) >= :dataInicio) " +
            "AND (:dataFim IS NULL OR CAST(m.dataMovimentacao AS date) <= :dataFim) " +
            "ORDER BY m.dataMovimentacao DESC")
    Page<MovimentacaoEstoque> filtrarPorData(
            @Param("idEmpresa") Long idEmpresa,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    // Sem filtro
    Page<MovimentacaoEstoque> findByEmpresa_IdOrderByDataMovimentacaoDesc(Long idEmpresa, Pageable pageable);

    // Só Data Início
    Page<MovimentacaoEstoque> findByEmpresa_IdAndDataMovimentacaoGreaterThanEqualOrderByDataMovimentacaoDesc(
            Long idEmpresa, LocalDateTime dataInicio, Pageable pageable);

    // Só Data Fim
    Page<MovimentacaoEstoque> findByEmpresa_IdAndDataMovimentacaoLessThanEqualOrderByDataMovimentacaoDesc(
            Long idEmpresa, LocalDateTime dataFim, Pageable pageable);

    // Data Início e Data Fim
    Page<MovimentacaoEstoque> findByEmpresa_IdAndDataMovimentacaoBetweenOrderByDataMovimentacaoDesc(
            Long idEmpresa, LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);
}