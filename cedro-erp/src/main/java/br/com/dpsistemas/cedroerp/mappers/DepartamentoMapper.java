package br.com.dpsistemas.cedroerp.mappers;


import br.com.dpsistemas.cedroerp.dtos.DepartamentoDTO;
import br.com.dpsistemas.cedroerp.models.Departamento;
import br.com.dpsistemas.cedroerp.models.Empresa;
import org.springframework.stereotype.Component;

@Component
public class DepartamentoMapper {

    public DepartamentoDTO toDTO(Departamento departamento) {

        if (departamento == null) {
            return null;
        }

        DepartamentoDTO dto = new DepartamentoDTO();

        dto.setId(departamento.getId());

        if (departamento.getEmpresa() != null) {
            dto.setIdEmpresa(
                    departamento.getEmpresa().getId()
            );
        }

        dto.setNome(departamento.getNome());
        dto.setDescricao(departamento.getDescricao());

        dto.setStatus(departamento.getStatus());

        dto.setDataCadastro(
                departamento.getDataCadastro()
        );

        dto.setDataAlteracao(
                departamento.getDataAlteracao()
        );

        return dto;
    }


    public Departamento toEntity(
            DepartamentoDTO dto,
            Empresa empresa) {

        if (dto == null) {
            return null;
        }

        Departamento departamento =
                new Departamento();

        departamento.setEmpresa(empresa);

        departamento.setNome(
                normalizarTexto(dto.getNome())
        );

        departamento.setDescricao(
                normalizarTexto(dto.getDescricao())
        );

        /*
         * Se vier null, deixamos a BaseEntity
         * definir como true no @PrePersist.
         */
        if (dto.getStatus() != null) {
            departamento.setStatus(
                    dto.getStatus()
            );
        }

        return departamento;
    }


    public void updateEntity(
            DepartamentoDTO dto,
            Departamento departamento,
            Empresa empresa) {

        if (dto == null || departamento == null) {
            return;
        }

        departamento.setEmpresa(empresa);

        departamento.setNome(
                normalizarTexto(dto.getNome())
        );

        departamento.setDescricao(
                normalizarTexto(dto.getDescricao())
        );

        if (dto.getStatus() != null) {
            departamento.setStatus(
                    dto.getStatus()
            );
        }
    }


    private String normalizarTexto(String texto) {

        if (texto == null) {
            return null;
        }

        return texto.trim();
    }
}
