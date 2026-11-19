package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.EspecieMadeiraDTO;
import br.com.dpsistemas.cedroerp.models.EspecieMadeira;
import org.springframework.stereotype.Component;

@Component
public class EspecieMadeiraMapper {

    public EspecieMadeiraDTO toDTO(EspecieMadeira entity) {
        if (entity == null) return null;
        EspecieMadeiraDTO dto = new EspecieMadeiraDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setStatus(entity.getStatus());

        // Copiando as datas de auditoria
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setDataAlteracao(entity.getDataAlteracao());

        return dto;
    }

    public EspecieMadeira toEntity(EspecieMadeiraDTO dto) {
        if (dto == null) return null;
        EspecieMadeira entity = new EspecieMadeira();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}