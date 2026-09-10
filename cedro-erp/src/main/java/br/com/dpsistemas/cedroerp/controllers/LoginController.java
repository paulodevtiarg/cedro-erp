package br.com.dpsistemas.cedroerp.controllers;

import java.util.Set;

import br.com.dpsistemas.cedroerp.dtos.UsuarioSessaoDTO;
import br.com.dpsistemas.cedroerp.models.Usuario;
import br.com.dpsistemas.cedroerp.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	private final UsuarioService usuarioService;

	public LoginController(
			UsuarioService usuarioService) {

		this.usuarioService = usuarioService;
	}
	
	@GetMapping("/login")
	public String loginPage(@RequestParam(required = false) String modo,
	                        @RequestParam(required = false) String login,
	                        Model model) {

	    model.addAttribute("modo", modo == null ? "login" : modo);
	    model.addAttribute("login", login);

	    return "login/index";
	}
	@PostMapping("/login")
	public String login(
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam(
					defaultValue = "login"
			) String loginType,
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		try {

			Usuario usuario =
					usuarioService.autenticar(
							username,
							password,
							loginType
					);

			UsuarioSessaoDTO usuarioSessao =
					new UsuarioSessaoDTO();

			usuarioSessao.setIdUsuario(usuario.getId());
			usuarioSessao.setIdEmpresa(usuario.getEmpresa().getId());
			usuarioSessao.setIdDepartamento(
					usuario.getDepartamento().getId()
			);

			usuarioSessao.setNome(usuario.getNome());

			usuarioSessao.setNomeEmpresa(
					usuario.getEmpresa().getNomeFantasia()
			);

			usuarioSessao.setNomeDepartamento(
					usuario.getDepartamento().getNome()
			);

			usuarioSessao.setPerfil(usuario.getPerfil());
			usuarioSessao.setFoto(usuario.getFoto());

			session.setAttribute(
					"usuarioLogado",
					usuarioSessao
			);

			return "redirect:/home";

		} catch (IllegalArgumentException e) {

			redirectAttributes.addFlashAttribute(
					"erro",
					e.getMessage()
			);

			redirectAttributes.addAttribute(
					"login",
					username
			);

			redirectAttributes.addAttribute(
					"modo",
					"login"
			);

			return "redirect:/login";
		}
	}

	private String autenticarNovaSessao(
			Usuario usuario,
			RedirectAttributes redirectAttributes) {

		/*
		 * Vamos ajustar esta parte abaixo,
		 * porque precisamos da nova HttpSession.
		 */

		return "redirect:/home";
	}

	@GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    

	
}
