package br.com.dpsistemas.cedroerp.models;

import br.com.dpsistemas.cedroerp.enumerators.EstadosEnum;
import br.com.dpsistemas.cedroerp.enumerators.PerfilEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="USUARIO")
public class Usuario extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPRESA", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEPARTAMENTO", nullable = false)
    private Departamento departamento;


    @Column(name = "LOGIN", nullable = false, length = 50)
    private String login;

    @Column(name = "SENHA_HASH", nullable = false, length = 255)
    private String senha;

    @Column(name = "NOME", nullable = false, length = 255)
    private String nome;

    @Column(name = "EMAIL", nullable = false, length = 255)
    private String email;

    @Column(name = "CPF", nullable = false, length = 11)
    private String cpf;

    @Column(name = "TELEFONE", nullable = false, length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "PERFIL")
    private PerfilEnum perfil;


    @Column(name = "FOTO", nullable = true, length = 255)
    private String foto;

    @Column(name = "CODIGO_SEGURANCA", nullable = true, length = 255)
    private String codSeguranca;

    @Column(name = "CODIGO_EXPIRACAO")
    private LocalDateTime codigoExpiracao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
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

    public void setCpf(String cpf) {
        this.cpf = cpf;
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

    public String getCodSeguranca() {
        return codSeguranca;
    }

    public void setCodSeguranca(String codSeguranca) {
        this.codSeguranca = codSeguranca;
    }

    public LocalDateTime getCodigoExpiracao() {
        return codigoExpiracao;
    }

    public void setCodigoExpiracao(LocalDateTime codigoExpiracao) {
        this.codigoExpiracao = codigoExpiracao;
    }
}
