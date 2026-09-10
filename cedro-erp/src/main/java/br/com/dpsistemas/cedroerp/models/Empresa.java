package br.com.dpsistemas.cedroerp.models;

import br.com.dpsistemas.cedroerp.enumerators.EstadosEnum;
import br.com.dpsistemas.cedroerp.enumerators.RegimeTributarioEnum;
import jakarta.persistence.*;

@Entity
@Table(name ="EMPRESA")
public class Empresa extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPRESA")
    private Long id;

    @Column(name = "RAZAO_SOCIAL", nullable = false, length = 255)
    private String razaoSocial;

    @Column(name = "NOME_FANTASIA", nullable = true, length = 255)
    private String nomeFantasia;

    @Column(name = "LOGO", nullable = true, length = 255)
    private String logo;

    @Column(name = "CNPJ", nullable = false, length = 18)
    private String cnpj;

    @Column(name = "INSCRICAO_ESTADUAL", nullable = true, length = 50)
    private String inscEstadual;

    @Column(name = "INSCRICAO_MUNICIPAL", nullable = true, length = 50)
    private String inscMunicipal;

    @Column(name = "EMAIL_PRINCIPAL", nullable = true, length = 50)
    private String email;

    @Column(name = "TELEFONE_PRINCIPAL", nullable = true, length = 20)
    private String telefonePrincipal;

    @Column(name = "TELEFONE_SECUNDARIO", nullable = true, length = 20)
    private String telefoneSecundario;

    @Column(name = "SITE", nullable = true, length = 255)
    private String site;

    @Column(name = "CEP", nullable = true, length = 10)
    private String cep;

    @Column(name = "LOGRADOURO", nullable = true, length = 255)
    private String logradouro;

    @Column(name = "NUMERO", nullable = true, length = 20)
    private String numero;

    @Column(name = "COMPLEMENTO", nullable = true, length = 255)
    private String complemento;

    @Column(name = "BAIRRO", nullable = true, length = 100)
    private String bairro;

    @Column(name = "CIDADE", nullable = true, length = 100)
    private String cidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", length = 2)
    private EstadosEnum uf;

    @Column(name = "PAIS", nullable = true, length = 50)
    private String pais;

    @Column(name = "NOME_RESPONSAVEL", nullable = true, length = 255)
    private String nomeResponsavel;

    @Column(name = "CPF_RESPONSAVEL", nullable = true, length = 11)
    private String cpfResponsavel;

    @Column(name = "EMAIL_RESPONSAVEL", nullable = true, length = 255)
    private String emailResponsavel;

    @Column(name = "TELEFONE_RESPONSAVEL", nullable = true, length = 20)
    private String telefoneResponsavel;

    @Column(name = "CNAE_PRINCIPAL", nullable = true, length = 10)
    private String cnae;

    @Enumerated(EnumType.STRING)
    @Column(name = "REGIME_TRIBUTARIO", length = 30)
    private RegimeTributarioEnum regimeTributario;

    @Column(name = "MEI", nullable = false)
    private Boolean mei;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscEstadual() {
        return inscEstadual;
    }

    public void setInscEstadual(String inscEstadual) {
        this.inscEstadual = inscEstadual;
    }

    public String getInscMunicipal() {
        return inscMunicipal;
    }

    public void setInscMunicipal(String inscMunicipal) {
        this.inscMunicipal = inscMunicipal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonePrincipal() {
        return telefonePrincipal;
    }

    public void setTelefonePrincipal(String telefonePrincipal) {
        this.telefonePrincipal = telefonePrincipal;
    }

    public String getTelefoneSecundario() {
        return telefoneSecundario;
    }

    public void setTelefoneSecundario(String telefoneSecundario) {
        this.telefoneSecundario = telefoneSecundario;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getEmailResponsavel() {
        return emailResponsavel;
    }

    public void setEmailResponsavel(String emailResponsavel) {
        this.emailResponsavel = emailResponsavel;
    }

    public String getTelefoneResponsavel() {
        return telefoneResponsavel;
    }

    public void setTelefoneResponsavel(String telefoneResponsavel) {
        this.telefoneResponsavel = telefoneResponsavel;
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
}
