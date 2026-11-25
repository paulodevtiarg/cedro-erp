package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.TipoProdutoDTO;
import br.com.dpsistemas.cedroerp.mappers.TipoProdutoMapper;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.TipoProduto;
import br.com.dpsistemas.cedroerp.repositorys.TipoProdutoRepository;
import br.com.dpsistemas.cedroerp.utils.TipoProdutoSpecification;
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
public class TipoProdutoService {

    @Autowired private TipoProdutoRepository repository;
    @Autowired private TipoProdutoMapper mapper;
    @Autowired private EmpresaService empresaService;

    @Transactional(readOnly = true)
    public Page<TipoProdutoDTO> listar(TipoProdutoDTO filtro, Long idEmpresa) {
        Pageable pageable = PageRequest.of(filtro.getPage(), filtro.getSize(), Sort.by("nome").ascending());

        Specification<TipoProduto> spec = TipoProdutoSpecification.empresaIgual(idEmpresa)
                .and(TipoProdutoSpecification.nomeContem(filtro.getFiltroNome()))
                .and(TipoProdutoSpecification.statusIgual(converterStatus(filtro.getFiltroStatus())));

        return repository.findAll(spec, pageable).map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public TipoProdutoDTO buscarPorId(Long id, Long idEmpresa) {
        TipoProduto entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Produto não encontrado."));
        return mapper.toDTO(entity);
    }

    @Transactional
    public TipoProdutoDTO salvar(TipoProdutoDTO dto, Long idEmpresa) {
        if (repository.existsByNomeIgnoreCaseAndEmpresa_Id(dto.getNome(), idEmpresa)) {
            throw new IllegalArgumentException("Já existe um tipo de produto com esse nome.");
        }
        TipoProduto entity = mapper.toEntity(dto);
        Empresa empresa = empresaService.buscarPorId(idEmpresa);
        entity.setEmpresa(empresa);
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public TipoProdutoDTO atualizar(Long id, TipoProdutoDTO dto, Long idEmpresa) {
        TipoProduto entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Produto não encontrado."));

        if (repository.existsByNomeIgnoreCaseAndEmpresa_IdAndIdNot(dto.getNome(), idEmpresa, id)) {
            throw new IllegalArgumentException("Já existe outro tipo de produto com esse nome.");
        }
        entity.setNome(dto.getNome());
        entity.setStatus(dto.getStatus());
        return mapper.toDTO(repository.save(entity));
    }

    @Transactional
    public void alterarStatus(Long id, Long idEmpresa, Boolean status) {
        TipoProduto entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Produto não encontrado."));
        entity.setStatus(status);
        repository.save(entity);
    }

    @Transactional
    public void excluir(Long id, Long idEmpresa) {
        TipoProduto entity = repository.findByIdAndEmpresa_Id(id, idEmpresa)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Produto não encontrado."));
        repository.delete(entity);
    }

    private Boolean converterStatus(Integer status) {
        if (status == null) return null;
        if (status == 0) return false;
        if (status == 1) return true;
        return null;
    }
}