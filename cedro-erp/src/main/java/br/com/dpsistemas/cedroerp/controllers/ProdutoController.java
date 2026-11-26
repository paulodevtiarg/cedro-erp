package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.ProdutoDTO;
import br.com.dpsistemas.cedroerp.services.*;
import br.com.dpsistemas.cedroerp.utils.UrlUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired private ProdutoService produtoService;
    @Autowired private TipoProdutoService tipoService;
    @Autowired private EspecieMadeiraService especieService;
    @Autowired private UnidadeMedidaService unidadeService;
    @Autowired private UsuarioLogadoService usuarioLogadoService;
    @Autowired private UrlUtils urlUtils;
    @Autowired private CloudinaryService cloudinaryService;

    // ==================================================
    // LISTAR
    // ==================================================
    @GetMapping
    public String listar(@ModelAttribute("filtro") ProdutoDTO filtro,
                         @RequestParam(defaultValue = "0") int page, Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        filtro.setPage(page);
        Page<ProdutoDTO> produtos = produtoService.listar(filtro, idEmpresa);

        model.addAttribute("produtos", produtos);
        model.addAttribute("pageTitle", "Produtos - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        model.addAttribute("queryParams", urlUtils.produtoQuery(filtro, page));
        return "produtos/index";
    }

    // ==================================================
    // NOVO
    // ==================================================
    @GetMapping("/novo")
    public String novo(Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        ProdutoDTO dto = new ProdutoDTO();
        dto.setStatus(true);

        model.addAttribute("produto", dto);
        prepararCombos(model, idEmpresa);

        model.addAttribute("pageTitle", "Novo Produto - Cedro ERP");
        model.addAttribute("activeMenu", "estoque");
        return "produtos/form";
    }

    // ==================================================
    // SALVAR (NOVO)
    // ==================================================
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("produto") ProdutoDTO dto,
                         BindingResult result, Model model,
                         @RequestParam(value = "arquivoImagem", required = false) MultipartFile imagem,
                         RedirectAttributes ra) throws IOException {

        Long idEmpresa = usuarioLogadoService.getEmpresaId();

        if (result.hasErrors()) {
            prepararCombos(model, idEmpresa);
            model.addAttribute("pageTitle", "Novo Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "produtos/form";
        }

        try {
            ProdutoDTO produtoSalvo = produtoService.salvar(dto, idEmpresa);

            // Upload da Imagem
            if (imagem != null && !imagem.isEmpty()) {
                try {
                    String urlImagem = cloudinaryService.upload(imagem, "produtos");
                    produtoService.atualizarImagem(produtoSalvo.getId(), idEmpresa, urlImagem);
                } catch (IOException e) {
                    System.err.println("Erro ao subir a imagem: " + e.getMessage());
                    ra.addFlashAttribute("msgAviso", "Produto salvo, mas a imagem falhou.");
                }
            }

            ra.addFlashAttribute("msgOk", "Produto salvo com sucesso!");
            return "redirect:/produtos";

        } catch (IllegalArgumentException e) {
            result.reject("erro.geral", e.getMessage());
            prepararCombos(model, idEmpresa);
            model.addAttribute("pageTitle", "Novo Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "produtos/form";
        }
    }

    // ==================================================
    // EDITAR (GET)
    // ==================================================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Long idEmpresa = usuarioLogadoService.getEmpresaId();
            ProdutoDTO dto = produtoService.buscarPorId(id, idEmpresa);

            model.addAttribute("produto", dto);
            prepararCombos(model, idEmpresa);

            model.addAttribute("pageTitle", "Editar Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "produtos/form";
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
            return "redirect:/produtos";
        }
    }

    // ==================================================
    // ATUALIZAR (POST)
    // ==================================================
    @PostMapping("/editar/{id}")
    public String atualizar(@PathVariable Long id, @Valid @ModelAttribute("produto") ProdutoDTO dto,
                            BindingResult result, Model model,
                            @RequestParam(value = "arquivoImagem", required = false) MultipartFile imagem,
                            RedirectAttributes ra) throws IOException {

        Long idEmpresa = usuarioLogadoService.getEmpresaId();

        if (result.hasErrors()) {
            prepararCombos(model, idEmpresa);
            model.addAttribute("pageTitle", "Editar Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "produtos/form";
        }

        try {
            ProdutoDTO produtoSalvo = produtoService.atualizar(id, dto, idEmpresa);

            // Upload da Imagem
            if (imagem != null && !imagem.isEmpty()) {
                try {
                    String urlImagem = cloudinaryService.upload(imagem, "produtos");
                    produtoService.atualizarImagem(produtoSalvo.getId(), idEmpresa, urlImagem);
                } catch (IOException e) {
                    System.err.println("Erro ao subir a imagem: " + e.getMessage());
                    ra.addFlashAttribute("msgAviso", "Produto salvo, mas a imagem falhou.");
                }
            }

            ra.addFlashAttribute("msgOk", "Produto atualizado com sucesso!");
            return "redirect:/produtos";

        } catch (IllegalArgumentException e) {
            result.reject("erro.geral", e.getMessage());
            prepararCombos(model, idEmpresa);
            model.addAttribute("pageTitle", "Editar Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "produtos/form";
        }
    }

    // ==================================================
    // ALTERAR STATUS
    // ==================================================
    @PostMapping("/alterar-status/{id}")
    public String alterarStatus(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ProdutoDTO dto = produtoService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            produtoService.alterarStatus(id, usuarioLogadoService.getEmpresaId(), !dto.getStatus());
            ra.addFlashAttribute("msgOk", "Status alterado!");
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", e.getMessage());
        }
        return "redirect:/produtos";
    }

    // ==================================================
    // DETALHES
    // ==================================================
    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, @RequestParam(required = false) String acao, Model model) {
        try {
            ProdutoDTO dto = produtoService.buscarPorId(id, usuarioLogadoService.getEmpresaId());
            model.addAttribute("produto", dto);
            if ("excluir".equals(acao)) model.addAttribute("modoExclusao", true);

            model.addAttribute("pageTitle", "Detalhes do Produto - Cedro ERP");
            model.addAttribute("activeMenu", "estoque");
            return "produtos/detalhes";
        } catch (Exception e) {
            return "redirect:/produtos";
        }
    }

    // ==================================================
    // EXCLUIR
    // ==================================================
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            produtoService.excluir(id, usuarioLogadoService.getEmpresaId());
            ra.addFlashAttribute("msgOk", "Produto excluído com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("msgErro", "Erro ao excluir. Verifique se ele está em uso.");
        }
        return "redirect:/produtos";
    }

    // ==================================================
    // MÉTODO AUXILIAR
    // ==================================================
    private void prepararCombos(Model model, Long idEmpresa) {
        model.addAttribute("tipos", tipoService.listar(new br.com.dpsistemas.cedroerp.dtos.TipoProdutoDTO(), idEmpresa).getContent());
        model.addAttribute("especies", especieService.listar(new br.com.dpsistemas.cedroerp.dtos.EspecieMadeiraDTO(), idEmpresa).getContent());
        model.addAttribute("unidades", unidadeService.listar(new br.com.dpsistemas.cedroerp.dtos.UnidadeMedidaDTO(), idEmpresa).getContent());
    }
}