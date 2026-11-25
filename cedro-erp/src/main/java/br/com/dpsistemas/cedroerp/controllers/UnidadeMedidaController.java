package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.UnidadeMedidaDTO;
import br.com.dpsistemas.cedroerp.services.UnidadeMedidaService;
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
@RequestMapping("/unidades-medida")
public class UnidadeMedidaController {

    @Autowired private UnidadeMedidaService unidadeService;
    @Autowired private UsuarioLogadoService usuarioLogadoService;
    @Autowired private UrlUtils urlUtils;

    @GetMapping
    public String listar(@ModelAttribute("filtro") UnidadeMedidaDTO filtro,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        filtro.setPage(page);

        Page<UnidadeMedidaDTO> unidades = unidadeService.listar(filtro, idEmpresa);

        model.addAttribute("unidades", unidades);
        model.addAttribute("pageTitle", "Unidades de Medida - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        model.addAttribute("queryParams", urlUtils.unidadeMedidaQuery(filtro, page));
        return "unidades-medida/index";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        UnidadeMedidaDTO dto = new UnidadeMedidaDTO();
        dto.setStatus(true);
        model.addAttribute("unidade", dto);
        model.addAttribute("pageTitle", "Nova Unidade de Medida - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        return "unidades-medida/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("unidade") UnidadeMedidaDTO dto,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Nova Unidade de Medida - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "unidades-medida/form";
        }
        try {
            unidadeService.salvar(dto, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Unidade salva com sucesso!");
            return "redirect:/unidades-medida";
        } catch (IllegalArgumentException e) {
            result.rejectValue("sigla", "erro.sigla", e.getMessage());
            model.addAttribute("pageTitle", "Nova Unidade de Medida - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "unidades-medida/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            UnidadeMedidaDTO dto = unidadeService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            model.addAttribute("unidade", dto);
            model.addAttribute("pageTitle", "Editar Unidade de Medida - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "unidades-medida/form";
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
            return "redirect:/unidades-medida";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("unidade") UnidadeMedidaDTO dto,
                            BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Editar Unidade de Medida - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "unidades-medida/form";
        }
        try {
            unidadeService.atualizar(id, dto, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Unidade atualizada com sucesso!");
            return "redirect:/unidades-medida";
        } catch (IllegalArgumentException e) {
            result.rejectValue("sigla", "erro.sigla", e.getMessage());
            model.addAttribute("pageTitle", "Editar Unidade de Medida - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "unidades-medida/form";
        }
    }

    @PostMapping("/alterar-status/{id}")
    public String alterarStatus(@PathVariable Long id, RedirectAttributes ra) {
        try {
            UnidadeMedidaDTO dto = unidadeService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            unidadeService.alterarStatus(id, usuarioLogadoService.getEmpresaId(), !dto.getStatus());
            ra.addFlashAttribute("msgOk", "Status alterado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
        }
        return "redirect:/unidades-medida";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id,
                           @RequestParam(required = false) String acao,
                           Model model) {
        try {
            UnidadeMedidaDTO dto = unidadeService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            model.addAttribute("unidade", dto);
            if ("excluir".equals(acao)) model.addAttribute("modoExclusao", true);
            model.addAttribute("pageTitle", "Detalhes da Unidade - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "unidades-medida/detalhes";
        } catch (Exception e) {
            return "redirect:/unidades-medida";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            unidadeService.excluir(id, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Unidade excluída com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", "Erro ao excluir. Verifique se ela está em uso.");
        }
        return "redirect:/unidades-medida";
    }
}