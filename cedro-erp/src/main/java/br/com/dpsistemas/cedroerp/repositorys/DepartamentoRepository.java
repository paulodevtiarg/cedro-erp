package br.com.dpsistemas.cedroerp.repositorys;


import br.com.dpsistemas.cedroerp.models.Departamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DepartamentoRepository
        extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findByIdAndEmpresa_Id(
            Long id,
            Long idEmpresa
    );

    Page<Departamento> findByEmpresa_Id(
            Long idEmpresa,
            Pageable pageable
    );

    Page<Departamento> findByEmpresa_IdAndNomeContainingIgnoreCase(
            Long idEmpresa,
            String nome,
            Pageable pageable
    );

    Page<Departamento> findByEmpresa_IdAndStatus(
            Long idEmpresa,
            Boolean status,
            Pageable pageable
    );

    Page<Departamento> findByEmpresa_IdAndNomeContainingIgnoreCaseAndStatus(
            Long idEmpresa,
            String nome,
            Boolean status,
            Pageable pageable
    );

    boolean existsByEmpresa_IdAndNomeIgnoreCase(
            Long idEmpresa,
            String nome
    );

    boolean existsByEmpresa_IdAndNomeIgnoreCaseAndIdNot(
            Long idEmpresa,
            String nome,
            Long id
    );
}
