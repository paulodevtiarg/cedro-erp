package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.ProdutoDTO;
import br.com.dpsistemas.cedroerp.dtos.ProdutoMedidaDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Transactional(readOnly = true)
    public Page<ProdutoDTO> listar(ProdutoDTO filtro, Long idEmpresa) {
        Pageable pageable = PageRequest.of(filtro.getPage(), filtro.getSize(), Sort.by("id").descending());

        Specification<Produto> spec = ProdutoSpecification.empresaIgual(idEmpresa)
                .and(ProdutoSpecification.especieContem(filtro.getFiltroEspecie()))
                .and(ProdutoSpecification.tipoContem(filtro.getFiltroTipo()))
                .and(ProdutoSpecification.statusIgual(converterStatus(filtro.getFiltroStatus())));

        return produtoRepository.findAll(spec, pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id, Long idEmpresa) {
        Produto entity = produtoRepository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
        return mapper.toDTO(entity);
    }

    @Transactional
    public ProdutoDTO salvar(ProdutoDTO dto, Long idEmpresa) {
        validarDuplicidade(dto, idEmpresa, null);

        Produto entity = mapper.toEntity(dto);
        Empresa empresa = empresaService.buscarPorId(idEmpresa);
        entity.setEmpresa(empresa);

        // Seta os relacionamentos buscando no banco
        entity.setTipoProduto(tipoProdutoRepository.findById(dto.getTipoProdutoId()).orElseThrow());
        entity.setEspecieMadeira(especieRepository.findById(dto.getEspecieMadeiraId()).orElseThrow());
        entity.setUnidadeMedida(unidadeRepository.findById(dto.getUnidadeMedidaId()).orElseThrow());

        // Processa as Medidas
        // Processa as Medidas
        if (dto.getMedidas() != null && !dto.getMedidas().isEmpty()) {
            List<ProdutoMedida> medidas = new ArrayList<>();
            List<String> skusGerados = new ArrayList<>(); // Para validar duplicidade na mesma tela

            for (ProdutoMedidaDTO mDto : dto.getMedidas()) {
                ProdutoMedida medida = new ProdutoMedida();
                medida.setComprimentoM(mDto.getComprimentoM());
                medida.setPrecoVenda(mDto.getPrecoVenda());
                medida.setQuantidadeEstoque(mDto.getQuantidadeEstoque());
                medida.setStatus(mDto.getStatus() != null ? mDto.getStatus() : true);
                medida.setEmpresa(empresa);
                medida.setProduto(entity);

                // 🔥 REGRA DO SKU: Usuário digitou? Usa o dele. Tá em branco? Gera automático.
                String skuFinal;
                if (mDto.getSku() == null || mDto.getSku().trim().isBlank()) {
                    skuFinal = gerarSku(entity, medida);
                } else {
                    skuFinal = mDto.getSku().trim().toUpperCase();
                }

                // Valida se o SKU já não foi usado na linha de cima do mesmo formulário
                if (skusGerados.contains(skuFinal)) {
                    throw new IllegalArgumentException("O SKU '" + skuFinal + "' está duplicado nos tamanhos informados. Verifique o preenchimento.");
                }

                skusGerados.add(skuFinal);
                medida.setSku(skuFinal);
                medidas.add(medida);
            }
            entity.setMedidas(medidas);
        }

        return mapper.toDTO(produtoRepository.save(entity));
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoDTO dto, Long idEmpresa) {
        Produto entity = produtoRepository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        validarDuplicidade(dto, idEmpresa, id);

        // Atualiza os dados do Produto
        entity.setLarguraCm(dto.getLarguraCm());
        entity.setEspessuraCm(dto.getEspessuraCm());
        entity.setStatus(dto.getStatus());

        entity.setTipoProduto(tipoProdutoRepository.findById(dto.getTipoProdutoId()).orElseThrow());
        entity.setEspecieMadeira(especieRepository.findById(dto.getEspecieMadeiraId()).orElseThrow());
        entity.setUnidadeMedida(unidadeRepository.findById(dto.getUnidadeMedidaId()).orElseThrow());

        // ========================================================
        // 🔥 CORREÇÃO: Atualiza as Medidas existentes em vez de recriar
        // ========================================================
        if (dto.getMedidas() != null) {
            List<String> skusGerados = new ArrayList<>();

            // Mapeia as medidas que já existem no banco por ID
            Map<Long, ProdutoMedida> medidasExistentes = entity.getMedidas().stream()
                    .collect(Collectors.toMap(ProdutoMedida::getId, m -> m));

            List<ProdutoMedida> novasMedidasList = new ArrayList<>();

            for (ProdutoMedidaDTO mDto : dto.getMedidas()) {
                ProdutoMedida medida;

                // Se a medida veio com ID, significa que ela já existe no banco
                if (mDto.getId() != null && medidasExistentes.containsKey(mDto.getId())) {
                    medida = medidasExistentes.get(mDto.getId());
                } else {
                    // Se não tem ID, é uma nova medida sendo adicionada na tela
                    medida = new ProdutoMedida();
                    medida.setEmpresa(entity.getEmpresa());
                    medida.setProduto(entity);
                }

                medida.setComprimentoM(mDto.getComprimentoM());
                medida.setPrecoVenda(mDto.getPrecoVenda());
                medida.setQuantidadeEstoque(mDto.getQuantidadeEstoque());
                medida.setStatus(mDto.getStatus() != null ? mDto.getStatus() : true);

                // Regra do SKU
                String skuFinal;
                if (mDto.getSku() == null || mDto.getSku().trim().isBlank()) {
                    skuFinal = gerarSku(entity, medida);
                } else {
                    skuFinal = mDto.getSku().trim().toUpperCase();
                }

                // Validação de SKU duplicado
                if (skusGerados.contains(skuFinal)) {
                    throw new IllegalArgumentException("O SKU '" + skuFinal + "' está duplicado nos tamanhos informados.");
                }
                skusGerados.add(skuFinal);
                medida.setSku(skuFinal);

                novasMedidasList.add(medida);
            }

            // Remove da lista apenas as medidas que o usuário deletou na tela
            entity.getMedidas().removeIf(m -> !novasMedidasList.contains(m));

            // Adiciona as novas (se houver)
            for (ProdutoMedida m : novasMedidasList) {
                if (!entity.getMedidas().contains(m)) {
                    entity.getMedidas().add(m);
                }
            }
        }

        return mapper.toDTO(produtoRepository.save(entity));
    }

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
            existe = produtoRepository.existsByTipoProduto_IdAndEspecieMadeira_IdAndLarguraCmAndEspessuraCmAndEmpresa_Id(
                    dto.getTipoProdutoId(), dto.getEspecieMadeiraId(), dto.getLarguraCm(), dto.getEspessuraCm(), idEmpresa);
        } else {
            existe = produtoRepository.existsByTipoProduto_IdAndEspecieMadeira_IdAndLarguraCmAndEspessuraCmAndEmpresa_IdAndIdNot(
                    dto.getTipoProdutoId(), dto.getEspecieMadeiraId(), dto.getLarguraCm(), dto.getEspessuraCm(), idEmpresa, idIgnorar);
        }
        if (existe) {
            throw new IllegalArgumentException("Já existe um produto com essa combinação de Tipo, Espécie, Largura e Espessura.");
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
        produtoRepository.delete(entity);
    }

    private Boolean converterStatus(Integer status) {
        if (status == null) return null;
        if (status == 0) return false;
        if (status == 1) return true;
        return null;
    }

    private String gerarSku(Produto produto, ProdutoMedida medida) {
        // 1. Pega as iniciais do Tipo e da Espécie
        String tipo = produto.getTipoProduto().getNome().replaceAll("[^a-zA-Z]", "").toUpperCase();
        String especie = produto.getEspecieMadeira().getNome().replaceAll("[^a-zA-Z]", "").toUpperCase();

        // Garante que tem pelo menos 3 letras, se não tiver, usa a palavra inteira
        tipo = tipo.length() >= 3 ? tipo.substring(0, 3) : tipo;
        especie = especie.length() >= 3 ? especie.substring(0, 3) : especie;

        // 2. Formata as dimensões (Remove os ".0" desnecessários)
        String largura = produto.getLarguraCm().stripTrailingZeros().toPlainString();
        String espessura = produto.getEspessuraCm().stripTrailingZeros().toPlainString();

        // 3. Formata o comprimento (ou M3 se for nulo)
        String comprimento = (medida.getComprimentoM() != null)
                ? medida.getComprimentoM().stripTrailingZeros().toPlainString()
                : "M3";

        // 4. Monta o SKU: EX: TAB-TAI-2X30-3.0
        return String.format("%s-%s-%sX%s-%s", tipo, especie, largura, espessura, comprimento).toUpperCase();
    }
}