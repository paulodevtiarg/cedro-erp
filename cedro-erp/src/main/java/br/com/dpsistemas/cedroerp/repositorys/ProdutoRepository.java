package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {

    Optional<Produto> findByIdAndEmpresa_Id(Long id, Long idEmpresa);

    // Verifica se já existe um produto com a mesma combinação (Tipo + Espécie + Largura + Espessura)
    boolean existsByTipoProduto_IdAndEspecieMadeira_IdAndLarguraCmAndEspessuraCmAndEmpresa_Id(
            Long tipoProdutoId, Long especieMadeiraId, java.math.BigDecimal larguraCm, java.math.BigDecimal espessuraCm, Long idEmpresa
    );

    boolean existsByTipoProduto_IdAndEspecieMadeira_IdAndLarguraCmAndEspessuraCmAndEmpresa_IdAndIdNot(
            Long tipoProdutoId, Long especieMadeiraId, java.math.BigDecimal larguraCm, java.math.BigDecimal espessuraCm, Long idEmpresa, Long id
    );
}