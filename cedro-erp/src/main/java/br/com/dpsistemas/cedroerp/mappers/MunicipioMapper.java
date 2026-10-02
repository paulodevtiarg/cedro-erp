package br.com.dpsistemas.cedroerp.mappers;


import br.com.dpsistemas.cedroerp.dtos.MunicipioDTO;
import br.com.dpsistemas.cedroerp.dtos.MunicipioSelectDTO;
import br.com.dpsistemas.cedroerp.models.Municipio;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MunicipioMapper {

    public MunicipioDTO toDTO(Municipio entity) {
        if (entity == null) {
            return null;
        }
        return new MunicipioDTO(
                entity.getId(),
                entity.getCodigoIbge(),
                entity.getNome(),
                entity.getUf()
        );
    }

    public Municipio toEntity(MunicipioDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Municipio(
                dto.getId(),
                dto.getCodigoIbge(),
                dto.getNome(),
                dto.getUf()
        );
    }

    public MunicipioSelectDTO toSelectDTO(Municipio entity) {
        if (entity == null) {
            return null;
        }
        return new MunicipioSelectDTO(
                entity.getId(),
                entity.getCodigoIbge(),
                entity.getNome()
        );
    }

    public List<MunicipioDTO> toDTOList(List<Municipio> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MunicipioSelectDTO> toSelectDTOList(List<Municipio> list) {
        if (list == null) {
            return List.of();
        }
        return list.stream()
                .map(this::toSelectDTO)
                .collect(Collectors.toList());
    }
}