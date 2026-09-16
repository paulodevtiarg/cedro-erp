package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.DepartamentoDTO;
import br.com.dpsistemas.cedroerp.dtos.UsuarioDTO;
import br.com.dpsistemas.cedroerp.enumerators.PerfilEnum;
import br.com.dpsistemas.cedroerp.services.CloudinaryService;
import br.com.dpsistemas.cedroerp.services.DepartamentoService;
import br.com.dpsistemas.cedroerp.services.UsuarioLogadoService;
import br.com.dpsistemas.cedroerp.services.UsuarioService;
import br.com.dpsistemas.cedroerp.utils.UrlUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private UrlUtils urlUtils;

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @GetMapping
    public String listar(@ModelAttribute("filtro") UsuarioDTO filtro,
                         @RequestParam(defaultValue = "0") int page,
                         Model model ) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        filtro.setPage(page);
        Page<UsuarioDTO> usuarios = usuarioService.listar(filtro,idEmpresa);
        model.addAttribute("pageTitle", "Usuarios - Cedro ERP");
        model.addAttribute("usuarios",usuarios);
        model.addAttribute("activeMenu", "pessoal");
        model.addAttribute("queryParams", urlUtils.usuarioQuery(filtro, page));
        return "usuarios/index";
    }

    @GetMapping("/novo")
    public String novo(Model model)
    {
        UsuarioDTO usuarioDTO =  new UsuarioDTO();
        model.addAttribute("departamentos",departamentoService.listarAtivosPorEmpresa(usuarioLogadoService.getEmpresaId()));
        model.addAttribute("usuario",usuarioDTO);
        model.addAttribute("perfis", PerfilEnum.values());
        model.addAttribute("pageTitle", "Usuario :Novo - Cedro ERP");
        model.addAttribute("activeMenu","pessoal");

        return "usuarios/form";
    }
    @PostMapping("/salvar")
    public String salvar(HttpSession session,
                         @Valid @ModelAttribute("usuario") UsuarioDTO dto,
                         BindingResult resultUsuario,
                         Model model,
                         @RequestParam(value = "arquivoFoto", required = false) MultipartFile foto,
                         RedirectAttributes redirectAttributes)throws IOException
    {
// senha obrigatória para novo
        if (dto.getId() == null) {
            if (dto.getSenha() == null || dto.getSenha().isBlank()) {
                resultUsuario.rejectValue("senha", "erro.senha", "Senha é obrigatória");
            } else if (dto.getSenha().length() < 6 || dto.getSenha().length() > 60) {
                resultUsuario.rejectValue("senha", "erro.senha", "A senha deve ter entre 6 e 60 caracteres");
            }else if(dto.getConfirmarSenha() == null || !dto.getSenha().equals(dto.getConfirmarSenha())){
                resultUsuario.rejectValue("senha", "erro.senha", "A senha deve ter entre 6 e 60 caracteres");
            }
        }

        // edição → só valida se digitou
        if (dto.getId() != null && dto.getSenha() != null && !dto.getSenha().isBlank()) {
            if (dto.getSenha().length() < 6 || dto.getSenha().length() > 60) {
                resultUsuario.rejectValue("senha", "erro.senha", "A senha deve ter entre 6 e 60 caracteres");
            }
        }

        // CPF
        if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
            boolean cpfJaExiste;
            if (dto.getId() == null) {
                // NOVO CADASTRO
                cpfJaExiste = usuarioService.existeCpf(dto.getCpf(),usuarioLogadoService.getEmpresaId());
            } else {
                // EDIÇÃO
                cpfJaExiste =  usuarioService.existeCpfOutroUsuario(dto.getCpf(),usuarioLogadoService.getEmpresaId(),dto.getId());
            }
            if (cpfJaExiste) {
                resultUsuario.rejectValue("cpf","erro.cpf","CPF já cadastrado nesta empresa.");
            }
        }

        // LOGIN
        if (dto.getLogin() != null && !dto.getLogin().isBlank()) {
            boolean loginJaExiste;
            if (dto.getId() == null) {
                // NOVO CADASTRO
                loginJaExiste =usuarioService.existeLogin(dto.getLogin(), usuarioLogadoService.getEmpresaId());
            } else {
                // EDIÇÃO
                loginJaExiste =usuarioService.existeLoginOutroUsuario(dto.getLogin(), usuarioLogadoService.getEmpresaId(),dto.getId());
            }
            if (loginJaExiste) {
                resultUsuario.rejectValue("login","erro.login","Login já cadastrado nesta empresa."
                );
            }
        }

        if (resultUsuario.hasErrors()) {
            model.addAttribute("departamentos",departamentoService.listarAtivosPorEmpresa(usuarioLogadoService.getEmpresaId()));
            model.addAttribute("perfis", PerfilEnum.values());
            model.addAttribute("pageTitle", "Usuario :Novo - Cedro ERP");
            model.addAttribute("activeMenu","pessoal");
            return "usuarios/form";
        }//fim primeiro if

        //salvando o usuario
        try{
            Long idEmpresa = usuarioLogadoService.getEmpresaId();
            UsuarioDTO usuarioSalvo =  usuarioService.salvar(dto,idEmpresa);
            if(foto !=null && !foto.isEmpty()){
                try{
                    String urlFoto = cloudinaryService.upload(foto, "usuarios");
                    usuarioService.atualizarFoto(usuarioSalvo.getId(), idEmpresa, urlFoto);
                }catch (Exception e){
                    log.warn("Usuario {} salvo, mas houve erro no upload da Foto", dto.getEmail(),e);
                }
            }
            redirectAttributes.addFlashAttribute("msgOk", "Usuário salvo com sucesso!");
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("msgErro", "Erro ao salvar o usuário: " + e.getMessage());
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(
            @PathVariable Long id,
            @ModelAttribute("filtro") UsuarioDTO filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String acao,
            Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        UsuarioDTO dto = usuarioService.buscarPorId(id,idEmpresa );

        model.addAttribute("usuario", dto);


        model.addAttribute("pageTitle", "Usuarios:Detalhes - Cedro ERP");
        model.addAttribute("activeMenu","pessoal");

        String queryParams = urlUtils.usuarioQuery(filtro, page);
        model.addAttribute("queryParams", queryParams);

        return "usuarios/detalhes";
    }

}
