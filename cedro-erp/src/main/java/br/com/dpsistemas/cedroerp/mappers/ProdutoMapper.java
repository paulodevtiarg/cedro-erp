package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.ProdutoDTO;
import br.com.dpsistemas.cedroerp.dtos.ProdutoMedidaDTO;
import br.com.dpsistemas.cedroerp.dtos.ProdutoVariacaoBaseDTO;
import br.com.dpsistemas.cedroerp.models.Produto;
import br.com.dpsistemas.cedroerp.models.ProdutoMedida;
import br.com.dpsistemas.cedroerp.models.ProdutoVariacaoBase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoMapper {

    // ==================================================
    // PRODUTO (O PAI)
    // ==================================================
    public ProdutoDTO toDTO(Produto entity) {
        if (entity == null) return null;

        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setDataAlteracao(entity.getDataAlteracao());
        dto.setImagem(entity.getImagem());

        // Dados achatados do Tipo e Espécie
        if (entity.getTipoProduto() != null) {
            dto.setTipoProdutoId(entity.getTipoProduto().getId());
            dto.setTipoProdutoNome(entity.getTipoProduto().getNome());
        }
        if (entity.getEspecieMadeira() != null) {
            dto.setEspecieMadeiraId(entity.getEspecieMadeira().getId());
            dto.setEspecieMadeiraNome(entity.getEspecieMadeira().getNome());
        }

        // Converte a lista de Variações Base (As Mães)
        if (entity.getVariacoesBase() != null) {
            List<ProdutoVariacaoBaseDTO> basesDto = entity.getVariacoesBase().stream()
                    .map(this::toVariacaoBaseDTO)
                    .collect(Collectors.toList());
            dto.setVariacoesBase(basesDto);
        }

        return dto;
    }

    public Produto toEntity(ProdutoDTO dto) {
        if (dto == null) return null;

        Produto entity = new Produto();
        entity.setId(dto.getId());
        entity.setStatus(dto.getStatus());
        entity.setImagem(dto.getImagem());

        // Os relacionamentos (Tipo, Espécie) são setados no Service
        return entity;
    }

    // ==================================================
    // PRODUTO VARIAÇÃO BASE (A MÃE)
    // ==================================================
    public ProdutoVariacaoBaseDTO toVariacaoBaseDTO(ProdutoVariacaoBase entity) {
        if (entity == null) return null;

        ProdutoVariacaoBaseDTO dto = new ProdutoVariacaoBaseDTO();
        dto.setId(entity.getId());
        dto.setLarguraCm(entity.getLarguraCm());
        dto.setEspessuraCm(entity.getEspessuraCm());

        if (entity.getUnidadeMedida() != null) {
            dto.setUnidadeMedidaId(entity.getUnidadeMedida().getId());
            dto.setUnidadeMedidaSigla(entity.getUnidadeMedida().getSigla());
        }

        // Converte a lista de Medidas (Os Filhos)
        if (entity.getMedidas() != null) {
            List<ProdutoMedidaDTO> medidasDto = entity.getMedidas().stream()
                    .map(this::toMedidaDTO)
                    .collect(Collectors.toList());
            dto.setMedidas(medidasDto);
        }

        return dto;
    }

    // ==================================================
    // PRODUTO MEDIDA (O FILHO)
    // ==================================================
    public ProdutoMedidaDTO toMedidaDTO(ProdutoMedida entity) {
        if (entity == null) return null;

        ProdutoMedidaDTO dto = new ProdutoMedidaDTO();
        dto.setId(entity.getId());
        dto.setComprimentoM(entity.getComprimentoM());
        dto.setSku(entity.getSku());
        dto.setPrecoVenda(entity.getPrecoVenda());
        dto.setQuantidadeEstoque(entity.getQuantidadeEstoque());
        dto.setStatus(entity.getStatus());

        return dto;
    }
}