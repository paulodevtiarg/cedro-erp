package br.com.dpsistemas.cedroerp.repositorys;



import br.com.dpsistemas.cedroerp.models.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {

    /**
     * Busca todos os municípios de um determinado estado (UF) ordenados pelo nome.
     * Útil para popular o <select> de cidades via AJAX após o usuário escolher a UF.
     */
    List<Municipio> findByUfOrderByNomeAsc(String uf);

    /**
     * Busca um município pelo seu código IBGE (7 dígitos).
     * Essencial para vincular o município ao cliente após a consulta do ViaCEP.
     */
    Optional<Municipio> findByCodigoIbge(String codigoIbge);

    /**
     * Busca um município pelo nome exato e pela UF (ignorando maiúsculas e minúsculas).
     */
    Optional<Municipio> findByNomeIgnoreCaseAndUfIgnoreCase(String nome, String uf);
}
