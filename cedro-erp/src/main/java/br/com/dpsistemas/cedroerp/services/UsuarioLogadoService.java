package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.UsuarioSessaoDTO;
import br.com.dpsistemas.cedroerp.enumerators.PerfilEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class UsuarioLogadoService {

    private final HttpSession session;

    public UsuarioLogadoService(
            HttpSession session) {

        this.session = session;
    }


    public UsuarioSessaoDTO getUsuarioLogado() {

        return (UsuarioSessaoDTO)
                session.getAttribute("usuarioLogado");
    }

    public Long getEmpresaId() {

        UsuarioSessaoDTO usuario =
                getUsuarioLogado();

        if (usuario == null) {
            return null;
        }

        return usuario.getIdEmpresa();
    }


    public PerfilEnum getPerfil() {

        UsuarioSessaoDTO usuario =
                getUsuarioLogado();

        if (usuario == null) {
            return null;
        }

        return usuario.getPerfil();
    }


    public boolean isAdmin() {

        return getPerfil() ==
                PerfilEnum.ADMIN;
    }


    public boolean isGestor() {

        return getPerfil() ==
                PerfilEnum.GESTOR;
    }


    public boolean podeGerenciarCadastros() {

        PerfilEnum perfil =
                getPerfil();

        return perfil == PerfilEnum.ADMIN
                || perfil == PerfilEnum.GESTOR;
    }
}