package br.com.dpsistemas.cedroerp.dtos;


import br.com.dpsistemas.cedroerp.enumerators.EstadosEnum;
import br.com.dpsistemas.cedroerp.enumerators.RegimeTributarioEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class EmpresaDTO {

    private Long id;

    @NotBlank(message = "A razão social é obrigatória.")
    @Size(max = 255, message = "A razão social deve possuir no máximo 255 caracteres.")
    private String razaoSocial;

    @Size(max = 255, message = "O nome fantasia deve possuir no máximo 255 caracteres.")
    private String nomeFantasia;


    private String logo;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @Size(max = 18, message = "CNPJ inválido.")
    private String cnpj;

    @Size(max = 50)
    private String inscEstadual;

    @Size(max = 50)
    private String inscMunicipal;

    @Email(message = "Informe um e-mail válido.")
    @Size(max = 50)
    private String email;

    @Size(max = 20)
    private String telefonePrincipal;

    @Size(max = 20)
    private String telefoneSecundario;

    @Size(max = 255)
    private String site;

    @Size(max = 10)
    private String cep;

    @Size(max = 255)
    private String logradouro;

    @Size(max = 20)
    private String numero;

    @Size(max = 255)
    private String complemento;

    @Size(max = 100)
    private String bairro;

    @Size(max = 100)
    private String cidade;

    private EstadosEnum uf;

    @Size(max = 50)
    private String pais;

    @Size(max = 255)
    private String nomeResponsavel;

    @Size(max = 11)
    private String cpfResponsavel;

    @Email(message = "Informe um e-mail válido para o responsável.")
    @Size(max = 255)
    private String emailResponsavel;

    @Size(max = 20)
    private String telefoneResponsavel;

    @Size(max = 10)
    private String cnae;

    private RegimeTributarioEnum regimeTributario;

    @NotNull(message = "Informe se a empresa é MEI.")
    private Boolean mei;

    /*
     * Campos herdados da BaseEntity.
     * Úteis principalmente para exibição.
     */
    private Boolean status;

    private LocalDateTime dataCadastro;

    private LocalDateTime dataAlteracao;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

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
}
