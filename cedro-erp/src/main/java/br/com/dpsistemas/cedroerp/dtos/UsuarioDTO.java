package br.com.dpsistemas.cedroerp.dtos;

import br.com.dpsistemas.cedroerp.enumerators.PerfilEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class UsuarioDTO {

    private Long id;

    /*
     * A empresa será obtida pela sessão.
     * Mantemos o ID no DTO para consulta/exibição,
     * mas não precisa vir do formulário.
     */
    private Long idEmpresa;

    @NotNull(message = "O departamento é obrigatório.")
    private Long idDepartamento;

    /*
     * Podemos retornar esses nomes no DTO
     * para facilitar index/detalhes posteriormente.
     */
    private String nomeEmpresa;

    private String nomeDepartamento;


    @NotBlank(message = "O login é obrigatório.")
    @Size(
            min = 3,
            max = 50,
            message = "O login deve possuir entre 3 e 50 caracteres."
    )
    private String login;

    private String senha;

    private String confirmarSenha;


    @NotBlank(message = "O nome é obrigatório.")
    @Size(
            max = 255,
            message = "O nome deve possuir no máximo 255 caracteres."
    )
    private String nome;


    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(
            max = 255,
            message = "O e-mail deve possuir no máximo 255 caracteres."
    )
    private String email;


    @NotBlank(message = "O CPF é obrigatório.")
    @Size(
            min = 11,
            max = 11,
            message = "O CPF deve possuir 11 dígitos."
    )
    private String cpf;


    @NotBlank(message = "O telefone é obrigatório.")
    @Size(
            max = 20,
            message = "O telefone deve possuir no máximo 20 caracteres."
    )
    private String telefone;


    @NotNull(message = "O perfil é obrigatório.")
    private PerfilEnum perfil;

    private String foto;

    /*
     * Herdados da BaseEntity
     */
    private Boolean status;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAlteracao;


    private Boolean primeiroAcesso = false;

    private LocalDateTime dataExpiracao;

    private String codigoSeguranca;

    /*
     * ==========================
     * FILTROS DA TELA INDEX
     * ==========================
     */

    private String filtroNome;
    private String filtroCpf;
    private Integer filtroStatus;
    private Integer page = 0;
    private Integer size = 10;

    /*
     * ==========================
     * GETTERS E SETTERS
     * ==========================
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Long getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Long idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getNomeDepartamento() {
        return nomeDepartamento;
    }

    public void setNomeDepartamento(String nomeDepartamento) {
        this.nomeDepartamento = nomeDepartamento;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCpfFormatado() {

        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }

        return cpf.replaceFirst(
                "(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                "$1.$2.$3-$4"
        );
    }

    public void setCpf(String cpf) {
        this.cpf = cpf == null
                ? null
                : cpf.replaceAll("\\D", "");
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public PerfilEnum getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilEnum perfil) {
        this.perfil = perfil;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public String getFiltroNome() {
        return filtroNome;
    }

    public void setFiltroNome(String filtroNome) {
        this.filtroNome = Optional.ofNullable(filtroNome)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(s -> s.toUpperCase(Locale.ROOT))
                .orElse(null);
    }

    public String getFiltroCpf() {
        return filtroCpf;
    }

    public void setFiltroCpf(String filtroCpf) {
        this.filtroCpf = Optional.ofNullable(filtroCpf)
                .map(s -> s.replaceAll("\\D", ""))
                .filter(s -> !s.isBlank())
                .orElse(null);
    }

    public Integer getFiltroStatus() {
        return filtroStatus;
    }

    public void setFiltroStatus(Integer filtroStatus) {
        this.filtroStatus = filtroStatus;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getPrimeiroAcesso() {
        return primeiroAcesso;
    }

    public void setPrimeiroAcesso(Boolean primeiroAcesso) {
        this.primeiroAcesso = primeiroAcesso;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }

    public void setCodigoSeguranca(String codigoSeguranca) {
        this.codigoSeguranca = codigoSeguranca;
    }

    public String getDataAlteracaoFormatada() {
        if (dataAlteracao == null) {
            return "";
        }

        return dataAlteracao.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
    }

    public String getDataCadastroFormatada() {
        if (dataCadastro == null) {
            return "";
        }

        return dataCadastro.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );
    }
}