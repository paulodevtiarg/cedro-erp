package br.com.dpsistemas.cedroerp.models;

import br.com.dpsistemas.cedroerp.enumerators.*;
import br.com.dpsistemas.cedroerp.utils.UppercaseTrimConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name ="CLIENTE")
public class Cliente  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPRESA", nullable = false)
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_PESSOA", nullable = false)
    private TipoPessoaEnum tipoPessoa;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "NOME", nullable = true, length = 255)
    private String nome;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "RAZAO_SOCIAL", nullable = true, length = 255)
    private String razaoSocial;

    @Column(name = "CPF", nullable = true, length = 255)
    private String cpf;

    @Column(name = "CNPJ", nullable = true, length = 255)
    private String cnpj;

    @Column(name = "INSCRICAO_ESTADUAL", nullable = true, length = 50)
    private String inscricaoEstadual;

    @Column(name = "EMAIL_PRINCIPAL", nullable = true, length = 255)
    private String emailPrincipal;



    @Column(name = "TELEFONE", nullable = true, length = 20)
    private String telefone;

    @Column(name = "CELULAR", nullable = true, length = 20)
    private String celular;

    @Column(name = "WHATSAPP", nullable = true)
    private Boolean whatsapp;

    @Column(name = "CEP", nullable = true, length = 10)
    private String cep;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "LOGRADOURO", nullable = true, length = 255)
    private String logradouro;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "NUMERO", nullable = true, length = 20)
    private String numero;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "COMPLEMENTO", nullable = true, length = 255)
    private String complemento;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "BAIRRO", nullable = true, length = 100)
    private String bairro;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "CIDADE", nullable = true, length = 100)
    private String cidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", length = 2)
    private EstadosEnum uf;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "PAIS", nullable = true, length = 50)
    private String pais;

    @Column(name = "DATA_NASCIMENTO", nullable = true)
    private LocalDateTime dataNascimentoInicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENERO", length = 2)
    private GeneroEnum genero;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_CIVIL", length = 2)
    private EstadoCivilEnum estadoCivil;

    @Column(name = "CNAE_PRINCIPAL", nullable = true, length = 10)
    private String cnae;

    @Enumerated(EnumType.STRING)
    @Column(name = "REGIME_TRIBUTARIO", length = 2)
    private RegimeTributarioEnum regimeTributario;


    @Column(name = "MEI", nullable = true)
    private Boolean mei;

    @Convert(converter = UppercaseTrimConverter.class)
    @Column(name = "OBSERVACOES", nullable = true, length = 255)
    private String obs;

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

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getEmailPrincipal() {
        return emailPrincipal;
    }

    public void setEmailPrincipal(String emailPrincipal) {
        this.emailPrincipal = emailPrincipal;
    }


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Boolean getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(Boolean whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public EstadosEnum getUf() {
        return uf;
    }

    public void setUf(EstadosEnum uf) {
        this.uf = uf;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public LocalDateTime getDataNascimentoInicio() {
        return dataNascimentoInicio;
    }

    public void setDataNascimentoInicio(LocalDateTime dataNascimentoInicio) {
        this.dataNascimentoInicio = dataNascimentoInicio;
    }

    public GeneroEnum getGenero() {
        return genero;
    }

    public void setGenero(GeneroEnum genero) {
        this.genero = genero;
    }

    public EstadoCivilEnum getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivilEnum estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    public RegimeTributarioEnum getRegimeTributario() {
        return regimeTributario;
    }

    public void setRegimeTributario(RegimeTributarioEnum regimeTributario) {
        this.regimeTributario = regimeTributario;
    }

    public Boolean getMei() {
        return mei;
    }

    public void setMei(Boolean mei) {
        this.mei = mei;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
