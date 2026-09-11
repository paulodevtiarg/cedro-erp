package br.com.dpsistemas.cedroerp.configs;

import br.com.dpsistemas.cedroerp.dtos.UsuarioSessaoDTO;
import br.com.dpsistemas.cedroerp.services.UsuarioLogadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.dpsistemas.cedroerp.models.Usuario;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @ModelAttribute("usuarioLogado")
    public UsuarioSessaoDTO usuarioLogado() {
        return usuarioLogadoService.getUsuarioLogado();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        return usuarioLogadoService.isAdmin();
    }

    @ModelAttribute("isGestor")
    public boolean isGestor() {
        return usuarioLogadoService.isGestor();
    }

    @ModelAttribute("podeGerenciarCadastros")
    public boolean podeGerenciarCadastros() {
        return usuarioLogadoService.podeGerenciarCadastros();
    }
}