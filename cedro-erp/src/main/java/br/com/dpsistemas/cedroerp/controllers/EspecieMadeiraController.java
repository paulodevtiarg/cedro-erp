package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.EspecieMadeiraDTO;
import br.com.dpsistemas.cedroerp.services.EspecieMadeiraService;
import br.com.dpsistemas.cedroerp.services.UsuarioLogadoService;
import br.com.dpsistemas.cedroerp.utils.UrlUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/especies")
public class EspecieMadeiraController {

    @Autowired
    private EspecieMadeiraService especieService;
    @Autowired
    private UsuarioLogadoService usuarioLogadoService;
    @Autowired
    private UrlUtils urlUtils;

    @GetMapping
    public String listar(@ModelAttribute("filtro") EspecieMadeiraDTO filtro,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        filtro.setPage(page);

        Page<EspecieMadeiraDTO> especies = especieService.listar(filtro, idEmpresa);

        model.addAttribute("especies", especies);
        model.addAttribute("pageTitle", "Espécies - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        model.addAttribute("queryParams", urlUtils.especieQuery(filtro, page));
        // Aqui você pode criar um metodo no UrlUtils para montar os params de busca
        return "especies/index";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("especie", new EspecieMadeiraDTO());
        model.addAttribute("pageTitle", "Nova Espécie - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        return "especies/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("especie") EspecieMadeiraDTO dto,
                         BindingResult result, Model model, RedirectAttributes ra) {

        // Se o @NotBlank falhar, cai aqui e volta pro form
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Nova Espécie - Cedro ERP");
            model.addAttribute("activeMenu", "cadastros");
            return "especies/form";
        }

        try {
            especieService.salvar(dto, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Espécie salva com sucesso!");
            return "redirect:/especies";
        } catch (IllegalArgumentException e) {
            // Erro de nome duplicado
            result.rejectValue("nome", "erro.nome", e.getMessage());
            model.addAttribute("pageTitle", "Nova Espécie - Cedro ERP");
            model.addAttribute("activeMenu", "cadastros");
            return "especies/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            EspecieMadeiraDTO dto = especieService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            model.addAttribute("especie", dto);
            model.addAttribute("pageTitle", "Editar Espécie - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "especies/form";
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
            return "redirect:/especies";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("especie") EspecieMadeiraDTO dto,
                            BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Editar Espécie - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "especies/form";
        }
        try {
            especieService.atualizar(id, dto, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Espécie atualizada com sucesso!");
            return "redirect:/especies";
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "erro.nome", e.getMessage());
            model.addAttribute("pageTitle", "Editar Espécie - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "especies/form";
        }
    }

    @PostMapping("/alterar-status/{id}")
    public String alterarStatus(@PathVariable Long id, RedirectAttributes ra) {
        try {
            EspecieMadeiraDTO dto = especieService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            especieService.alterarStatus(id, usuarioLogadoService.getEmpresaId(), !dto.getStatus());
            ra.addFlashAttribute("msgOk", "Status alterado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
        }
        return "redirect:/especies";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id,
                           @ModelAttribute("filtro") EspecieMadeiraDTO filtro,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String acao,
                           Model model) {
        try {
            EspecieMadeiraDTO dto = especieService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            model.addAttribute("especie", dto);
            if ("excluir".equals(acao)) model.addAttribute("modoExclusao", true);

            model.addAttribute("pageTitle", "Detalhes da Espécie - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            model.addAttribute("queryParams", urlUtils.especieQuery(filtro, page));
            return "especies/detalhes";
        } catch (Exception e) {
            return "redirect:/especies";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            especieService.excluir(id, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Espécie excluída com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", "Erro ao excluir. Verifique se ela está em uso.");
        }
        return "redirect:/especies";
    }
}