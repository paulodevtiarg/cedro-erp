package br.com.dpsistemas.cedroerp.services;


import br.com.dpsistemas.cedroerp.dtos.MunicipioDTO;
import br.com.dpsistemas.cedroerp.dtos.MunicipioSelectDTO;
import br.com.dpsistemas.cedroerp.mappers.MunicipioMapper;
import br.com.dpsistemas.cedroerp.models.Municipio;
import br.com.dpsistemas.cedroerp.repositorys.MunicipioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MunicipioService {

    private final MunicipioRepository municipioRepository;
    private final MunicipioMapper municipioMapper;

    public MunicipioService(MunicipioRepository municipioRepository, MunicipioMapper municipioMapper) {
        this.municipioRepository = municipioRepository;
        this.municipioMapper = municipioMapper;
    }

    @Transactional(readOnly = true)
    public List<MunicipioSelectDTO> buscarPorUf(String uf) {
        if (uf == null || uf.trim().isEmpty()) {
            return List.of();
        }
        List<Municipio> municipios = municipioRepository.findByUfOrderByNomeAsc(uf.toUpperCase().trim());
        return municipioMapper.toSelectDTOList(municipios);
    }

    @Transactional(readOnly = true)
    public Optional<MunicipioDTO> buscarPorCodigoIbge(String codigoIbge) {
        if (codigoIbge == null || codigoIbge.trim().isEmpty()) {
            return Optional.empty();
        }
        return municipioRepository.findByCodigoIbge(codigoIbge.trim())
                .map(municipioMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<MunicipioDTO> buscarPorNomeEUf(String nome, String uf) {
        if (nome == null || uf == null) {
            return Optional.empty();
        }
        return municipioRepository.findByNomeIgnoreCaseAndUfIgnoreCase(nome.trim(), uf.trim())
                .map(municipioMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<MunicipioDTO> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return municipioRepository.findById(id)
                .map(municipioMapper::toDTO);
    }
}
