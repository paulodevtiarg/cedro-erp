package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.MovimentacaoEstoqueDTO;
import br.com.dpsistemas.cedroerp.dtos.ProdutoDTO;
import br.com.dpsistemas.cedroerp.enumerators.TipoMovimentacaoEnum;
import br.com.dpsistemas.cedroerp.models.ProdutoMedida;
import br.com.dpsistemas.cedroerp.services.MovimentacaoEstoqueService;
import br.com.dpsistemas.cedroerp.services.ProdutoService;
import br.com.dpsistemas.cedroerp.services.UsuarioLogadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/estoque/movimentacoes")
public class MovimentacaoEstoqueController {

    @Autowired private MovimentacaoEstoqueService movimentacaoService;
    @Autowired private ProdutoService produtoService;
    @Autowired private UsuarioLogadoService usuarioLogadoService;

    @GetMapping
    public String listar(@ModelAttribute("filtro") MovimentacaoEstoqueDTO filtro,
                         @RequestParam(defaultValue = "0") int page,
                         Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        filtro.setPage(page);

        Page<MovimentacaoEstoqueDTO> movimentacoes = movimentacaoService.listar(filtro, idEmpresa);

        // 🔥 AQUI USAMOS O NOVO MÉTODO AGRUPADO
        Map<String, List<ProdutoMedida>> medidasAgrupadas = produtoService.obterMedidasAgrupadasPorProduto(idEmpresa);

        model.addAttribute("movimentacoes", movimentacoes);
        model.addAttribute("medidasAgrupadas", medidasAgrupadas);
        model.addAttribute("tiposMovimentacao", TipoMovimentacaoEnum.values());

        // 🔥 AQUI ESTÁ A CORREÇÃO
        model.addAttribute("movimentacao", new MovimentacaoEstoqueDTO());

        model.addAttribute("pageTitle", "Movimentações de Estoque - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        return "movimentacoes/index";
    }

    @PostMapping("/lancar")
    public String lancar(@Valid @ModelAttribute("movimentacao") MovimentacaoEstoqueDTO dto,
                         BindingResult result, Model model, RedirectAttributes ra) {
        try {
            movimentacaoService.lancarMovimentacao(dto, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Movimentação registrada com sucesso!");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
        }
        return "redirect:/estoque/movimentacoes";
    }
}