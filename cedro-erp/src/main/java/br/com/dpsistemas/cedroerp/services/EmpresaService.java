package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.repositorys.EmpresaRepository;
import org.springframework.stereotype.Service;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Empresa não encontrada."
                        )
                );
    }

    public Empresa salvar(Empresa empresa) {
        return empresaRepository.save(empresa);
    }
}