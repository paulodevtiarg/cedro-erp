package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClienteRepository
        extends JpaRepository<Cliente, Long>,
        JpaSpecificationExecutor<Cliente> {

    Optional<Cliente> findByIdAndEmpresa_Id(
            Long id,
            Long idEmpresa
    );


    /*
     * ==================================================
     * CPF
     * ==================================================
     */

    boolean existsByEmpresa_IdAndCpf(
            Long idEmpresa,
            String cpf
    );

    boolean existsByEmpresa_IdAndCpfAndIdNot(
            Long idEmpresa,
            String cpf,
            Long idCliente
    );


    /*
     * ==================================================
     * CNPJ
     * ==================================================
     */

    boolean existsByEmpresa_IdAndCnpj(
            Long idEmpresa,
            String cnpj
    );

    boolean existsByEmpresa_IdAndCnpjAndIdNot(
            Long idEmpresa,
            String cnpj,
            Long idCliente
    );
}