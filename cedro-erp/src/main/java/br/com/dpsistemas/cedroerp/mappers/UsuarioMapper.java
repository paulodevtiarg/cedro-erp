package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.UsuarioDTO;
import br.com.dpsistemas.cedroerp.models.Departamento;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDTO(Usuario usuario) {

        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();

        dto.setId(usuario.getId());

        if (usuario.getEmpresa() != null) {
            dto.setIdEmpresa(usuario.getEmpresa().getId());
            dto.setNomeEmpresa(usuario.getEmpresa().getRazaoSocial());
        }

        if (usuario.getDepartamento() != null) {
            dto.setIdDepartamento(
                    usuario.getDepartamento().getId()
            );

            dto.setNomeDepartamento(
                    usuario.getDepartamento().getNome()
            );
        }

        dto.setLogin(usuario.getLogin());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setCpf(usuario.getCpf());
        dto.setTelefone(usuario.getTelefone());
        dto.setPerfil(usuario.getPerfil());
        dto.setFoto(usuario.getFoto());

        dto.setStatus(usuario.getStatus());
        dto.setDataCadastro(usuario.getDataCadastro());
        dto.setDataAlteracao(usuario.getDataAlteracao());

        /*
         * IMPORTANTE:
         *
         * Nunca:
         *
         * dto.setSenha(usuario.getSenha());
         *
         * O hash não deve voltar para a tela.
         */

        return dto;
    }


    public Usuario toEntity(
            UsuarioDTO dto,
            Empresa empresa,
            Departamento departamento) {

        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setEmpresa(empresa);
        usuario.setDepartamento(departamento);

        usuario.setLogin(normalizar(dto.getLogin()));
        usuario.setNome(normalizar(dto.getNome()));
        usuario.setEmail(normalizar(dto.getEmail()));
        usuario.setCpf(normalizar(dto.getCpf()));
        usuario.setTelefone(normalizar(dto.getTelefone()));

        usuario.setPerfil(dto.getPerfil());
        usuario.setFoto(dto.getFoto());

        if (dto.getStatus() != null) {
            usuario.setStatus(dto.getStatus());
        }

        /*
         * A senha NÃO é definida aqui.
         * O Service fará o BCrypt.
         */

        return usuario;
    }


    public void updateEntity(
            UsuarioDTO dto,
            Usuario usuario,
            Empresa empresa,
            Departamento departamento) {

        if (dto == null || usuario == null) {
            return;
        }

        usuario.setEmpresa(empresa);
        usuario.setDepartamento(departamento);

        usuario.setLogin(normalizar(dto.getLogin()));
        usuario.setNome(normalizar(dto.getNome()));
        usuario.setEmail(normalizar(dto.getEmail()));
        usuario.setCpf(normalizar(dto.getCpf()));
        usuario.setTelefone(normalizar(dto.getTelefone()));

        usuario.setPerfil(dto.getPerfil());

        if (dto.getFoto() != null) {
            usuario.setFoto(dto.getFoto());
        }

        if (dto.getStatus() != null) {
            usuario.setStatus(dto.getStatus());
        }

        /*
         * Não mexemos em:
         *
         * senha
         * codigoSeguranca
         * codigoExpiracao
         * dataCadastro
         * dataAlteracao
         *
         * Cada um possui seu fluxo específico.
         */
    }


    private String normalizar(String valor) {

        if (valor == null) {
            return null;
        }

        return valor.trim();
    }
}