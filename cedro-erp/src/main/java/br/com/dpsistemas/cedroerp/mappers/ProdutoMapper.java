package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.ProdutoDTO;
import br.com.dpsistemas.cedroerp.dtos.ProdutoMedidaDTO;
import br.com.dpsistemas.cedroerp.models.Produto;
import br.com.dpsistemas.cedroerp.models.ProdutoMedida;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProdutoMapper {

    public ProdutoDTO toDTO(Produto entity) {
        if (entity == null) return null;

        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(entity.getId());

        // Dados achatados
        if (entity.getTipoProduto() != null) {
            dto.setTipoProdutoId(entity.getTipoProduto().getId());
            dto.setTipoProdutoNome(entity.getTipoProduto().getNome());
        }
        if (entity.getEspecieMadeira() != null) {
            dto.setEspecieMadeiraId(entity.getEspecieMadeira().getId());
            dto.setEspecieMadeiraNome(entity.getEspecieMadeira().getNome());
        }
        if (entity.getUnidadeMedida() != null) {
            dto.setUnidadeMedidaId(entity.getUnidadeMedida().getId());
            dto.setUnidadeMedidaSigla(entity.getUnidadeMedida().getSigla());
        }

        dto.setLarguraCm(entity.getLarguraCm());
        dto.setEspessuraCm(entity.getEspessuraCm());
        dto.setStatus(entity.getStatus());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setDataAlteracao(entity.getDataAlteracao());

        dto.setImagem(entity.getImagem());

        // Converte a lista de Medidas (se existir)
        if (entity.getMedidas() != null) {
            List<ProdutoMedidaDTO> medidasDto = entity.getMedidas().stream()
                    .map(this::toMedidaDTO)
                    .collect(Collectors.toList());
            dto.setMedidas(medidasDto);
        }

        return dto;
    }

    public Produto toEntity(ProdutoDTO dto) {
        if (dto == null) return null;

        Produto entity = new Produto();
        entity.setId(dto.getId());
        entity.setLarguraCm(dto.getLarguraCm());
        entity.setEspessuraCm(dto.getEspessuraCm());
        entity.setStatus(dto.getStatus());

        // Os relacionamentos (Tipo, Espécie, Unidade) não são setados aqui.
        // O Service vai buscar no banco e setar na Entity.
        return entity;
    }

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