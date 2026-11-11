package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.VersaoSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VersaoSistemaRepository extends JpaRepository<VersaoSistema, Long> {

    // Busca a versão mais recente pela data
    Optional<VersaoSistema> findTopByOrderByDataVersaoDesc();
}