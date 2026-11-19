package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.EspecieMadeira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EspecieMadeiraRepository extends JpaRepository<EspecieMadeira, Long>, JpaSpecificationExecutor<EspecieMadeira> {

    // Verifica se já existe uma espécie com esse nome na mesma empresa (para validação)
    boolean existsByNomeIgnoreCaseAndEmpresa_Id(String nome, Long idEmpresa);

    // Verifica na edição (ignorando o próprio ID)
    boolean existsByNomeIgnoreCaseAndEmpresa_IdAndIdNot(String nome, Long idEmpresa, Long id);

    Optional<EspecieMadeira> findByIdAndEmpresa_Id(Long id, Long idEmpresa);


}