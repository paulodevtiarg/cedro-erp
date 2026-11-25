package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.UnidadeMedidaDTO;
import br.com.dpsistemas.cedroerp.mappers.UnidadeMedidaMapper;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.UnidadeMedida;
import br.com.dpsistemas.cedroerp.repositorys.UnidadeMedidaRepository;
import br.com.dpsistemas.cedroerp.utils.UnidadeMedidaSpecification;
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
public class UnidadeMedidaService {

    @Autowired private UnidadeMedidaRepository repository;
    @Autowired private UnidadeMedidaMapper mapper;
    @Autowired private EmpresaService empresaService;

    @Transactional(readOnly = true)
    public Page<UnidadeMedidaDTO> listar(UnidadeMedidaDTO filtro, Long idEmpresa) {
        Pageable pageable = PageRequest.of(filtro.getPage(), filtro.getSize(), Sort.by("sigla").ascending());

        Specification<UnidadeMedida> spec = UnidadeMedidaSpecification.empresaIgual(idEmpresa)
                .and(UnidadeMedidaSpecification.siglaContem(filtro.getFiltroSigla()))
                .and(UnidadeMedidaSpecification.descricaoContem(filtro.getFiltroDescricao()))
                .and(UnidadeMedidaSpecification.statusIgual(converterStatus(filtro.getFiltroStatus())));

        return repository.findAll(spec, pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UnidadeMedidaDTO buscarPorId(Long id, Long idEmpresa) {
        UnidadeMedida entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Medida não encontrada."));
        return mapper.toDTO(entity);
    }

    @Transactional
    public UnidadeMedidaDTO salvar(UnidadeMedidaDTO dto, Long idEmpresa) {
        if (repository.existsBySiglaIgnoreCaseAndEmpresa_Id(dto.getSigla(), idEmpresa)) {
            throw new IllegalArgumentException("Já existe uma unidade com essa sigla.");
        }
        UnidadeMedida entity = mapper.toEntity(dto);
        Empresa empresa = empresaService.buscarPorId(idEmpresa);
        entity.setEmpresa(empresa);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public UnidadeMedidaDTO atualizar(Long id, UnidadeMedidaDTO dto, Long idEmpresa) {
        UnidadeMedida entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Medida não encontrada."));

        if (repository.existsBySiglaIgnoreCaseAndEmpresa_IdAndIdNot(dto.getSigla(), idEmpresa, id)) {
            throw new IllegalArgumentException("Já existe outra unidade com essa sigla.");
        }
        entity.setSigla(dto.getSigla());
        entity.setDescricao(dto.getDescricao());
        entity.setStatus(dto.getStatus());
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public void alterarStatus(Long id, Long idEmpresa, Boolean status) {
        UnidadeMedida entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Medida não encontrada."));
        entity.setStatus(status);
        repository.save(entity);
    }

    @Transactional
    public void excluir(Long id, Long idEmpresa) {
        UnidadeMedida entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Unidade de Medida não encontrada."));
        repository.delete(entity);
    }

    private Boolean converterStatus(Integer status) {
        if (status == null) return null;
        if (status == 0) return false;
        if (status == 1) return true;
        return null;
    }
}