package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.EspecieMadeiraDTO;
import br.com.dpsistemas.cedroerp.mappers.EspecieMadeiraMapper;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.EspecieMadeira;
import br.com.dpsistemas.cedroerp.repositorys.EspecieMadeiraRepository;
import br.com.dpsistemas.cedroerp.specifications.EspecieMadeiraSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspecieMadeiraService {

    @Autowired
    private EspecieMadeiraRepository repository;

    @Autowired
    private EspecieMadeiraMapper mapper;

    @Autowired
    private EmpresaService empresaService; // Para buscar a empresa

    @Transactional(readOnly = true)
    public Page<EspecieMadeiraDTO> listar(EspecieMadeiraDTO filtro, Long idEmpresa) {
        Pageable pageable = PageRequest.of(filtro.getPage(), filtro.getSize(), Sort.by("nome").ascending());

        Specification<EspecieMadeira> spec = EspecieMadeiraSpecification.empresaIgual(idEmpresa)
                .and(EspecieMadeiraSpecification.nomeContem(filtro.getFiltroNome()))
                .and(EspecieMadeiraSpecification.statusIgual(converterStatus(filtro.getFiltroStatus())));

        return repository.findAll(spec, pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public EspecieMadeiraDTO buscarPorId(Long id, Long idEmpresa) {
        EspecieMadeira entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Espécie não encontrada."));
        return mapper.toDTO(entity);
    }

    @Transactional
    public EspecieMadeiraDTO salvar(EspecieMadeiraDTO dto, Long idEmpresa) {
        if (repository.existsByNomeIgnoreCaseAndEmpresa_Id(dto.getNome(), idEmpresa)) {
            throw new IllegalArgumentException("Já existe uma espécie cadastrada com esse nome.");
        }

        EspecieMadeira entity = mapper.toEntity(dto);
        Empresa empresa = empresaService.buscarPorId(idEmpresa);
        entity.setEmpresa(empresa);

        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public EspecieMadeiraDTO atualizar(Long id, EspecieMadeiraDTO dto, Long idEmpresa) {
        EspecieMadeira entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Espécie não encontrada."));

        if (repository.existsByNomeIgnoreCaseAndEmpresa_IdAndIdNot(dto.getNome(), idEmpresa, id)) {
            throw new IllegalArgumentException("Já existe outra espécie com esse nome.");
        }

        entity.setNome(dto.getNome());
        entity.setStatus(dto.getStatus());

        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public void alterarStatus(Long id, Long idEmpresa, Boolean status) {
        EspecieMadeira entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Espécie não encontrada."));
        entity.setStatus(status);
        repository.save(entity);
    }

    @Transactional
    public void excluir(Long id, Long idEmpresa) {
        EspecieMadeira entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Espécie não encontrada."));
        repository.delete(entity);
    }

    private Boolean converterStatus(Integer status) {
        if (status == null) return null;
        if (status == 0) return false;
        if (status == 1) return true;
        return null;
    }


}