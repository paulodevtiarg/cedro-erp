package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.MovimentacaoEstoqueDTO;
import br.com.dpsistemas.cedroerp.models.MovimentacaoEstoque;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoEstoqueMapper {

    public MovimentacaoEstoqueDTO toDTO(MovimentacaoEstoque entity) {
        if (entity == null) return null;

        MovimentacaoEstoqueDTO dto = new MovimentacaoEstoqueDTO();
        dto.setId(entity.getId());
        dto.setTipoMovimentacao(entity.getTipoMovimentacao());
        dto.setQuantidade(entity.getQuantidade());
        dto.setMotivo(entity.getMotivo());
        dto.setDataMovimentacao(entity.getDataMovimentacao());

        if (entity.getProdutoMedida() != null) {
            dto.setProdutoMedidaId(entity.getProdutoMedida().getId());
            dto.setSku(entity.getProdutoMedida().getSku());

            if (entity.getProdutoMedida().getVariacaoBase() != null) {
                var base = entity.getProdutoMedida().getVariacaoBase();
                dto.setUnidadeSigla(base.getUnidadeMedida().getSigla());

                if (base.getProduto() != null) {
                    String nome = base.getProduto().getTipoProduto().getNome() + " de " + base.getProduto().getEspecieMadeira().getNome();
                    dto.setProdutoNome(nome + " (" + base.getLarguraCm() + "x" + base.getEspessuraCm() + ")");
                }
            }
        }
        return dto;
    }
}