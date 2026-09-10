package br.com.dpsistemas.cedroerp.repositorys;

import br.com.dpsistemas.cedroerp.models.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {


    /*
     * ==================================================
     * BUSCA SEGURA POR EMPRESA
     * ==================================================
     */

    @EntityGraph(attributePaths = {
            "empresa",
            "departamento"
    })
    Optional<Usuario> findByIdAndEmpresa_Id(
            Long id,
            Long idEmpresa
    );


    /*
     * ==================================================
     * INDEX - FILTROS
     * ==================================================
     *
     * nome   = null → todos
     * cpf    = null → todos
     * status = null → todos
     */

    @EntityGraph(attributePaths = {
            "empresa",
            "departamento"
    })
    @Query("""
        SELECT u
        FROM Usuario u

        WHERE u.empresa.id = :idEmpresa

        AND (
            :nome IS NULL
            OR :nome = ''
            OR LOWER(u.nome)
                LIKE LOWER(CONCAT('%', :nome, '%'))
        )

        AND (
            :cpf IS NULL
            OR :cpf = ''
            OR u.cpf LIKE CONCAT('%', :cpf, '%')
        )

        AND (
            :status IS NULL
            OR u.status = :status
        )
    """)
    Page<Usuario> filtrar(
            @Param("idEmpresa") Long idEmpresa,
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("status") Boolean status,
            Pageable pageable
    );


    /*
     * ==================================================
     * AUTENTICAÇÃO
     * ==================================================
     *
     * Como sua tela permite entrar por:
     *
     * Login
     * E-mail
     * CPF
     */

    @EntityGraph(attributePaths = {
            "empresa",
            "departamento"
    })
    Optional<Usuario> findByLoginIgnoreCase(
            String login
    );


    @EntityGraph(attributePaths = {
            "empresa",
            "departamento"
    })
    Optional<Usuario> findByEmailIgnoreCase(
            String email
    );


    @EntityGraph(attributePaths = {
            "empresa",
            "departamento"
    })
    Optional<Usuario> findByCpf(
            String cpf
    );


    /*
     * ==================================================
     * DUPLICIDADE - CADASTRO
     * ==================================================
     */

    boolean existsByLoginIgnoreCase(
            String login
    );

    boolean existsByEmailIgnoreCase(
            String email
    );

    boolean existsByCpf(
            String cpf
    );


    /*
     * ==================================================
     * DUPLICIDADE - ALTERAÇÃO
     * ==================================================
     *
     * Ignora o próprio usuário sendo alterado.
     */

    boolean existsByLoginIgnoreCaseAndIdNot(
            String login,
            Long id
    );

    boolean existsByEmailIgnoreCaseAndIdNot(
            String email,
            Long id
    );

    boolean existsByCpfAndIdNot(
            String cpf,
            Long id
    );
}