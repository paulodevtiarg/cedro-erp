package br.com.dpsistemas.cedroerp.repositorys;


import br.com.dpsistemas.cedroerp.models.Departamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartamentoRepository
        extends JpaRepository<Departamento, Long> {


    /*
     * Busca segura:
     * o departamento obrigatoriamente precisa
     * pertencer à empresa informada.
     */
    Optional<Departamento> findByIdAndEmpresa_Id(
            Long id,
            Long idEmpresa
    );


    /*
     * Filtros da tela index.
     *
     * nome = null ou vazio → ignora filtro
     * status = null        → todos
     */
    @Query("""
        SELECT d
        FROM Departamento d

        WHERE d.empresa.id = :idEmpresa

        AND (
            :nome IS NULL
            OR :nome = ''
            OR LOWER(d.nome)
                LIKE LOWER(CONCAT('%', :nome, '%'))
        )

        AND (
            :status IS NULL
            OR d.status = :status
        )
    """)
    Page<Departamento> filtrar(
            @Param("idEmpresa") Long idEmpresa,
            @Param("nome") String nome,
            @Param("status") Boolean status,
            Pageable pageable
    );


    /*
     * Evita departamentos duplicados
     * dentro da mesma empresa.
     */
    boolean existsByEmpresa_IdAndNomeIgnoreCase(
            Long idEmpresa,
            String nome
    );


    /*
     * Usado na alteração.
     *
     * Verifica se existe outro departamento
     * com o mesmo nome, ignorando o registro
     * que está sendo alterado.
     */
    boolean existsByEmpresa_IdAndNomeIgnoreCaseAndIdNot(
            Long idEmpresa,
            String nome,
            Long id
    );
}
