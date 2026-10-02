package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.ClienteDTO;
import br.com.dpsistemas.cedroerp.dtos.CnaeDTO;
import br.com.dpsistemas.cedroerp.mappers.ClienteMapper;
import br.com.dpsistemas.cedroerp.models.Cliente;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.Municipio;
import br.com.dpsistemas.cedroerp.repositorys.ClienteRepository;
import br.com.dpsistemas.cedroerp.repositorys.MunicipioRepository;
import br.com.dpsistemas.cedroerp.specifications.ClienteSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final MunicipioRepository municipioRepository;

    private static final Set<Integer> TAMANHOS_PERMITIDOS = Set.of(5, 10, 20, 30);

    public ClienteService(
            ClienteRepository clienteRepository,
            ClienteMapper clienteMapper,
            EmpresaService empresaService,
            CnaeService cnaeService,
            MunicipioRepository municipioRepository) {

        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.empresaService = empresaService;
        this.cnaeService = cnaeService;
        this.municipioRepository = municipioRepository;
    }

    private void preencherDadosCnae(ClienteDTO dto) {
        if (dto == null || dto.getCnae() == null || dto.getCnae().isBlank()) {
            return;
        }

        CnaeDTO cnae = cnaeService.buscarPorCodigo(dto.getCnae());

        if (cnae != null) {
            dto.setCnaeDescricao(
                    cnae.getDescricao() != null ? cnae.getDescricao().toUpperCase() : null
            );

            dto.setCnaeDivisao(
                    cnae.getDivisao() != null ? cnae.getDivisao().toUpperCase() : null
            );
        }
    }

    /*
     * ==================================================
     * LISTAR / FILTRAR
     * ==================================================
     */

    @Transactional(readOnly = true)
    public Page<ClienteDTO> listar(ClienteDTO filtro, Long idEmpresa) {
        validarEmpresa(idEmpresa);

        if (filtro == null) {
            filtro = new ClienteDTO();
        }

        String nome = normalizarFiltro(filtro.getFiltroNome());
        String cpf = normalizarDocumento(filtro.getFiltroCpf());
        String cnpj = normalizarDocumento(filtro.getFiltroCnpj());
        String razaoSocial = normalizarFiltro(filtro.getFiltroRazaoSocial());
        String cidade = normalizarFiltro(filtro.getFiltroCidade());
        Boolean status = converterStatus(filtro.getFiltroStatus());

        int page = normalizarPagina(filtro.getPage());
        int size = normalizarTamanhoPagina(filtro.getSize());

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "nome"));

        Specification<Cliente> spec = ClienteSpecification.empresaIgual(idEmpresa)
                .and(ClienteSpecification.nomeContem(nome))
                .and(ClienteSpecification.cpfContem(cpf))
                .and(ClienteSpecification.cnpjContem(cnpj))
                .and(ClienteSpecification.razaoSocialContem(razaoSocial))
                .and(ClienteSpecification.cidadeContem(cidade))
                .and(ClienteSpecification.statusIgual(status));

        return clienteRepository.findAll(spec, pageable).map(clienteMapper::toDTO);
    }

    /*
     * ==================================================
     * BUSCAR DTO POR ID
     * ==================================================
     */

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id, Long idEmpresa) {
        Cliente cliente = buscarEntidadePorId(id, idEmpresa);
        ClienteDTO dto = clienteMapper.toDTO(cliente);
        preencherDadosCnae(dto);
        return dto;
    }

    /*
     * ==================================================
     * BUSCAR ENTITY POR ID
     * ==================================================
     */

    @Transactional(readOnly = true)
    public Cliente buscarEntidadePorId(Long id, Long idEmpresa) {
        if (id == null) {
            throw new IllegalArgumentException("ID do cliente não informado.");
        }

        validarEmpresa(idEmpresa);

        return clienteRepository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
    }

    /*
     * ==================================================
     * SALVAR
     * ==================================================
     */

    @Transactional
    public ClienteDTO salvar(ClienteDTO dto, Long idEmpresa) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do cliente não informados.");
        }

        validarEmpresa(idEmpresa);
        normalizarDados(dto);
        validarDuplicidadeCadastro(dto, idEmpresa);

        // Define o idEmpresa no DTO para ser mapeado pelo Mapper
        dto.setIdEmpresa(idEmpresa);

        // Converte o DTO para Entidade
        Cliente cliente = clienteMapper.toEntity(dto);

        // Garante que a Empresa persistida no banco está associada corretamente
        Empresa empresa = empresaService.buscarPorId(idEmpresa);
        cliente.setEmpresa(empresa);

        // Se o município foi informado no DTO, associa o município persistido
        if (dto.getIdMunicipio() != null) {
            Municipio municipio = municipioRepository.findById(dto.getIdMunicipio())
                    .orElseThrow(() -> new IllegalArgumentException("Município não encontrado."));
            cliente.setMunicipio(municipio);

            // Opcional: Atualiza a string da cidade caso esteja nula
            if (cliente.getCidade() == null || cliente.getCidade().isBlank()) {
                cliente.setCidade(municipio.getNome());
            }
        }

        Cliente salvo = clienteRepository.save(cliente);

        return clienteMapper.toDTO(salvo);
    }

    /*
     * ==================================================
     * ATUALIZAR
     * ==================================================
     */

    @Transactional
    public ClienteDTO atualizar(Long id, ClienteDTO dto, Long idEmpresa) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do cliente não informados.");
        }

        Cliente cliente = buscarEntidadePorId(id, idEmpresa);

        normalizarDados(dto);
        validarDuplicidadeAlteracao(dto, idEmpresa, id);

        // Copia os dados atualizados do DTO para a Entidade existente
        dto.setId(id);
        dto.setIdEmpresa(idEmpresa);

        // Converte novos dados para a entidade
        Cliente dadosAtualizados = clienteMapper.toEntity(dto);

        // Atualiza os campos da entidade gerenciada
        cliente.setTipoPessoa(dadosAtualizados.getTipoPessoa());
        cliente.setNome(dadosAtualizados.getNome());
        cliente.setRazaoSocial(dadosAtualizados.getRazaoSocial());
        cliente.setCpf(dadosAtualizados.getCpf());
        cliente.setCnpj(dadosAtualizados.getCnpj());
        cliente.setInscricaoEstadual(dadosAtualizados.getInscricaoEstadual());
        cliente.setEmailPrincipal(dadosAtualizados.getEmailPrincipal());
        cliente.setTelefone(dadosAtualizados.getTelefone());
        cliente.setCelular(dadosAtualizados.getCelular());
        cliente.setWhatsapp(dadosAtualizados.getWhatsapp());
        cliente.setCep(dadosAtualizados.getCep());
        cliente.setLogradouro(dadosAtualizados.getLogradouro());
        cliente.setNumero(dadosAtualizados.getNumero());
        cliente.setComplemento(dadosAtualizados.getComplemento());
        cliente.setBairro(dadosAtualizados.getBairro());
        cliente.setCidade(dadosAtualizados.getCidade());
        cliente.setUf(dadosAtualizados.getUf());
        cliente.setPais(dadosAtualizados.getPais());
        cliente.setDataNascimentoInicio(dadosAtualizados.getDataNascimentoInicio());
        cliente.setGenero(dadosAtualizados.getGenero());
        cliente.setEstadoCivil(dadosAtualizados.getEstadoCivil());
        cliente.setCnae(dadosAtualizados.getCnae());
        cliente.setRegimeTributario(dadosAtualizados.getRegimeTributario());
        cliente.setMei(dadosAtualizados.getMei());
        cliente.setObs(dadosAtualizados.getObs());

        // Atualiza o Município
        if (dto.getIdMunicipio() != null) {
            Municipio municipio = municipioRepository.findById(dto.getIdMunicipio())
                    .orElseThrow(() -> new IllegalArgumentException("Município não encontrado."));
            cliente.setMunicipio(municipio);
        } else {
            cliente.setMunicipio(null);
        }

        Cliente atualizado = clienteRepository.save(cliente);

        return clienteMapper.toDTO(atualizado);
    }

    /*
     * ==================================================
     * EXCLUIR
     * ==================================================
     */

    @Transactional
    public void excluir(Long id, Long idEmpresa) {
        Cliente cliente = buscarEntidadePorId(id, idEmpresa);
        clienteRepository.delete(cliente);
    }

    /*
     * ==================================================
     * ATIVAR / INATIVAR / STATUS
     * ==================================================
     */

    @Transactional
    public void ativar(Long id, Long idEmpresa) {
        Cliente cliente = buscarEntidadePorId(id, idEmpresa);
        cliente.setStatus(true);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void inativar(Long id, Long idEmpresa) {
        Cliente cliente = buscarEntidadePorId(id, idEmpresa);
        cliente.setStatus(false);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void alterarStatus(Long id, Long idEmpresa, Boolean status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não informado.");
        }

        Cliente cliente = buscarEntidadePorId(id, idEmpresa);
        cliente.setStatus(status);
        clienteRepository.save(cliente);
    }

    /*
     * ==================================================
     * CPF - VERIFICAÇÕES
     * ==================================================
     */

    @Transactional(readOnly = true)
    public boolean existeCpf(String cpf, Long idEmpresa) {
        cpf = normalizarDocumento(cpf);
        if (cpf == null) {
            return false;
        }
        return clienteRepository.existsByEmpresa_IdAndCpf(idEmpresa, cpf);
    }

    @Transactional(readOnly = true)
    public boolean existeCpfOutroCliente(String cpf, Long idEmpresa, Long idCliente) {
        cpf = normalizarDocumento(cpf);
        if (cpf == null) {
            return false;
        }
        return clienteRepository.existsByEmpresa_IdAndCpfAndIdNot(idEmpresa, cpf, idCliente);
    }

    /*
     * ==================================================
     * CNPJ - VERIFICAÇÕES
     * ==================================================
     */

    @Transactional(readOnly = true)
    public boolean existeCnpj(String cnpj, Long idEmpresa) {
        cnpj = normalizarDocumento(cnpj);
        if (cnpj == null) {
            return false;
        }
        return clienteRepository.existsByEmpresa_IdAndCnpj(idEmpresa, cnpj);
    }

    @Transactional(readOnly = true)
    public boolean existeCnpjOutroCliente(String cnpj, Long idEmpresa, Long idCliente) {
        cnpj = normalizarDocumento(cnpj);
        if (cnpj == null) {
            return false;
        }
        return clienteRepository.existsByEmpresa_IdAndCnpjAndIdNot(idEmpresa, cnpj, idCliente);
    }

    /*
     * ==================================================
     * VALIDAÇÕES PRIVADAS
     * ==================================================
     */

    private void validarDuplicidadeCadastro(ClienteDTO dto, Long idEmpresa) {
        String cpf = normalizarDocumento(dto.getCpf());
        String cnpj = normalizarDocumento(dto.getCnpj());

        if (cpf != null && clienteRepository.existsByEmpresa_IdAndCpf(idEmpresa, cpf)) {
            throw new IllegalArgumentException("Já existe um cliente com esse CPF nesta empresa.");
        }

        if (cnpj != null && clienteRepository.existsByEmpresa_IdAndCnpj(idEmpresa, cnpj)) {
            throw new IllegalArgumentException("Já existe um cliente com esse CNPJ nesta empresa.");
        }
    }

    private void validarDuplicidadeAlteracao(ClienteDTO dto, Long idEmpresa, Long idCliente) {
        String cpf = normalizarDocumento(dto.getCpf());
        String cnpj = normalizarDocumento(dto.getCnpj());

        if (cpf != null && clienteRepository.existsByEmpresa_IdAndCpfAndIdNot(idEmpresa, cpf, idCliente)) {
            throw new IllegalArgumentException("Já existe outro cliente com esse CPF nesta empresa.");
        }

        if (cnpj != null && clienteRepository.existsByEmpresa_IdAndCnpjAndIdNot(idEmpresa, cnpj, idCliente)) {
            throw new IllegalArgumentException("Já existe outro cliente com esse CNPJ nesta empresa.");
        }
    }

    private void normalizarDados(ClienteDTO dto) {
        if (dto.getNome() != null) {
            dto.setNome(dto.getNome().trim());
        }
        if (dto.getRazaoSocial() != null) {
            dto.setRazaoSocial(dto.getRazaoSocial().trim());
        }
        if (dto.getEmailPrincipal() != null) {
            dto.setEmailPrincipal(dto.getEmailPrincipal().trim().toLowerCase(Locale.ROOT));
        }
        if (dto.getCidade() != null) {
            dto.setCidade(dto.getCidade().trim());
        }
    }

    private String normalizarFiltro(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }

    private String normalizarDocumento(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        String documento = valor.replaceAll("\\D", "");
        return documento.isBlank() ? null : documento;
    }

    private Boolean converterStatus(Integer status) {
        if (status == null) return null;
        if (status == 0) return false;
        if (status == 1) return true;
        return null;
    }

    private int normalizarPagina(Integer page) {
        return (page == null || page < 0) ? 0 : page;
    }

    private int normalizarTamanhoPagina(Integer size) {
        return (size == null || !TAMANHOS_PERMITIDOS.contains(size)) ? 10 : size;
    }

    private void validarEmpresa(Long idEmpresa) {
        if (idEmpresa == null) {
            throw new IllegalArgumentException("Empresa não informada.");
        }
    }
}