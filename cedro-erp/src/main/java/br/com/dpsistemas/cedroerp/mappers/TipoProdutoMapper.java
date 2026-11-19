package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.TipoProdutoDTO;
import br.com.dpsistemas.cedroerp.models.TipoProduto;
import org.springframework.stereotype.Component;

@Component
public class TipoProdutoMapper {

    public TipoProdutoDTO toDTO(TipoProduto entity) {
        if (entity == null) return null;

        TipoProdutoDTO dto = new TipoProdutoDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setStatus(entity.getStatus());

        // Copiando as datas de auditoria
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setDataAlteracao(entity.getDataAlteracao());

        return dto;
    }

    public TipoProduto toEntity(TipoProdutoDTO dto) {
        if (dto == null) return null;

        TipoProduto entity = new TipoProduto();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}