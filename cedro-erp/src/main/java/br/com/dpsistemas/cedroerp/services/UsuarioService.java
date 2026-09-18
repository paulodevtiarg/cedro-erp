package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.UsuarioDTO;
import br.com.dpsistemas.cedroerp.mappers.UsuarioMapper;
import br.com.dpsistemas.cedroerp.models.Departamento;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.Usuario;
import br.com.dpsistemas.cedroerp.repositorys.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EmpresaService empresaService;
    private final DepartamentoService departamentoService;
    private final PasswordEncoder passwordEncoder;


    private static final Set<Integer> TAMANHOS_PERMITIDOS =
            Set.of(5, 10, 20, 30);


    public UsuarioService(
            UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            EmpresaService empresaService,
            DepartamentoService departamentoService,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.empresaService = empresaService;
        this.departamentoService = departamentoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listar(
            UsuarioDTO filtro,
            Long idEmpresa) {

        if (filtro == null) {
            filtro = new UsuarioDTO();
        }

        String nome =  filtro.getFiltroNome();
        String cpf = filtro.getFiltroCpf();
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

        return usuarioRepository
                .filtrar(
                        idEmpresa,
                        nome,
                        cpf,
                        status,
                        pageable
                )
                .map(usuarioMapper::toDTO);
    }


    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(
            Long id,
            Long idEmpresa) {

        Usuario usuario =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        return usuarioMapper.toDTO(
                usuario
        );
    }


    @Transactional(readOnly = true)
    public Usuario buscarEntidadePorId(
            Long id,
            Long idEmpresa) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "ID do usuário não informado."
            );
        }

        validarEmpresa(idEmpresa);


        return usuarioRepository
                .findByIdAndEmpresa_Id(
                        id,
                        idEmpresa
                )
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário não encontrado."
                        )
                );
    }


    @Transactional
    public UsuarioDTO salvar(
            UsuarioDTO dto,
            Long idEmpresa) {

        if (dto == null) {
            throw new IllegalArgumentException("Dados do usuário não informados.");
        }

        normalizarDados(dto);
        Empresa empresa = empresaService.buscarPorId(idEmpresa);
        Departamento departamento = departamentoService.buscarEntidadePorId(dto.getIdDepartamento(),idEmpresa);
        Usuario usuario = usuarioMapper.toEntity(dto,empresa,departamento);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(salvo);
    }


    @Transactional
    public UsuarioDTO atualizar(
            Long id,
            UsuarioDTO dto,
            Long idEmpresa) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Dados do usuário não informados."
            );
        }

        Usuario usuario = buscarEntidadePorId(id,idEmpresa);
        normalizarDados(dto);
        validarDuplicidadeAlteracao(dto,id);
        Empresa empresa = empresaService.buscarPorId(idEmpresa);
        Departamento departamento = departamentoService.buscarEntidadePorId(dto.getIdDepartamento(),idEmpresa);
        usuarioMapper.updateEntity(dto,usuario,empresa,departamento);
        if (
                dto.getSenha() != null && !dto.getSenha().isBlank()
        ) {
              usuario.setSenha(passwordEncoder.encode(dto.getSenha())
            );
        }
        Usuario atualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(atualizado);
    }


    @Transactional(readOnly = true)
    public Usuario autenticar(
            String identificador,
            String senha,
            String tipoLogin) {

        if (
                identificador == null ||
                        identificador.isBlank() ||
                        senha == null ||
                        senha.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Login ou senha inválidos."
            );
        }


        Usuario usuario;


        switch (
                tipoLogin == null
                        ? "login"
                        : tipoLogin.toLowerCase(Locale.ROOT)
        ) {

            case "email" ->
                    usuario = usuarioRepository
                            .findByEmailIgnoreCase(
                                    identificador.trim()
                            )
                            .orElseThrow(() ->
                                    loginInvalido()
                            );


            case "cpf" -> {
                String cpf = identificador;
                usuario = usuarioRepository
                        .findByCpf(cpf)
                        .orElseThrow(() ->
                                loginInvalido()
                        );
            }


            default ->
                    usuario = usuarioRepository
                            .findByLoginIgnoreCase(
                                    identificador.trim()
                            )
                            .orElseThrow(() ->
                                    loginInvalido()
                            );
        }


        /*
         * ========================================
         * USUÁRIO BLOQUEADO
         * ========================================
         *
         * VERIFICA ANTES DA SENHA.
         */
        if (!Boolean.TRUE.equals(usuario.getStatus())) {

            throw new IllegalArgumentException(
                    "Usuário bloqueado. Entre em contato com o administrador do sistema."
            );
        }


        if (!Boolean.TRUE.equals(usuario.getStatus())) {

            throw new IllegalArgumentException("A empresa, a qual o Usuário pertence, está Bloqueada. Entre em contato com o administrador do sistema." );
        }



        /*
         * BCrypt
         */
        if (
                !passwordEncoder.matches(
                        senha,
                        usuario.getSenha()
                )
        ) {

            throw loginInvalido();
        }


        return usuario;
    }

    @Transactional
    public void excluir(
            Long id,
            Long idEmpresa) {
        Usuario usuario = buscarEntidadePorId( id, idEmpresa );

        usuarioRepository.delete(usuario);
    }

    @Transactional
    public void ativar(
            Long id,
            Long idEmpresa) {

        Usuario usuario =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        usuario.ativar();

        usuarioRepository.save(usuario);
    }


    @Transactional
    public void inativar(
            Long id,
            Long idEmpresa) {

        Usuario usuario =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );

        usuario.inativar();

        usuarioRepository.save(usuario);
    }


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


        Usuario usuario =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );


        usuario.setStatus(status);

        usuarioRepository.save(usuario);
    }


    @Transactional
    public void atualizarFoto(
            Long idUsuario,
            Long idEmpresa,
            String foto) {

        Usuario usuario =
                buscarEntidadePorId(
                        idUsuario,
                        idEmpresa
                );

        usuario.setFoto(foto);

        usuarioRepository.save(usuario);
    }

    private void normalizarDados(
            UsuarioDTO dto) {

        if (dto.getLogin() != null) {
            dto.setLogin(dto.getLogin().trim().toLowerCase(Locale.ROOT));
        }

        if (dto.getEmail() != null) {
            dto.setEmail(dto.getEmail().trim().toLowerCase(Locale.ROOT)
            );
        }

        if (dto.getCpf() != null) {
            dto.setCpf(dto.getCpf());
        }
        if (dto.getTelefone() != null) {
            dto.setTelefone(dto.getTelefone());
        }
        if (dto.getNome() != null) {
            dto.setNome(dto.getNome().trim());
        }
    }


    @Transactional
    public Usuario finalizarPrimeiroAcesso(
            Long idUsuario,
            Long idEmpresa) {

        Usuario usuario =
                buscarEntidadePorId(
                        idUsuario,
                        idEmpresa
                );

        usuario.setPrimeiroAcesso(false);

        return usuarioRepository.save(usuario);
    }


    private void validarDuplicidadeCadastro(
            UsuarioDTO dto,
            Long idEmpresa) {

        // Login único
        if (usuarioRepository.existsByLoginIgnoreCase(
                dto.getLogin())) {

            throw new IllegalArgumentException(
                    "Já existe um usuário com esse login."
            );
        }


    }

    @Transactional
    public Usuario alterarSenhaPrimeiroAcesso(
            Long idUsuario,
            Long idEmpresa,
            String senha,
            String confirmarSenha) {

        if (
                senha == null ||
                        senha.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Informe a nova senha."
            );
        }

        if (senha.length() < 6) {

            throw new IllegalArgumentException(
                    "A senha deve possuir no mínimo 6 caracteres."
            );
        }

        if (
                confirmarSenha == null ||
                        !senha.equals(confirmarSenha)
        ) {

            throw new IllegalArgumentException(
                    "As senhas informadas não conferem."
            );
        }

        Usuario usuario =
                buscarEntidadePorId(
                        idUsuario,
                        idEmpresa
                );

        usuario.setSenha(
                passwordEncoder.encode(
                        senha
                )
        );

        usuario.setPrimeiroAcesso(false);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void salvarCodigoRecuperacao(
            Long idUsuario,
            String codigo,
            LocalDateTime dataExpiracao) {

        Usuario usuario =  usuarioRepository.findById(idUsuario)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Usuário não encontrado."
                                )
                        );

        usuario.setCodSeguranca(codigo);
        usuario.setDataExpiracao(dataExpiracao);
        usuarioRepository.save(usuario);
    }


    public Usuario buscarPorLoginCpfOuEmail(String valor) {
        return usuarioRepository
                .findByLoginOrCpfOrEmail(valor, valor, valor)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public boolean existeLoginOutroUsuario(
            String login,
            Long idEmpresa,
            Long idUsuario) {

        if (login == null || login.isBlank()) {
            return false;
        }

        return usuarioRepository
                .existsByEmpresa_IdAndLoginIgnoreCaseAndIdNot(
                        idEmpresa,
                        login.trim(),
                        idUsuario
                );
    }

    @Transactional(readOnly = true)
    public boolean existeLogin(
            String login,
            Long idEmpresa) {

        if (login == null || login.isBlank()) {
            return false;
        }

        return usuarioRepository
                .existsByEmpresa_IdAndLoginIgnoreCase(
                        idEmpresa,
                        login.trim()
                );
    }

    @Transactional(readOnly = true)
    public boolean existeCpf(
            String cpf,
            Long idEmpresa) {

        return usuarioRepository
                .existsByEmpresa_IdAndCpf(
                        idEmpresa,
                        cpf
                );
    }

    @Transactional(readOnly = true)
    public boolean existeCpfOutroUsuario(
            String cpf,
            Long idEmpresa,
            Long idUsuario) {

        return usuarioRepository
                .existsByEmpresa_IdAndCpfAndIdNot(
                        idEmpresa,
                        cpf,
                        idUsuario
                );
    }

    private void validarDuplicidadeAlteracao(
            UsuarioDTO dto,
            Long id) {

        if (
                usuarioRepository
                        .existsByLoginIgnoreCaseAndIdNot(
                                dto.getLogin(),
                                id
                        )
        ) {

            throw new IllegalArgumentException(
                    "Já existe outro usuário com esse login."
            );
        }


        if (
                usuarioRepository
                        .existsByEmailIgnoreCaseAndIdNot(
                                dto.getEmail(),
                                id
                        )
        ) {

            throw new IllegalArgumentException(
                    "Já existe outro usuário com esse e-mail."
            );
        }


        if (
                usuarioRepository
                        .existsByCpfAndIdNot(
                                dto.getCpf(),
                                id
                        )
        ) {

            throw new IllegalArgumentException(
                    "Já existe outro usuário com esse CPF."
            );
        }
    }

    private String normalizarFiltro(String valor)
    {
        if (
                valor == null ||  valor.isBlank()
        ) {
            return null;
        }

        return valor.trim();
    }


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


    private void validarEmpresa(
            Long idEmpresa) {

        if (idEmpresa == null) {

            throw new IllegalArgumentException(
                    "Empresa não informada."
            );
        }
    }


    private IllegalArgumentException loginInvalido() {

        /*
         * Intencionalmente não informamos
         * se foi login, CPF, e-mail ou senha.
         */
        return new IllegalArgumentException(
                "Login ou senha inválidos."
        );
    }

    @Transactional
    public void concluirResetSenha(
            Long idUsuario,
            String novaSenha) {

        Usuario usuario =
                usuarioRepository.findById(idUsuario)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Usuário não encontrado."
                                )
                        );

        usuario.setSenha(
                passwordEncoder.encode(novaSenha)
        );

        usuario.setPrimeiroAcesso(false);
        usuario.setCodSeguranca(null);
        usuario.setDataExpiracao(null);

        usuarioRepository.save(usuario);
    }
}