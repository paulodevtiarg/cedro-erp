package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.TipoProdutoDTO;
import br.com.dpsistemas.cedroerp.services.TipoProdutoService;
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
@RequestMapping("/tipos-produtos")
public class TipoProdutoController {

    @Autowired private TipoProdutoService tipoProdutoService;
    @Autowired private UsuarioLogadoService usuarioLogadoService;
    @Autowired private UrlUtils urlUtils;

    @GetMapping
    public String listar(@ModelAttribute("filtro") TipoProdutoDTO filtro,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        filtro.setPage(page);

        Page<TipoProdutoDTO> tipos = tipoProdutoService.listar(filtro, idEmpresa);

        model.addAttribute("tipos", tipos);
        model.addAttribute("pageTitle", "Tipos de Produto - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        model.addAttribute("queryParams", urlUtils.tipoProdutoQuery(filtro, page));
        return "tipos-produtos/index";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        TipoProdutoDTO dto = new TipoProdutoDTO();
        dto.setStatus(true);
        model.addAttribute("tipo", dto);
        model.addAttribute("pageTitle", "Novo Tipo de Produto - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        return "tipos-produtos/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("tipo") TipoProdutoDTO dto,
                         BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Novo Tipo de Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "tipos-produtos/form";
        }
        try {
            tipoProdutoService.salvar(dto, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Tipo salvo com sucesso!");
            return "redirect:/tipos-produtos";
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "erro.nome", e.getMessage());
            model.addAttribute("pageTitle", "Novo Tipo de Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "tipos-produtos/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            TipoProdutoDTO dto = tipoProdutoService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            model.addAttribute("tipo", dto);
            model.addAttribute("pageTitle", "Editar Tipo de Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "tipos-produtos/form";
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
            return "redirect:/tipos-produtos";
        }
    }

    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("tipo") TipoProdutoDTO dto,
                            BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Editar Tipo de Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "tipos-produtos/form";
        }
        try {
            tipoProdutoService.atualizar(id, dto, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Tipo atualizado com sucesso!");
            return "redirect:/tipos-produtos";
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "erro.nome", e.getMessage());
            model.addAttribute("pageTitle", "Editar Tipo de Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "tipos-produtos/form";
        }
    }

    @PostMapping("/alterar-status/{id}")
    public String alterarStatus(@PathVariable Long id, RedirectAttributes ra) {
        try {
            TipoProdutoDTO dto = tipoProdutoService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            tipoProdutoService.alterarStatus(id, usuarioLogadoService.getEmpresaId(), !dto.getStatus());
            ra.addFlashAttribute("msgOk", "Status alterado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
        }
        return "redirect:/tipos-produtos";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id,
                           @RequestParam(required = false) String acao,
                           Model model) {
        try {
            TipoProdutoDTO dto = tipoProdutoService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            model.addAttribute("tipo", dto);
            if ("excluir".equals(acao)) model.addAttribute("modoExclusao", true);
            model.addAttribute("pageTitle", "Detalhes do Tipo - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "tipos-produtos/detalhes";
        } catch (Exception e) {
            return "redirect:/tipos-produtos";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            tipoProdutoService.excluir(id, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Tipo excluído com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", "Erro ao excluir. Verifique se ele está em uso.");
        }
        return "redirect:/tipos-produtos";
    }
}