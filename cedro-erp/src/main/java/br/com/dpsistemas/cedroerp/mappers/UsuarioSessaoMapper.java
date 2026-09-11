package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.UsuarioSessaoDTO;
import br.com.dpsistemas.cedroerp.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioSessaoMapper {

    public UsuarioSessaoDTO toDTO(Usuario usuario) {

        UsuarioSessaoDTO dto =
                new UsuarioSessaoDTO();

        dto.setIdUsuario(
                usuario.getId()
        );

        dto.setIdEmpresa(
                usuario.getEmpresa().getId()
        );

        dto.setNome(
                usuario.getNome()
        );

        dto.setNomeEmpresa(
                usuario.getEmpresa().getNomeFantasia()
        );

        dto.setPerfil(
                usuario.getPerfil()
        );

        dto.setFoto(
                usuario.getFoto()
        );

        /*
         * Departamento pode ser nulo?
         * Se puder, já deixamos protegido.
         */
        if (usuario.getDepartamento() != null) {

            dto.setIdDepartamento(
                    usuario.getDepartamento().getId()
            );

            dto.setNomeDepartamento(
                    usuario.getDepartamento().getNome()
            );
        }

        return dto;
    }
}