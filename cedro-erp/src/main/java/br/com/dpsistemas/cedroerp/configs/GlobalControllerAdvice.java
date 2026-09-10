package br.com.dpsistemas.cedroerp.configs;

import br.com.dpsistemas.cedroerp.dtos.UsuarioSessaoDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.dpsistemas.cedroerp.models.Usuario;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("usuarioLogado")
    public UsuarioSessaoDTO getUsuarioLogado(
            HttpSession session) {

        return (UsuarioSessaoDTO)
                session.getAttribute("usuarioLogado");
    }
}