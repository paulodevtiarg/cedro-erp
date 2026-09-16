package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.models.Usuario;
import br.com.dpsistemas.cedroerp.services.CodigoSegurancaService;
import br.com.dpsistemas.cedroerp.services.EmailService;
import br.com.dpsistemas.cedroerp.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ForgotPasswordController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CodigoSegurancaService codigoService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/esqueci-senha")
    public String esqueciSenha(@RequestParam String login,
                               RedirectAttributes ra) {

        Usuario usuario = usuarioService.buscarPorLoginCpfOuEmail(login);

        if (usuario == null) {
            ra.addFlashAttribute("erro", "Usuário não encontrado");
            return "redirect:/login?modo=forgot";
        }

        String codigo = codigoService.gerarCodigo();
        usuario.setCodSeguranca(codigo);
        usuario.setDataExpiracao(LocalDateTime.now().plusMinutes(15));

        usuarioService.salvarCodigoRecuperacao(
                usuario.getId(),
                codigo,
                LocalDateTime.now().plusMinutes(15)
        );
        // 🔥 TRATE O NULL
        boolean primeiroAcesso = Boolean.TRUE.equals(usuario.getPrimeiroAcesso());
        emailService.enviarCodigo(
                usuario.getEmail(),
                codigo,
                primeiroAcesso
        );

        // 🔥 AQUI:
        ra.addFlashAttribute("primeiroAcesso", primeiroAcesso);

        return "redirect:/login?modo=reset&login=" + login;
    }

    @GetMapping("/validar-reset-senha")
    public String tela() {
        return "login/reset-senha";
    }

    @GetMapping("/esqueci-senha")
    public String tela(Model model) {
        model.addAttribute("recuperacao", true);
        return "login/index";
    }

    @GetMapping("/reset-senha")
    public String telaReset(@RequestParam String login, Model model) {
        model.addAttribute("modo", "reset");

        model.addAttribute("login", login);
        return "login/reset-senha";
    }


    @PostMapping("/reset-senha")
    public String resetSenha(@RequestParam String login,
                             @RequestParam String codigo,
                             @RequestParam String senha,
                             @RequestParam String confirmarSenha,
                             RedirectAttributes ra) {

        if (!senha.equals(confirmarSenha)) {
            ra.addFlashAttribute("erro", "Senhas não conferem");
            return "redirect:/login?modo=reset&login=" + login;
        }

        Usuario usuario = usuarioService.buscarPorLoginCpfOuEmail(login);

        if (usuario == null) {
            return "redirect:/login?modo=login";
        }

        if (usuario.getDataExpiracao() == null ||
                usuario.getDataExpiracao().isBefore(LocalDateTime.now())) {
            ra.addFlashAttribute("erro", "Código expirado");
            return "redirect:/login?modo=forgot";
        }

        if (!codigo.equals(usuario.getCodSeguranca())) {
            ra.addFlashAttribute("erro", "Código inválido");
            return "redirect:/login?modo=reset&login=" + login;
        }

        ra.addFlashAttribute("sucess", "Senha validada");
        usuarioService.concluirResetSenha(
                usuario.getId(),
                senha
        );

        ra.addFlashAttribute(
                "sucesso",
                "Senha alterada com sucesso! Você já pode acessar o sistema."
        );

        return "redirect:/login?modo=login";
    }
}