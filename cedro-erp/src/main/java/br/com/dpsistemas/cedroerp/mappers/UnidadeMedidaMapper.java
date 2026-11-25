package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.UnidadeMedidaDTO;
import br.com.dpsistemas.cedroerp.models.UnidadeMedida;
import org.springframework.stereotype.Component;

@Component
public class UnidadeMedidaMapper {

    public UnidadeMedidaDTO toDTO(UnidadeMedida entity) {
        if (entity == null) return null;

        UnidadeMedidaDTO dto = new UnidadeMedidaDTO();
        dto.setId(entity.getId());
        dto.setSigla(entity.getSigla());
        dto.setDescricao(entity.getDescricao());
        dto.setStatus(entity.getStatus());

        dto.setDataCadastro(entity.getDataCadastro());
        dto.setDataAlteracao(entity.getDataAlteracao());

        return dto;
    }

    public UnidadeMedida toEntity(UnidadeMedidaDTO dto) {
        if (dto == null) return null;

        UnidadeMedida entity = new UnidadeMedida();
        entity.setId(dto.getId());
        entity.setSigla(dto.getSigla());
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}