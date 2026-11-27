package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.ProdutoDTO;
import br.com.dpsistemas.cedroerp.dtos.ProdutoMedidaDTO;
import br.com.dpsistemas.cedroerp.dtos.ProdutoVariacaoBaseDTO;
import br.com.dpsistemas.cedroerp.mappers.ProdutoMapper;
import br.com.dpsistemas.cedroerp.models.*;
import br.com.dpsistemas.cedroerp.repositorys.*;
import br.com.dpsistemas.cedroerp.specifications.ProdutoSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private TipoProdutoRepository tipoProdutoRepository;
    @Autowired private EspecieMadeiraRepository especieRepository;
    @Autowired private UnidadeMedidaRepository unidadeRepository;
    @Autowired private ProdutoMapper mapper;
    @Autowired private EmpresaService empresaService;
    @Autowired private ProdutoMedidaRepository produtoMedidaRepository;


    // ==================================================
    // LISTAR E BUSCAR
    // ==================================================
    @Transactional(readOnly = true)
    public Page<ProdutoDTO> listar(ProdutoDTO filtro, Long idEmpresa) {
        Pageable pageable = PageRequest.of(filtro.getPage(), filtro.getSize(), Sort.by("id").descending());

        Specification<Produto> spec = ProdutoSpecification.empresaIgual(idEmpresa)
                .and(ProdutoSpecification.especieContem(filtro.getFiltroEspecie()))
                .and(ProdutoSpecification.tipoContem(filtro.getFiltroTipo()))
                .and(ProdutoSpecification.classificacaoIgual(filtro.getFiltroClassificacao()))
                .and(ProdutoSpecification.statusIgual(converterStatus(filtro.getFiltroStatus())));

        return produtoRepository.findAll(spec, pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id, Long idEmpresa) {
        Produto entity = produtoRepository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        return mapper.toDTO(entity);
    }

    // ==================================================
    // SALVAR (NOVO)
    // ==================================================
    @Transactional
    public ProdutoDTO salvar(ProdutoDTO dto, Long idEmpresa) {
        validarDuplicidade(dto, idEmpresa, null);

        Produto entity = mapper.toEntity(dto);
        Empresa empresa = empresaService.buscarPorId(idEmpresa);
        entity.setEmpresa(empresa);

        entity.setTipoProduto(tipoProdutoRepository.findById(dto.getTipoProdutoId()).orElseThrow());
        entity.setEspecieMadeira(especieRepository.findById(dto.getEspecieMadeiraId()).orElseThrow());

        if (dto.getVariacoesBase() != null && !dto.getVariacoesBase().isEmpty()) {
            List<ProdutoVariacaoBase> bases = new ArrayList<>();

            for (ProdutoVariacaoBaseDTO baseDto : dto.getVariacoesBase()) {
                ProdutoVariacaoBase base = new ProdutoVariacaoBase();
                base.setLarguraCm(baseDto.getLarguraCm());
                base.setEspessuraCm(baseDto.getEspessuraCm());
                base.setEmpresa(empresa);
                base.setProduto(entity);

                UnidadeMedida unidade = unidadeRepository.findById(baseDto.getUnidadeMedidaId())
                        .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada."));
                base.setUnidadeMedida(unidade);

                List<ProdutoMedida> medidas = new ArrayList<>();
                List<String> skusGerados = new ArrayList<>();

                if (baseDto.getMedidas() != null) {
                    for (ProdutoMedidaDTO medidaDto : baseDto.getMedidas()) {
                        ProdutoMedida medida = new ProdutoMedida();
                        medida.setComprimentoM(medidaDto.getComprimentoM());
                        medida.setPrecoVenda(medidaDto.getPrecoVenda());

                        // 🔥 PRODUTO NOVO: Aceita o estoque digitado
                        medida.setQuantidadeEstoque(medidaDto.getQuantidadeEstoque() != null ? medidaDto.getQuantidadeEstoque() : BigDecimal.ZERO);

                        medida.setStatus(medidaDto.getStatus() != null ? medidaDto.getStatus() : true);
                        medida.setEmpresa(empresa);
                        medida.setVariacaoBase(base);

                        String skuFinal = (medidaDto.getSku() == null || medidaDto.getSku().trim().isBlank())
                                ? gerarSku(entity, base, medida)
                                : medidaDto.getSku().trim().toUpperCase();

                        if (skusGerados.contains(skuFinal)) {
                            throw new IllegalArgumentException("O SKU '" + skuFinal + "' está duplicado.");
                        }
                        skusGerados.add(skuFinal);
                        medida.setSku(skuFinal);
                        medidas.add(medida);
                    }
                }
                base.setMedidas(medidas);
                bases.add(base);
            }
            entity.setVariacoesBase(bases);
        }

        return mapper.toDTO(produtoRepository.save(entity));
    }

    // ==================================================
    // ATUALIZAR
    // ==================================================
    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto, Long idEmpresa) {
        Produto entity = produtoRepository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        validarDuplicidade(dto, idEmpresa, id);

        entity.setStatus(dto.getStatus());
        entity.setImagem(dto.getImagem());
        entity.setTipoProduto(tipoProdutoRepository.findById(dto.getTipoProdutoId()).orElseThrow());
        entity.setEspecieMadeira(especieRepository.findById(dto.getEspecieMadeiraId()).orElseThrow());

        if (dto.getVariacoesBase() != null) {
            Map<Long, ProdutoVariacaoBase> basesExistentes = entity.getVariacoesBase().stream()
                    .collect(Collectors.toMap(ProdutoVariacaoBase::getId, b -> b));

            List<ProdutoVariacaoBase> novasBasesList = new ArrayList<>();

            for (ProdutoVariacaoBaseDTO baseDto : dto.getVariacoesBase()) {
                ProdutoVariacaoBase base;
                if (baseDto.getId() != null && basesExistentes.containsKey(baseDto.getId())) {
                    base = basesExistentes.get(baseDto.getId());
                } else {
                    base = new ProdutoVariacaoBase();
                    base.setEmpresa(entity.getEmpresa());
                    base.setProduto(entity);
                }

                base.setLarguraCm(baseDto.getLarguraCm());
                base.setEspessuraCm(baseDto.getEspessuraCm());
                base.setUnidadeMedida(unidadeRepository.findById(baseDto.getUnidadeMedidaId()).orElseThrow());

                Map<Long, ProdutoMedida> medidasExistentes = base.getMedidas().stream()
                        .collect(Collectors.toMap(ProdutoMedida::getId, m -> m));

                List<ProdutoMedida> novasMedidasList = new ArrayList<>();
                List<String> skusGerados = new ArrayList<>();

                if (baseDto.getMedidas() != null) {
                    for (ProdutoMedidaDTO medidaDto : baseDto.getMedidas()) {
                        ProdutoMedida medida;
                        boolean isNovaMedida = false;

                        if (medidaDto.getId() != null && medidasExistentes.containsKey(medidaDto.getId())) {
                            medida = medidasExistentes.get(medidaDto.getId());
                        } else {
                            medida = new ProdutoMedida();
                            medida.setEmpresa(entity.getEmpresa());
                            medida.setVariacaoBase(base);
                            isNovaMedida = true; // Marcamos que é nova
                        }

                        medida.setComprimentoM(medidaDto.getComprimentoM());
                        medida.setPrecoVenda(medidaDto.getPrecoVenda());
                        medida.setStatus(medidaDto.getStatus() != null ? medidaDto.getStatus() : true);

                        // 🔥 AQUI ESTÁ A REGRA DE ESTOQUE:
                        // Se for uma nova medida, começa com 0. Se for edição de uma existente,
                        // o objeto já veio do banco com o estoque correto e NÃO alteramos.
                        if (isNovaMedida) {
                            medida.setQuantidadeEstoque(BigDecimal.ZERO);
                        }

                        String skuFinal = (medidaDto.getSku() == null || medidaDto.getSku().trim().isBlank())
                                ? gerarSku(entity, base, medida)
                                : medidaDto.getSku().trim().toUpperCase();

                        if (skusGerados.contains(skuFinal)) {
                            throw new IllegalArgumentException("O SKU '" + skuFinal + "' está duplicado.");
                        }
                        skusGerados.add(skuFinal);
                        medida.setSku(skuFinal);

                        novasMedidasList.add(medida);
                    }
                }

                base.getMedidas().removeIf(m -> !novasMedidasList.contains(m));
                for (ProdutoMedida m : novasMedidasList) {
                    if (!base.getMedidas().contains(m)) base.getMedidas().add(m);
                }
                novasBasesList.add(base);
            }

            entity.getVariacoesBase().removeIf(b -> !novasBasesList.contains(b));
            for (ProdutoVariacaoBase b : novasBasesList) {
                if (!entity.getVariacoesBase().contains(b)) entity.getVariacoesBase().add(b);
            }
        }

        return mapper.toDTO(produtoRepository.save(entity));
    }

    // ==================================================
    // MÉTODOS AUXILIARES
    // ==================================================
    @Transactional
    public void atualizarImagem(Long idProduto, Long idEmpresa, String urlImagem) {
        Produto produto = produtoRepository.findByIdAndEmpresa_Id(idProduto, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        produto.setImagem(urlImagem);
        produtoRepository.save(produto);
    }

    private void validarDuplicidade(ProdutoDTO dto, Long idEmpresa, Long idIgnorar) {
        boolean existe;
        if (idIgnorar == null) {
            existe = produtoRepository.existsByTipoProduto_IdAndEspecieMadeira_IdAndEmpresa_Id(
                    dto.getTipoProdutoId(), dto.getEspecieMadeiraId(), idEmpresa);
        } else {
            existe = produtoRepository.existsByTipoProduto_IdAndEspecieMadeira_IdAndEmpresa_IdAndIdNot(
                    dto.getTipoProdutoId(), dto.getEspecieMadeiraId(), idEmpresa, idIgnorar);
        }
        if (existe) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com esse Tipo e Espécie.");
        }
    }

    @Transactional
    public void alterarStatus(Long id, Long idEmpresa, Boolean status) {
        Produto entity = produtoRepository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        entity.setStatus(status);
        produtoRepository.save(entity);
    }

    @Transactional
    public void excluir(Long id, Long idEmpresa) {
        Produto entity = produtoRepository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        produtoRepository.delete(entity); // O cascade apagará Bases e Medidas
    }

    private Boolean converterStatus(Integer status) {
        if (status == null) return null;
        if (status == 0) return false;
        if (status == 1) return true;
        return null;
    }

    private String gerarSku(Produto produto, ProdutoVariacaoBase base, ProdutoMedida medida) {
        String tipo = produto.getTipoProduto().getNome().replaceAll("[^a-zA-Z]", "").toUpperCase();
        String especie = produto.getEspecieMadeira().getNome().replaceAll("[^a-zA-Z]", "").toUpperCase();
        tipo = tipo.length() >= 3 ? tipo.substring(0, 3) : tipo;
        especie = especie.length() >= 3 ? especie.substring(0, 3) : especie;

        String largura = base.getLarguraCm().stripTrailingZeros().toPlainString();
        String espessura = base.getEspessuraCm().stripTrailingZeros().toPlainString();
        String comprimento = (medida.getComprimentoM() != null)
                ? medida.getComprimentoM().stripTrailingZeros().toPlainString()
                : "M3";

        return String.format("%s-%s-%sX%s-%s", tipo, especie, largura, espessura, comprimento).toUpperCase();
    }

    public Map<String, Map<String, BigDecimal>> obterResumoEstoque(Long idEmpresa) {
        List<Object[]> resultados = produtoMedidaRepository.somarEstoquePorClassificacaoEUnidade(idEmpresa);

        // Estrutura: { "MADEIRA_SERRADA": { "M3": 150.0, "UN": 50.0 }, "MADEIRA_BRUTA": { ... } }
        Map<String, Map<String, BigDecimal>> resumo = new HashMap<>();

        // Inicializa as chaves para não dar null no HTML
        resumo.put("MADEIRA_SERRADA", new HashMap<>());
        resumo.put("MADEIRA_BRUTA", new HashMap<>());

        for (Object[] row : resultados) {
            String classificacao = row[0].toString(); // Ex: "MADEIRA_SERRADA"
            String unidade = row[1].toString();       // Ex: "M3"
            BigDecimal quantidade = (BigDecimal) row[2];

            resumo.get(classificacao).put(unidade, quantidade);
        }

        return resumo;
    }


    public Map<String, List<Map<String, Object>>> obterTopProdutosEstoque(Long idEmpresa) {
        List<Object[]> resultados = produtoMedidaRepository.listarTopProdutosEstoque(idEmpresa);

        List<Map<String, Object>> listaBruta = new ArrayList<>();
        List<Map<String, Object>> listaSerrada = new ArrayList<>();

        BigDecimal maxBruta = BigDecimal.ZERO;
        BigDecimal maxSerrada = BigDecimal.ZERO;

        for (Object[] row : resultados) {
            String tipo = row[0].toString();
            String especie = row[1].toString();
            String classificacao = row[2].toString();
            String unidade = row[3].toString();
            BigDecimal qtd = (BigDecimal) row[4];

            // Note que aqui nós colocamos os nomes das chaves com Strings ("nome", "quantidade", etc.)
            Map<String, Object> item = new HashMap<>();
            item.put("nome", tipo + " de " + especie);
            item.put("quantidade", qtd);
            item.put("unidade", unidade);

            if ("MADEIRA_BRUTA".equals(classificacao)) {
                listaBruta.add(item);
                if (qtd.compareTo(maxBruta) > 0) maxBruta = qtd;
            } else if ("MADEIRA_SERRADA".equals(classificacao)) {
                listaSerrada.add(item);
                if (qtd.compareTo(maxSerrada) > 0) maxSerrada = qtd;
            }
        }

        listaBruta.sort((a, b) -> ((BigDecimal)b.get("quantidade")).compareTo((BigDecimal)a.get("quantidade")));
        listaSerrada.sort((a, b) -> ((BigDecimal)b.get("quantidade")).compareTo((BigDecimal)a.get("quantidade")));

        if (listaBruta.size() > 5) listaBruta = listaBruta.subList(0, 5);
        if (listaSerrada.size() > 5) listaSerrada = listaSerrada.subList(0, 5);

        // 🔥 AQUI É ONDE O "null%" ERA GERADO. A chave precisa ser EXATAMENTE "porcentagem".
        for (Map<String, Object> item : listaBruta) {
            BigDecimal qtd = (BigDecimal) item.get("quantidade");
            int porc = maxBruta.compareTo(BigDecimal.ZERO) > 0
                    ? qtd.multiply(new BigDecimal(100)).divide(maxBruta, 0, RoundingMode.HALF_UP).intValue() : 0;
            item.put("porcentagem", porc);
        }

        for (Map<String, Object> item : listaSerrada) {
            BigDecimal qtd = (BigDecimal) item.get("quantidade");
            int porc = maxSerrada.compareTo(BigDecimal.ZERO) > 0
                    ? qtd.multiply(new BigDecimal(100)).divide(maxSerrada, 0, RoundingMode.HALF_UP).intValue() : 0;
            item.put("porcentagem", porc);
        }

        Map<String, List<Map<String, Object>>> retorno = new HashMap<>();
        retorno.put("BRUTA", listaBruta);
        retorno.put("SERRADA", listaSerrada);
        return retorno;
    }

    public List<ProdutoDTO> listarTodosParaSelect(Long idEmpresa) {
        // Aqui você pode buscar todos os produtos ativos da empresa (sem paginação)
        // e retornar a lista. Se for muito grande, o ideal é usar um AJAX de autocomplete.
        return produtoRepository.findAllByEmpresa_IdAndStatusTrue(idEmpresa)
                .stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public Map<String, List<ProdutoMedida>> obterMedidasAgrupadasPorProduto(Long idEmpresa) {
        List<ProdutoMedida> medidas = produtoMedidaRepository.buscarMedidasParaSelect(idEmpresa);

        // Agrupa pelo nome do Produto (Tipo + Espécie + Largura x Espessura)
        Map<String, List<ProdutoMedida>> agrupado = new LinkedHashMap<>();

        for (ProdutoMedida m : medidas) {
            var base = m.getVariacaoBase();
            var produto = base.getProduto();

            String nomeAgrupamento = produto.getTipoProduto().getNome() + " de " +
                    produto.getEspecieMadeira().getNome() + " (" +
                    base.getLarguraCm() + "x" + base.getEspessuraCm() + " cm)";

            agrupado.computeIfAbsent(nomeAgrupamento, k -> new ArrayList<>()).add(m);
        }

        return agrupado;
    }

}