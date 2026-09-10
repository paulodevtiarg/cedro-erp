package br.com.dpsistemas.cedroerp.services;


import br.com.dpsistemas.cedroerp.dtos.DepartamentoDTO;
import br.com.dpsistemas.cedroerp.mappers.DepartamentoMapper;
import br.com.dpsistemas.cedroerp.models.Departamento;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.repositorys.DepartamentoRepository;

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


    /*
     * Valores permitidos no select
     * de quantidade por página.
     */
    private static final Set<Integer> TAMANHOS_PERMITIDOS =
            Set.of(5, 10, 20, 30);


    public DepartamentoService(
            DepartamentoRepository departamentoRepository,
            DepartamentoMapper departamentoMapper,
            EmpresaService empresaService) {

        this.departamentoRepository =
                departamentoRepository;

        this.departamentoMapper =
                departamentoMapper;

        this.empresaService =
                empresaService;
    }


    /*
     * ======================================
     * LISTAGEM COM FILTROS E PAGINAÇÃO
     * ======================================
     */

    @Transactional(readOnly = true)
    public Page<DepartamentoDTO> listar(
            DepartamentoDTO filtro,
            Long idEmpresa) {

        if (idEmpresa == null) {
            throw new IllegalArgumentException(
                    "Empresa não informada."
            );
        }

        if (filtro == null) {
            filtro = new DepartamentoDTO();
        }

        String nome = normalizarFiltroNome(
                filtro.getFiltroNome()
        );

        Boolean status = converterStatus(
                filtro.getFiltroStatus()
        );

        int page = normalizarPagina(
                filtro.getPage()
        );

        int size = normalizarTamanhoPagina(
                filtro.getSize()
        );


        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Direction.ASC,
                        "nome"
                )
        );


        Page<Departamento> departamentos =
                departamentoRepository.filtrar(
                        idEmpresa,
                        nome,
                        status,
                        pageable
                );


        return departamentos.map(
                departamentoMapper::toDTO
        );
    }


    /*
     * ======================================
     * BUSCAR POR ID
     * ======================================
     */

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


    /*
     * ======================================
     * BUSCAR ENTIDADE
     * ======================================
     */

    @Transactional(readOnly = true)
    public Departamento buscarEntidadePorId(
            Long id,
            Long idEmpresa) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID do departamento não informado."
            );
        }

        if (idEmpresa == null) {
            throw new IllegalArgumentException(
                    "Empresa não informada."
            );
        }


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

        String nome =
                normalizarNome(dto.getNome());

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


        String nome =
                normalizarNome(dto.getNome());

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

        departamento.setStatus(status);

        departamentoRepository.save(
                departamento
        );
    }


    /*
     * ======================================
     * MÉTODOS AUXILIARES
     * ======================================
     */

    private void validarEmpresa(Long idEmpresa) {

        if (idEmpresa == null) {

            throw new IllegalArgumentException(
                    "Empresa não informada."
            );
        }
    }


    private String normalizarNome(
            String nome) {

        if (nome == null ||
                nome.isBlank()) {

            throw new IllegalArgumentException(
                    "O nome do departamento é obrigatório."
            );
        }

        return nome.trim();
    }


    private String normalizarFiltroNome(
            String nome) {

        if (nome == null ||
                nome.isBlank()) {

            return null;
        }

        return nome.trim();
    }


    /*
     * null → todos
     * 0    → false
     * 1    → true
     */
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


    private int normalizarPagina(
            Integer page) {

        if (page == null ||
                page < 0) {

            return 0;
        }

        return page;
    }


    private int normalizarTamanhoPagina(
            Integer size) {

        if (
                size == null ||
                        !TAMANHOS_PERMITIDOS.contains(size)
        ) {

            return 10;
        }

        return size;
    }
}