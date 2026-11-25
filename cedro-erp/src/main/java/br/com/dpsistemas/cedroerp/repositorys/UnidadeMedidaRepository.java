package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.UnidadeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Long>, JpaSpecificationExecutor<UnidadeMedida> {

    boolean existsBySiglaIgnoreCaseAndEmpresa_Id(String sigla, Long idEmpresa);

    boolean existsBySiglaIgnoreCaseAndEmpresa_IdAndIdNot(String sigla, Long idEmpresa, Long id);

    Optional<UnidadeMedida> findByIdAndEmpresa_Id(Long id, Long idEmpresa);
}