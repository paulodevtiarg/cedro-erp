package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoProdutoRepository extends JpaRepository<TipoProduto, Long>, JpaSpecificationExecutor<TipoProduto> {

    boolean existsByNomeIgnoreCaseAndEmpresa_Id(String nome, Long idEmpresa);

    boolean existsByNomeIgnoreCaseAndEmpresa_IdAndIdNot(String nome, Long idEmpresa, Long id);

    Optional<TipoProduto> findByIdAndEmpresa_Id(Long id, Long idEmpresa);
}