package br.com.dpsistemas.cedroerp.controllers;

import java.util.Set;

import br.com.dpsistemas.cedroerp.dtos.UsuarioSessaoDTO;
import br.com.dpsistemas.cedroerp.mappers.UsuarioSessaoMapper;
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
	private final UsuarioSessaoMapper usuarioSessaoMapper;

	public LoginController(
			UsuarioService usuarioService,
			UsuarioSessaoMapper usuarioSessaoMapper) {

		this.usuarioService = usuarioService;
		this.usuarioSessaoMapper =	usuarioSessaoMapper;
	}
	
	@GetMapping("/login")
	public String loginPage(@RequestParam(required = false) String modo,
	                        @RequestParam(required = false) String login,
	                        HttpSession session,
	                        Model model) {

		String modoAtual =	modo == null? "login": modo;

		/*
		 * Não permite abrir manualmente
		 * /login?modo=primeiro-acesso
		 * sem ter autenticado antes.
		 */
		if (
				"primeiro-acesso".equals(modoAtual) &&	session.getAttribute("usuarioPrimeiroAcesso") == null
		) {

			modoAtual = "login";
		}

		model.addAttribute("modo",modoAtual);
	    model.addAttribute("login", login);

	    return "login/index";
	}
	@PostMapping("/login")
	public String login(
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam(defaultValue = "login") String loginType,
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		try {
			Usuario usuario =	usuarioService.autenticar(username,	password,loginType);
			UsuarioSessaoDTO usuarioSessao = usuarioSessaoMapper.toDTO(usuario);


			/*
			 * PRIMEIRO ACESSO
			 *
			 * A senha já foi validada,
			 * mas ainda NÃO liberamos acesso ao sistema.
			 */
			if (Boolean.TRUE.equals(usuario.getPrimeiroAcesso())) {
				session.removeAttribute("usuarioLogado");
				session.setAttribute("usuarioPrimeiroAcesso",usuarioSessao);

				return "redirect:/login?modo=primeiro-acesso";
			}



			session.setAttribute("usuarioLogado",	usuarioSessao); //aqui a sessao é atribuida
			session.removeAttribute("usuarioPrimeiroAcesso");
			return "redirect:/home";

		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("erro",e.getMessage());
			redirectAttributes.addAttribute("login",username);
			redirectAttributes.addAttribute("modo",	"login");
			return "redirect:/login";
		}
	}

	private String autenticarNovaSessao(
			Usuario usuario,
			HttpSession session) {

		UsuarioSessaoDTO usuarioSessao =
				usuarioSessaoMapper.toDTO(usuario);

		session.removeAttribute(
				"usuarioPrimeiroAcesso"
		);

		session.setAttribute(
				"usuarioLogado",
				usuarioSessao
		);

		return "redirect:/home";
	}

	@GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
	@PostMapping("/primeiro-acesso/manter-senha")
	public String manterSenhaPrimeiroAcesso(
			HttpSession session,
			RedirectAttributes ra) {

		UsuarioSessaoDTO usuarioPendente =
				(UsuarioSessaoDTO)
						session.getAttribute(
								"usuarioPrimeiroAcesso"
						);

		if (usuarioPendente == null) {

			return "redirect:/login";
		}

		Usuario usuario =
				usuarioService.finalizarPrimeiroAcesso(
						usuarioPendente.getIdUsuario(),
						usuarioPendente.getIdEmpresa()
				);

		/*
		 * Agora sim vira usuário logado.
		 */
		return autenticarNovaSessao(usuario,session	);
	}

	@PostMapping("/primeiro-acesso/alterar-senha")
	public String alterarSenhaPrimeiroAcesso(
			@RequestParam String senha,
			@RequestParam String confirmarSenha,
			HttpSession session,
			RedirectAttributes ra) {

		UsuarioSessaoDTO usuarioPendente =
				(UsuarioSessaoDTO)
						session.getAttribute(
								"usuarioPrimeiroAcesso"
						);

		if (usuarioPendente == null) {

			return "redirect:/login";
		}

		try {

			Usuario usuario =
					usuarioService.alterarSenhaPrimeiroAcesso(
							usuarioPendente.getIdUsuario(),
							usuarioPendente.getIdEmpresa(),
							senha,
							confirmarSenha
					);

			return autenticarNovaSessao(
					usuario,
					session
			);

		} catch (IllegalArgumentException e) {

			ra.addFlashAttribute(
					"erro",
					e.getMessage()
			);

			return "redirect:/login?modo=primeiro-acesso";
		}
	}

	
}
