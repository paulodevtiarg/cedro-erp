package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.ClienteDTO;
import br.com.dpsistemas.cedroerp.dtos.CnaeDTO;
import br.com.dpsistemas.cedroerp.mappers.ClienteMapper;
import br.com.dpsistemas.cedroerp.models.Cliente;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.repositorys.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import br.com.dpsistemas.cedroerp.specifications.ClienteSpecification;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final EmpresaService empresaService;
    private final CnaeService cnaeService;

    private static final Set<Integer> TAMANHOS_PERMITIDOS =
            Set.of(5, 10, 20, 30);


    public ClienteService(
            ClienteRepository clienteRepository,
            ClienteMapper clienteMapper,
            EmpresaService empresaService,
            CnaeService cnaeService) {

        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.empresaService = empresaService;
        this.cnaeService = cnaeService;
    }

    private void preencherDadosCnae(
            ClienteDTO dto) {

        if (dto == null ||
                dto.getCnae() == null ||
                dto.getCnae().isBlank()) {

            return;
        }

        CnaeDTO cnae =
                cnaeService.buscarPorCodigo(
                        dto.getCnae()
                );

        if (cnae != null) {

            dto.setCnaeDescricao(
                    cnae.getDescricao() != null
                            ? cnae.getDescricao().toUpperCase()
                            : null
            );

            dto.setCnaeDivisao(
                    cnae.getDivisao() != null
                            ? cnae.getDivisao().toUpperCase()
                            : null
            );
        }
    }
    /*
     * ==================================================
     * LISTAR / FILTRAR
     * ==================================================
     */

    @Transactional(readOnly = true)
    public Page<ClienteDTO> listar(
            ClienteDTO filtro,
            Long idEmpresa) {

        validarEmpresa(idEmpresa);

        if (filtro == null) {
            filtro = new ClienteDTO();
        }

        String nome =
                normalizarFiltro(
                        filtro.getFiltroNome()
                );

        String cpf =
                normalizarDocumento(
                        filtro.getFiltroCpf()
                );

        String cnpj =
                normalizarDocumento(
                        filtro.getFiltroCnpj()
                );

        String razaoSocial =
                normalizarFiltro(
                        filtro.getFiltroRazaoSocial()
                );

        String cidade =
                normalizarFiltro(
                        filtro.getFiltroCidade()
                );

        Boolean status =
                converterStatus(
                        filtro.getFiltroStatus()
                );

        int page =
                normalizarPagina(
                        filtro.getPage()
                );

        int size =
                normalizarTamanhoPagina(
                        filtro.getSize()
                );


        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Direction.ASC,
                                "nome"
                        )
                );


        Specification<Cliente> spec =
                ClienteSpecification
                        .empresaIgual(idEmpresa)

                        .and(
                                ClienteSpecification
                                        .nomeContem(nome)
                        )

                        .and(
                                ClienteSpecification
                                        .cpfContem(cpf)
                        )

                        .and(
                                ClienteSpecification
                                        .cnpjContem(cnpj)
                        )

                        .and(
                                ClienteSpecification
                                        .razaoSocialContem(
                                                razaoSocial
                                        )
                        )

                        .and(
                                ClienteSpecification
                                        .cidadeContem(
                                                cidade
                                        )
                        )

                        .and(
                                ClienteSpecification
                                        .statusIgual(
                                                status
                                        )
                        );


        return clienteRepository
                .findAll(
                        spec,
                        pageable
                )
                .map(
                        clienteMapper::toDTO
                );
    }


    /*
     * ==================================================
     * BUSCAR DTO POR ID
     * ==================================================
     */

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(
            Long id,
            Long idEmpresa) {
        Cliente cliente = buscarEntidadePorId(id, idEmpresa);
        ClienteDTO dto =  clienteMapper.toDTO(cliente);
        preencherDadosCnae(dto);
        return dto;
    }


    /*
     * ==================================================
     * BUSCAR ENTITY POR ID
     * ==================================================
     */

    @Transactional(readOnly = true)
    public Cliente buscarEntidadePorId(
            Long id,
            Long idEmpresa) {

        if (id == null) {

            throw new IllegalArgumentException(
                    "ID do cliente não informado."
            );
        }

        validarEmpresa(idEmpresa);

        return clienteRepository
                .findByIdAndEmpresa_Id(
                        id,
                        idEmpresa
                )
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Cliente não encontrado."
                        )
                );
    }


    /*
     * ==================================================
     * SALVAR
     * ==================================================
     */

    @Transactional
    public ClienteDTO salvar(
            ClienteDTO dto,
            Long idEmpresa) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Dados do cliente não informados."
            );
        }

        validarEmpresa(idEmpresa);

        normalizarDados(dto);

        validarDuplicidadeCadastro(
                dto,
                idEmpresa
        );

        Empresa empresa =
                empresaService.buscarPorId(
                        idEmpresa
                );

        Cliente cliente =
                clienteMapper.toEntity(
                        dto,
                        empresa
                );

        Cliente salvo =
                clienteRepository.save(
                        cliente
                );

        return clienteMapper.toDTO(
                salvo
        );
    }


    /*
     * ==================================================
     * ATUALIZAR
     * ==================================================
     */

    @Transactional
    public ClienteDTO atualizar(
            Long id,
            ClienteDTO dto,
            Long idEmpresa) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Dados do cliente não informados."
            );
        }

        Cliente cliente =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        normalizarDados(dto);

        validarDuplicidadeAlteracao(
                dto,
                idEmpresa,
                id
        );

        Empresa empresa =
                empresaService.buscarPorId(
                        idEmpresa
                );

        clienteMapper.updateEntity(
                dto,
                cliente,
                empresa
        );

        Cliente atualizado =
                clienteRepository.save(
                        cliente
                );

        return clienteMapper.toDTO(
                atualizado
        );
    }


    /*
     * ==================================================
     * EXCLUIR
     * ==================================================
     */

    @Transactional
    public void excluir(
            Long id,
            Long idEmpresa) {

        Cliente cliente =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        clienteRepository.delete(
                cliente
        );
    }


    /*
     * ==================================================
     * ATIVAR
     * ==================================================
     */

    @Transactional
    public void ativar(
            Long id,
            Long idEmpresa) {

        Cliente cliente =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        cliente.setStatus(true);

        clienteRepository.save(
                cliente
        );
    }


    /*
     * ==================================================
     * INATIVAR
     * ==================================================
     */

    @Transactional
    public void inativar(
            Long id,
            Long idEmpresa) {

        Cliente cliente =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        cliente.setStatus(false);

        clienteRepository.save(
                cliente
        );
    }


    /*
     * ==================================================
     * ALTERAR STATUS
     * ==================================================
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

        Cliente cliente =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        cliente.setStatus(
                status
        );

        clienteRepository.save(
                cliente
        );
    }


    /*
     * ==================================================
     * CPF - VERIFICAÇÕES
     * ==================================================
     */

    @Transactional(readOnly = true)
    public boolean existeCpf(
            String cpf,
            Long idEmpresa) {

        cpf = normalizarDocumento(cpf);

        if (cpf == null) {
            return false;
        }

        return clienteRepository
                .existsByEmpresa_IdAndCpf(
                        idEmpresa,
                        cpf
                );
    }


    @Transactional(readOnly = true)
    public boolean existeCpfOutroCliente(
            String cpf,
            Long idEmpresa,
            Long idCliente) {

        cpf = normalizarDocumento(cpf);

        if (cpf == null) {
            return false;
        }

        return clienteRepository
                .existsByEmpresa_IdAndCpfAndIdNot(
                        idEmpresa,
                        cpf,
                        idCliente
                );
    }


    /*
     * ==================================================
     * CNPJ - VERIFICAÇÕES
     * ==================================================
     */

    @Transactional(readOnly = true)
    public boolean existeCnpj(
            String cnpj,
            Long idEmpresa) {

        cnpj = normalizarDocumento(cnpj);

        if (cnpj == null) {
            return false;
        }

        return clienteRepository
                .existsByEmpresa_IdAndCnpj(
                        idEmpresa,
                        cnpj
                );
    }


    @Transactional(readOnly = true)
    public boolean existeCnpjOutroCliente(
            String cnpj,
            Long idEmpresa,
            Long idCliente) {

        cnpj = normalizarDocumento(cnpj);

        if (cnpj == null) {
            return false;
        }

        return clienteRepository
                .existsByEmpresa_IdAndCnpjAndIdNot(
                        idEmpresa,
                        cnpj,
                        idCliente
                );
    }


    /*
     * ==================================================
     * VALIDA DUPLICIDADE - CADASTRO
     * ==================================================
     */

    private void validarDuplicidadeCadastro(
            ClienteDTO dto,
            Long idEmpresa) {

        String cpf =
                normalizarDocumento(
                        dto.getCpf()
                );

        String cnpj =
                normalizarDocumento(
                        dto.getCnpj()
                );


        if (cpf != null &&
                clienteRepository
                        .existsByEmpresa_IdAndCpf(
                                idEmpresa,
                                cpf
                        )) {

            throw new IllegalArgumentException(
                    "Já existe um cliente com esse CPF nesta empresa."
            );
        }


        if (cnpj != null &&
                clienteRepository
                        .existsByEmpresa_IdAndCnpj(
                                idEmpresa,
                                cnpj
                        )) {

            throw new IllegalArgumentException(
                    "Já existe um cliente com esse CNPJ nesta empresa."
            );
        }
    }


    /*
     * ==================================================
     * VALIDA DUPLICIDADE - ALTERAÇÃO
     * ==================================================
     */

    private void validarDuplicidadeAlteracao(
            ClienteDTO dto,
            Long idEmpresa,
            Long idCliente) {

        String cpf =
                normalizarDocumento(
                        dto.getCpf()
                );

        String cnpj =
                normalizarDocumento(
                        dto.getCnpj()
                );


        if (cpf != null &&
                clienteRepository
                        .existsByEmpresa_IdAndCpfAndIdNot(
                                idEmpresa,
                                cpf,
                                idCliente
                        )) {

            throw new IllegalArgumentException(
                    "Já existe outro cliente com esse CPF nesta empresa."
            );
        }


        if (cnpj != null &&
                clienteRepository
                        .existsByEmpresa_IdAndCnpjAndIdNot(
                                idEmpresa,
                                cnpj,
                                idCliente
                        )) {

            throw new IllegalArgumentException(
                    "Já existe outro cliente com esse CNPJ nesta empresa."
            );
        }
    }


    /*
     * ==================================================
     * NORMALIZA DADOS
     * ==================================================
     */

    private void normalizarDados(
            ClienteDTO dto) {

        if (dto.getNome() != null) {

            dto.setNome(
                    dto.getNome().trim()
            );
        }

        if (dto.getRazaoSocial() != null) {

            dto.setRazaoSocial(
                    dto.getRazaoSocial().trim()
            );
        }

        if (dto.getCpf() != null) {

            dto.setCpf(
                    dto.getCpf()
            );
        }

        if (dto.getCnpj() != null) {

            dto.setCnpj(
                    dto.getCnpj()
            );
        }

        if (dto.getEmailPrincipal() != null) {

            dto.setEmailPrincipal(
                    dto.getEmailPrincipal()
                            .trim()
                            .toLowerCase(Locale.ROOT)
            );
        }



        if (dto.getCidade() != null) {

            dto.setCidade(
                    dto.getCidade().trim()
            );
        }
    }


    /*
     * ==================================================
     * SPECIFICATION - TEXTO
     * ==================================================
     */

    private Specification<Cliente> contemIgnoreCase(
            String campo,
            String valor) {

        return (root, query, cb) ->
                cb.like(
                        cb.upper(
                                root.<String>get(campo)
                        ),
                        "%" +
                                valor.toUpperCase(Locale.ROOT) +
                                "%"
                );
    }


    /*
     * ==================================================
     * SPECIFICATION - DOCUMENTO
     * ==================================================
     */

    private Specification<Cliente> contem(
            String campo,
            String valor) {

        return (root, query, cb) ->
                cb.like(
                        root.<String>get(campo),
                        "%" + valor + "%"
                );
    }


    /*
     * ==================================================
     * NORMALIZA FILTRO
     * ==================================================
     */

    private String normalizarFiltro(
            String valor) {

        if (valor == null ||
                valor.isBlank()) {

            return null;
        }

        return valor.trim();
    }


    /*
     * ==================================================
     * NORMALIZA DOCUMENTO
     * ==================================================
     */

    private String normalizarDocumento(
            String valor) {

        if (valor == null ||
                valor.isBlank()) {

            return null;
        }

        String documento =
                valor.replaceAll(
                        "\\D",
                        ""
                );

        return documento.isBlank()
                ? null
                : documento;
    }


    /*
     * ==================================================
     * STATUS
     * ==================================================
     */

    private Boolean converterStatus(
            Integer status) {

        if (status == null) {
            return null;
        }

        if (status == 0) {
            return false;
        }

        if (status == 1) {
            return true;
        }

        return null;
    }


    /*
     * ==================================================
     * PÁGINA
     * ==================================================
     */

    private int normalizarPagina(
            Integer page) {

        if (page == null ||
                page < 0) {

            return 0;
        }

        return page;
    }


    /*
     * ==================================================
     * TAMANHO DA PÁGINA
     * ==================================================
     */

    private int normalizarTamanhoPagina(
            Integer size) {

        if (size == null ||
                !TAMANHOS_PERMITIDOS.contains(
                        size
                )) {

            return 10;
        }

        return size;
    }


    /*
     * ==================================================
     * EMPRESA
     * ==================================================
     */

    private void validarEmpresa(
            Long idEmpresa) {

        if (idEmpresa == null) {

            throw new IllegalArgumentException(
                    "Empresa não informada."
            );
        }
    }
}