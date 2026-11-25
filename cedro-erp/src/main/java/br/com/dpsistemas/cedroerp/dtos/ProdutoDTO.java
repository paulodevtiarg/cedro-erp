package br.com.dpsistemas.cedroerp.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDTO {

    private Long id;

    // Relacionamentos (IDs para enviar do form)
    private Long tipoProdutoId;
    private Long especieMadeiraId;
    private Long unidadeMedidaId;

    // Dados "achatados" (para exibir na tela sem JOINs)
    private String tipoProdutoNome;
    private String especieMadeiraNome;
    private String unidadeMedidaSigla;

    private BigDecimal larguraCm;
    private BigDecimal espessuraCm;
    private Boolean status = true;

    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;


    private String imagem;

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    // FILTROS
    private String filtroNome; // Vamos usar para buscar pelo nome da espécie ou tipo
    private String filtroEspecie;
    private String filtroTipo;
    private Integer filtroStatus = 1;
    private Integer page = 0;
    private Integer size = 10;

    // Dentro do ProdutoDTO:
    private List<ProdutoMedidaDTO> medidas = new ArrayList<>();

    // Getters e Setters (Vou resumir aqui para o código não ficar gigante, mas você gera todos no IDE)
    // ... Lembre-se de criar os Getters e Setters para todos os campos acima

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTipoProdutoId() {
        return tipoProdutoId;
    }

    public void setTipoProdutoId(Long tipoProdutoId) {
        this.tipoProdutoId = tipoProdutoId;
    }

    public Long getEspecieMadeiraId() {
        return especieMadeiraId;
    }

    public void setEspecieMadeiraId(Long especieMadeiraId) {
        this.especieMadeiraId = especieMadeiraId;
    }

    public Long getUnidadeMedidaId() {
        return unidadeMedidaId;
    }

    public void setUnidadeMedidaId(Long unidadeMedidaId) {
        this.unidadeMedidaId = unidadeMedidaId;
    }

    public String getTipoProdutoNome() {
        return tipoProdutoNome;
    }

    public void setTipoProdutoNome(String tipoProdutoNome) {
        this.tipoProdutoNome = tipoProdutoNome;
    }

    public String getEspecieMadeiraNome() {
        return especieMadeiraNome;
    }

    public void setEspecieMadeiraNome(String especieMadeiraNome) {
        this.especieMadeiraNome = especieMadeiraNome;
    }

    public String getUnidadeMedidaSigla() {
        return unidadeMedidaSigla;
    }

    public void setUnidadeMedidaSigla(String unidadeMedidaSigla) {
        this.unidadeMedidaSigla = unidadeMedidaSigla;
    }

    public BigDecimal getLarguraCm() {
        return larguraCm;
    }

    public void setLarguraCm(BigDecimal larguraCm) {
        this.larguraCm = larguraCm;
    }

    public BigDecimal getEspessuraCm() {
        return espessuraCm;
    }

    public void setEspessuraCm(BigDecimal espessuraCm) {
        this.espessuraCm = espessuraCm;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getFiltroNome() {
        return filtroNome;
    }

    public void setFiltroNome(String filtroNome) {
        this.filtroNome = filtroNome;
    }

    public String getFiltroEspecie() {
        return filtroEspecie;
    }

    public void setFiltroEspecie(String filtroEspecie) {
        this.filtroEspecie = filtroEspecie;
    }

    public String getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(String filtroTipo) {
        this.filtroTipo = filtroTipo;
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

    public List<ProdutoMedidaDTO> getMedidas() {
        return medidas;
    }

    public void setMedidas(List<ProdutoMedidaDTO> medidas) {
        this.medidas = medidas;
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

    public String getDataCadastroFormatada() {
        if (dataCadastro == null) return "";
        return dataCadastro.format(FORMATO_DATA_HORA);
    }

    public String getDataAlteracaoFormatada() {
        if (dataAlteracao == null) return "";
        return dataAlteracao.format(FORMATO_DATA_HORA);
    }

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
}