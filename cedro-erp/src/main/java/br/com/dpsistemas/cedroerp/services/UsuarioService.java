package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.UsuarioDTO;
import br.com.dpsistemas.cedroerp.mappers.UsuarioMapper;
import br.com.dpsistemas.cedroerp.models.Departamento;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.Usuario;
import br.com.dpsistemas.cedroerp.repositorys.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        this.usuarioRepository =
                usuarioRepository;

        this.usuarioMapper =
                usuarioMapper;

        this.empresaService =
                empresaService;

        this.departamentoService =
                departamentoService;

        this.passwordEncoder =
                passwordEncoder;
    }


    /*
     * ==================================================
     * INDEX
     * ==================================================
     */

    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listar(
            UsuarioDTO filtro,
            Long idEmpresa) {

        validarEmpresa(idEmpresa);

        if (filtro == null) {
            filtro = new UsuarioDTO();
        }

        String nome =
                normalizarFiltro(
                        filtro.getFiltroNome()
                );

        String cpf =
                normalizarCpfFiltro(
                        filtro.getFiltroCpf()
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


        Page<Usuario> usuarios =
                usuarioRepository.filtrar(
                        idEmpresa,
                        nome,
                        cpf,
                        status,
                        pageable
                );


        return usuarios.map(
                usuarioMapper::toDTO
        );
    }


    /*
     * ==================================================
     * BUSCAR DTO
     * ==================================================
     */

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


    /*
     * ==================================================
     * BUSCAR ENTIDADE
     * ==================================================
     */

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


    /*
     * ==================================================
     * CADASTRAR
     * ==================================================
     */

    @Transactional
    public UsuarioDTO salvar(
            UsuarioDTO dto,
            Long idEmpresa) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Dados do usuário não informados."
            );
        }

        validarEmpresa(idEmpresa);

        normalizarDados(dto);

        validarDadosObrigatorios(dto);

        validarSenhaCadastro(dto);

        validarDuplicidadeCadastro(dto);


        Empresa empresa =
                empresaService.buscarPorId(
                        idEmpresa
                );


        /*
         * Esse método garante que o departamento
         * pertence à mesma empresa.
         */
        Departamento departamento =
                departamentoService
                        .buscarEntidadePorId(
                                dto.getIdDepartamento(),
                                idEmpresa
                        );


        Usuario usuario =
                usuarioMapper.toEntity(
                        dto,
                        empresa,
                        departamento
                );


        /*
         * O texto puro nunca é salvo.
         */
        usuario.setSenha(
                passwordEncoder.encode(
                        dto.getSenha()
                )
        );


        Usuario salvo =
                usuarioRepository.save(
                        usuario
                );


        return usuarioMapper.toDTO(
                salvo
        );
    }


    /*
     * ==================================================
     * ALTERAR
     * ==================================================
     */

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


        Usuario usuario =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );


        normalizarDados(dto);

        validarDadosObrigatorios(dto);

        validarDuplicidadeAlteracao(
                dto,
                id
        );


        Empresa empresa =
                empresaService.buscarPorId(
                        idEmpresa
                );


        Departamento departamento =
                departamentoService
                        .buscarEntidadePorId(
                                dto.getIdDepartamento(),
                                idEmpresa
                        );


        usuarioMapper.updateEntity(
                dto,
                usuario,
                empresa,
                departamento
        );


        /*
         * Se informar uma nova senha,
         * altera.
         *
         * Se deixar vazio,
         * mantém a senha atual.
         */
        if (
                dto.getSenha() != null &&
                        !dto.getSenha().isBlank()
        ) {

            validarNovaSenha(dto);

            usuario.setSenha(
                    passwordEncoder.encode(
                            dto.getSenha()
                    )
            );
        }


        Usuario atualizado =
                usuarioRepository.save(
                        usuario
                );


        return usuarioMapper.toDTO(
                atualizado
        );
    }


    /*
     * ==================================================
     * AUTENTICAÇÃO
     * ==================================================
     */

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

                String cpf =
                        somenteNumeros(
                                identificador
                        );

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
         * Usuário inativo não pode entrar.
         */
        if (!Boolean.TRUE.equals(
                usuario.getStatus())) {

            throw loginInvalido();
        }


        /*
         * Empresa inativa também bloqueia acesso.
         */
        if (
                usuario.getEmpresa() == null ||
                        !Boolean.TRUE.equals(
                                usuario.getEmpresa().getStatus()
                        )
        ) {

            throw loginInvalido();
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


    /*
     * ==================================================
     * ATIVAR
     * ==================================================
     */

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


    /*
     * ==================================================
     * INATIVAR
     * ==================================================
     */

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


        Usuario usuario =
                buscarEntidadePorId(
                        id,
                        idEmpresa
                );


        usuario.setStatus(status);

        usuarioRepository.save(usuario);
    }


    /*
     * ==================================================
     * NORMALIZAÇÃO
     * ==================================================
     */

    private void normalizarDados(
            UsuarioDTO dto) {

        if (dto.getLogin() != null) {

            dto.setLogin(
                    dto.getLogin()
                            .trim()
                            .toLowerCase(Locale.ROOT)
            );
        }


        if (dto.getEmail() != null) {

            dto.setEmail(
                    dto.getEmail()
                            .trim()
                            .toLowerCase(Locale.ROOT)
            );
        }


        if (dto.getCpf() != null) {

            dto.setCpf(
                    somenteNumeros(
                            dto.getCpf()
                    )
            );
        }


        if (dto.getTelefone() != null) {

            dto.setTelefone(
                    somenteNumeros(
                            dto.getTelefone()
                    )
            );
        }


        if (dto.getNome() != null) {

            dto.setNome(
                    dto.getNome().trim()
            );
        }
    }


    /*
     * ==================================================
     * VALIDAÇÕES
     * ==================================================
     */

    private void validarDadosObrigatorios(
            UsuarioDTO dto) {

        if (
                dto.getLogin() == null ||
                        dto.getLogin().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "O login é obrigatório."
            );
        }


        if (
                dto.getNome() == null ||
                        dto.getNome().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "O nome é obrigatório."
            );
        }


        if (
                dto.getEmail() == null ||
                        dto.getEmail().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "O e-mail é obrigatório."
            );
        }


        if (
                dto.getCpf() == null ||
                        dto.getCpf().length() != 11
        ) {

            throw new IllegalArgumentException(
                    "CPF inválido."
            );
        }


        if (dto.getIdDepartamento() == null) {

            throw new IllegalArgumentException(
                    "O departamento é obrigatório."
            );
        }


        if (dto.getPerfil() == null) {

            throw new IllegalArgumentException(
                    "O perfil é obrigatório."
            );
        }
    }


    private void validarSenhaCadastro(
            UsuarioDTO dto) {

        if (
                dto.getSenha() == null ||
                        dto.getSenha().isBlank()
        ) {

            throw new IllegalArgumentException(
                    "A senha é obrigatória."
            );
        }

        validarNovaSenha(dto);
    }


    private void validarNovaSenha(
            UsuarioDTO dto) {

        if (dto.getSenha().length() < 6) {

            throw new IllegalArgumentException(
                    "A senha deve possuir no mínimo 6 caracteres."
            );
        }


        if (
                dto.getConfirmarSenha() == null ||
                        !dto.getSenha().equals(
                                dto.getConfirmarSenha()
                        )
        ) {

            throw new IllegalArgumentException(
                    "As senhas informadas não conferem."
            );
        }
    }


    /*
     * ==================================================
     * DUPLICIDADES
     * ==================================================
     */

    private void validarDuplicidadeCadastro(
            UsuarioDTO dto) {

        if (
                usuarioRepository
                        .existsByLoginIgnoreCase(
                                dto.getLogin()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Já existe um usuário com esse login."
            );
        }


        if (
                usuarioRepository
                        .existsByEmailIgnoreCase(
                                dto.getEmail()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Já existe um usuário com esse e-mail."
            );
        }


        if (
                usuarioRepository
                        .existsByCpf(
                                dto.getCpf()
                        )
        ) {

            throw new IllegalArgumentException(
                    "Já existe um usuário com esse CPF."
            );
        }
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


    /*
     * ==================================================
     * FILTROS
     * ==================================================
     */

    private String normalizarFiltro(
            String valor) {

        if (
                valor == null ||
                        valor.isBlank()
        ) {

            return null;
        }

        return valor.trim();
    }


    private String normalizarCpfFiltro(
            String cpf) {

        if (
                cpf == null ||
                        cpf.isBlank()
        ) {

            return null;
        }

        return somenteNumeros(cpf);
    }


    /*
     * null → todos
     * 0    → inativo
     * 1    → ativo
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


    /*
     * ==================================================
     * UTILITÁRIOS
     * ==================================================
     */

    private String somenteNumeros(
            String valor) {

        if (valor == null) {
            return null;
        }

        return valor.replaceAll(
                "\\D",
                ""
        );
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
}