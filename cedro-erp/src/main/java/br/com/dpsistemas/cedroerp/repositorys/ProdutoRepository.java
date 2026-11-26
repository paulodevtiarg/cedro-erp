package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {

    Optional<Produto> findByIdAndEmpresa_Id(Long id, Long idEmpresa);

    boolean existsByTipoProduto_IdAndEspecieMadeira_IdAndEmpresa_Id(
            Long tipoProdutoId, Long especieMadeiraId, Long idEmpresa);

    boolean existsByTipoProduto_IdAndEspecieMadeira_IdAndEmpresa_IdAndIdNot(
            Long tipoProdutoId, Long especieMadeiraId, Long idEmpresa, Long id);
}