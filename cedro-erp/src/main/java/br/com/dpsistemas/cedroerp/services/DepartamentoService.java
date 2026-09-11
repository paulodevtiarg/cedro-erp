package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.DepartamentoDTO;
import br.com.dpsistemas.cedroerp.mappers.DepartamentoMapper;
import br.com.dpsistemas.cedroerp.models.Departamento;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.repositorys.DepartamentoRepository;
import java.util.Locale;

import br.com.dpsistemas.cedroerp.repositorys.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    private final DepartamentoMapper departamentoMapper;

    private final EmpresaService empresaService;
    private final UsuarioRepository usuarioRepository;

    /*
     * Valores permitidos no select
     * de quantidade por página.
     */
    private static final Set<Integer> TAMANHOS_PERMITIDOS =
            Set.of(5, 10, 20, 30);


    public DepartamentoService(
            DepartamentoRepository departamentoRepository,
            DepartamentoMapper departamentoMapper,
            EmpresaService empresaService,
            UsuarioRepository usuarioRepository) {

        this.departamentoRepository =  departamentoRepository;
        this.departamentoMapper = departamentoMapper;
        this.empresaService = empresaService;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public boolean departamentoEstaEmUso(
            Long idDepartamento,
            Long idEmpresa) {

        buscarEntidadePorId(
                idDepartamento,
                idEmpresa
        );

        return usuarioRepository
                .existsByDepartamento_IdAndEmpresa_Id(
                        idDepartamento,
                        idEmpresa
                );
    }

    @Transactional(readOnly = true)
    public Page<DepartamentoDTO> listar(
            DepartamentoDTO filtro,
            Long idEmpresa) {

        validarEmpresa(idEmpresa);

        if (filtro == null) {
            filtro = new DepartamentoDTO();
        }

        String nome =  filtro.getFiltroNome();
        Boolean status = converterStatus(filtro.getFiltroStatus());
        int page = normalizarPagina(filtro.getPage());
        int size = normalizarTamanhoPagina(filtro.getSize());

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.ASC,
                                "nome"
                        )
                );

        Page<Departamento> departamentos =
                buscarDepartamentos(
                        idEmpresa,
                        nome,
                        status,
                        pageable
                );


        return departamentos.map(
                departamentoMapper::toDTO
        );
    }

    @Transactional(readOnly = true)
    public DepartamentoDTO buscarPorId(
            Long id,
            Long idEmpresa) {

        Departamento departamento =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );


        return departamentoMapper.toDTO(
                departamento
        );
    }

    @Transactional(readOnly = true)
    public Departamento buscarEntidadePorId(
            Long id,
            Long idEmpresa) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID do departamento não informado."
            );
        }


        validarEmpresa(idEmpresa);


        return departamentoRepository
                .findByIdAndEmpresa_Id(
                        id,
                        idEmpresa
                )
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Departamento não encontrado."
                        )
                );
    }


    /*
     * ======================================
     * CADASTRAR
     * ======================================
     */

    @Transactional
    public DepartamentoDTO salvar(
            DepartamentoDTO dto,
            Long idEmpresa) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Dados do departamento não informados."
            );
        }


        validarEmpresa(idEmpresa);


        String nome = dto.getNome();


        dto.setNome(nome);


        /*
         * Não permite nomes duplicados
         * dentro da mesma empresa.
         */
        if (
                departamentoRepository
                        .existsByEmpresa_IdAndNomeIgnoreCase(
                                idEmpresa,
                                nome
                        )
        ) {

            throw new IllegalArgumentException(
                    "Já existe um departamento com o nome informado."
            );
        }


        Empresa empresa =
                empresaService.buscarPorId(
                        idEmpresa
                );


        Departamento departamento =
                departamentoMapper.toEntity(
                        dto,
                        empresa
                );


        Departamento salvo =
                departamentoRepository.save(
                        departamento
                );


        return departamentoMapper.toDTO(
                salvo
        );
    }


    /*
     * ======================================
     * ALTERAR
     * ======================================
     */

    @Transactional
    public DepartamentoDTO atualizar(
            Long id,
            DepartamentoDTO dto,
            Long idEmpresa) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Dados do departamento não informados."
            );
        }


        Departamento departamento =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        String nome =  dto.getNome();
        dto.setNome(nome);
        /*
         * Verifica duplicidade,
         * ignorando o próprio registro.
         */
        if (
                departamentoRepository
                        .existsByEmpresa_IdAndNomeIgnoreCaseAndIdNot(
                                idEmpresa,
                                nome,
                                id
                        )
        ) {

            throw new IllegalArgumentException(
                    "Já existe outro departamento com o nome informado."
            );
        }


        Empresa empresa =
                empresaService.buscarPorId(
                        idEmpresa
                );


        departamentoMapper.updateEntity(
                dto,
                departamento,
                empresa
        );


        Departamento atualizado =
                departamentoRepository.save(
                        departamento
                );


        return departamentoMapper.toDTO(
                atualizado
        );
    }


    /*
     * ======================================
     * ATIVAR
     * ======================================
     */

    @Transactional
    public void ativar(
            Long id,
            Long idEmpresa) {

        Departamento departamento =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );


        departamento.ativar();


        departamentoRepository.save(
                departamento
        );
    }


    /*
     * ======================================
     * INATIVAR
     * ======================================
     */

    @Transactional
    public void inativar(
            Long id,
            Long idEmpresa) {

        Departamento departamento =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );


        departamento.inativar();


        departamentoRepository.save(
                departamento
        );
    }


    /*
     * ======================================
     * ALTERAR STATUS
     * ======================================
     */

    @Transactional
    public void alterarStatus(
            Long id,
            Long idEmpresa,
            Boolean status) {

        if (status == null) {

            throw new IllegalArgumentException(
                    "Status não informado."
            );
        }


        Departamento departamento =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );


        departamento.setStatus(
                status
        );


        departamentoRepository.save(
                departamento
        );
    }


    /*
     * ======================================
     * BUSCAR DEPARTAMENTOS
     * ======================================
     *
     * Escolhe automaticamente o método
     * do repository de acordo com os
     * filtros informados.
     *
     * Não utiliza @Query.
     * ======================================
     */

    private Page<Departamento> buscarDepartamentos(
            Long idEmpresa,
            String nome,
            Boolean status,
            Pageable pageable) {

        /*
         * Nome + Status
         */
        if (nome != null && status != null) {

            return departamentoRepository
                    .findByEmpresa_IdAndNomeContainingIgnoreCaseAndStatus(
                            idEmpresa,
                            nome,
                            status,
                            pageable
                    );
        }

        /*
         * Somente Nome
         * Status null = TODOS
         */
        if (nome != null) {

            return departamentoRepository
                    .findByEmpresa_IdAndNomeContainingIgnoreCase(
                            idEmpresa,
                            nome,
                            pageable
                    );
        }

        /*
         * Somente Status
         */
        if (status != null) {

            return departamentoRepository
                    .findByEmpresa_IdAndStatus(
                            idEmpresa,
                            status,
                            pageable
                    );
        }

        /*
         * Sem nome e sem status = TODOS
         */
        return departamentoRepository
                .findByEmpresa_Id(
                        idEmpresa,
                        pageable
                );
    }


    /*
     * ======================================
     * MÉTODOS AUXILIARES
     * ======================================
     */

    private void validarEmpresa(
            Long idEmpresa) {

        if (idEmpresa == null) {

            throw new IllegalArgumentException(
                    "Empresa não informada."
            );
        }
    }



    @Transactional
    public void excluir(
            Long id,
            Long idEmpresa) {
        Departamento departamento = buscarEntidadePorId( id, idEmpresa );

        boolean emUso = usuarioRepository.existsByDepartamento_IdAndEmpresa_Id(id,idEmpresa);

        if (emUso) {

            throw new IllegalArgumentException(
                    "Este departamento está vinculado a usuários e não pode ser excluído."
            );
        }

        departamentoRepository.delete(departamento);
    }
    private int normalizarPagina(
            Integer page) {

        if (
                page == null ||
                        page < 0
        ) {

            return 0;
        }


        return page;
    }

    private Boolean converterStatus(
            Integer status) {

        if (status == null) {
            return null;
        }

        if (status == 1) {
            return true;
        }

        if (status == 0) {
            return false;
        }

        return null;
    }


    private int normalizarTamanhoPagina(
            Integer size) {

        if (
                size == null ||
                        !TAMANHOS_PERMITIDOS.contains(
                                size
                        )
        ) {

            return 10;
        }


        return size;
    }
}