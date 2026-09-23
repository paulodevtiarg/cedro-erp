package br.com.dpsistemas.cedroerp.dtos;

import br.com.dpsistemas.cedroerp.enumerators.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClienteDTO {

    private Long id;

    // EMPRESA
    private Long idEmpresa;
    private String nomeEmpresa;

    // DADOS PRINCIPAIS
    private TipoPessoaEnum tipoPessoa;

    private String nome;
    private String razaoSocial;

    private String cpf;
    private String cnpj;

    private String inscricaoEstadual;

    private String emailPrincipal;


    private String telefone;
    private String celular;

    private Boolean whatsapp;

    // ENDEREÇO
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private EstadosEnum uf;
    private String pais;

    private String cnaeDescricao;
    private String cnaeDivisao;



    // PESSOA FÍSICA
    private LocalDateTime dataNascimentoInicio;
    private GeneroEnum genero;
    private EstadoCivilEnum estadoCivil;

    // PESSOA JURÍDICA
    private String cnae;
    private RegimeTributarioEnum regimeTributario;
    private Boolean mei;

    private String obs;

    // BASE ENTITY
    private Boolean status;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;

    // FILTROS
    private String filtroNome;
    private String filtroCpf;
    private String filtroCnpj;
    private String filtroRazaoSocial;
    private String filtroCidade;
    private Integer filtroStatus = 1;
    private Integer page = 0;
    private Integer size = 10;

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    /*
     * ==================================================
     * CAMPOS FORMATADOS
     * ==================================================
     */

    public String getCnaeFormatado() {

        if (cnae == null || cnae.length() != 7) {
            return cnae;
        }

        return cnae.replaceFirst(
                "(\\d{4})(\\d)(\\d{2})",
                "$1-$2/$3"
        );
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

    public String getCnpjFormatado() {

        if (cnpj == null || cnpj.length() != 14) {
            return cnpj;
        }

        return cnpj.replaceFirst(
                "(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                "$1.$2.$3/$4-$5"
        );
    }

    public String getDataNascimentoInicioFormatada() {

        if (dataNascimentoInicio == null) {
            return "";
        }

        return dataNascimentoInicio.format(FORMATO_DATA);
    }

    public String getDataCadastroFormatada() {

        if (dataCadastro == null) {
            return "";
        }

        return dataCadastro.format(FORMATO_DATA_HORA);
    }

    public String getDataAlteracaoFormatada() {

        if (dataAlteracao == null) {
            return "";
        }

        return dataAlteracao.format(FORMATO_DATA_HORA);
    }


    /*
     * ==================================================
     * GETTERS E SETTERS
     * ==================================================
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

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
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

        this.cpf = cpf == null
                ? null
                : cpf.replaceAll("\\D", "");
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {

        this.cnpj = cnpj == null
                ? null
                : cnpj.replaceAll("\\D", "");
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

    public void setDataNascimentoInicio(
            LocalDateTime dataNascimentoInicio) {

        this.dataNascimentoInicio =
                dataNascimentoInicio;
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

    public void setEstadoCivil(
            EstadoCivilEnum estadoCivil) {

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

    public void setRegimeTributario(
            RegimeTributarioEnum regimeTributario) {

        this.regimeTributario =
                regimeTributario;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(
            LocalDateTime dataCadastro) {

        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(
            LocalDateTime dataAlteracao) {

        this.dataAlteracao = dataAlteracao;
    }


    /*
     * ==================================================
     * FILTROS
     * ==================================================
     */

    public String getFiltroNome() {
        return filtroNome;
    }

    public void setFiltroNome(String filtroNome) {

        this.filtroNome = filtroNome == null
                ? null
                : filtroNome.trim();
    }

    public String getFiltroCpf() {
        return filtroCpf;
    }

    public void setFiltroCpf(String filtroCpf) {

        this.filtroCpf = filtroCpf == null
                ? null
                : filtroCpf.replaceAll("\\D", "");
    }

    public String getFiltroCnpj() {
        return filtroCnpj;
    }

    public void setFiltroCnpj(String filtroCnpj) {

        this.filtroCnpj = filtroCnpj == null
                ? null
                : filtroCnpj.replaceAll("\\D", "");
    }

    public String getFiltroRazaoSocial() {
        return filtroRazaoSocial;
    }

    public void setFiltroRazaoSocial(
            String filtroRazaoSocial) {

        this.filtroRazaoSocial =
                filtroRazaoSocial == null
                        ? null
                        : filtroRazaoSocial.trim();
    }

    public String getFiltroCidade() {
        return filtroCidade;
    }

    public void setFiltroCidade(
            String filtroCidade) {

        this.filtroCidade =
                filtroCidade == null
                        ? null
                        : filtroCidade.trim();
    }

    public Integer getFiltroStatus() {
        return filtroStatus;
    }

    public void setFiltroStatus(
            Integer filtroStatus) {

        this.filtroStatus = filtroStatus;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {

        this.page =
                page == null || page < 0
                        ? 0
                        : page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getCnaeDescricao() {
        return cnaeDescricao;
    }

    public void setCnaeDescricao(String cnaeDescricao) {
        this.cnaeDescricao = cnaeDescricao;
    }

    public String getCnaeDivisao() {
        return cnaeDivisao;
    }

    public void setCnaeDivisao(String cnaeDivisao) {
        this.cnaeDivisao = cnaeDivisao;
    }

}