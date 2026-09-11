package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.DepartamentoDTO;
import br.com.dpsistemas.cedroerp.services.DepartamentoService;
import br.com.dpsistemas.cedroerp.services.UsuarioLogadoService;
import br.com.dpsistemas.cedroerp.utils.UrlUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {
    @Autowired
    private UrlUtils urlUtils;

    @Autowired
    private DepartamentoService departamentoService;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;
    @GetMapping
    public String listar(@ModelAttribute("filtro") DepartamentoDTO filtro,
                         @RequestParam(defaultValue = "0") int page,
                         Model model ) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        filtro.setPage(page);
        Page<DepartamentoDTO> departamentos = departamentoService.listar(filtro,idEmpresa);
        model.addAttribute("pageTitle", "Departamentos - Cedro ERP");
        model.addAttribute("departamentos",departamentos);
        model.addAttribute("activeMenu", "pessoal");
        model.addAttribute("queryParams", urlUtils.departamentoQuery(filtro, page));
        return "departamentos/index";
    }

    @GetMapping("/novo")
    public String novo(Model model)
    {
        DepartamentoDTO departamento =  new DepartamentoDTO();

        model.addAttribute("departamento",departamento);
        model.addAttribute("pageTitle", "Departamentos:Novo - Cedro ERP");
        model.addAttribute("activeMenu","pessoal");

        return "departamentos/form";
    }

    @PostMapping("/salvar")
    public String salvar(HttpSession session,
                         @Valid @ModelAttribute("departamento") DepartamentoDTO dto,
                         BindingResult resultDepartamento,
                         Model model,
                         RedirectAttributes redirectAttributes)throws IOException
    {
        if (resultDepartamento.hasErrors() || resultDepartamento.hasErrors()) {
            model.addAttribute("pageTitle", "Departamentos:Novo - Cedro ERP");
            model.addAttribute("activeMenu","pessoal");
            return "departamentos/form";

        }

        try{
            Long idEmpresa = usuarioLogadoService.getEmpresaId();
            departamentoService.salvar(dto,idEmpresa);
            redirectAttributes.addFlashAttribute("msgOk", "Registro salvo com sucesso!");
        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "msgErro",
                    e.getMessage()
            );

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "msgErro",
                    "Erro inesperado ao salvar o registro."
            );
        }
        //Pega o Id da empresa


        return "redirect:/departamentos";


    }

    @GetMapping("/editar/{id}")
    public String editar( @PathVariable Long id,
                          @ModelAttribute("filtro") DepartamentoDTO filtro,
                          @RequestParam(defaultValue = "0") int page,
                          Model model,
                          RedirectAttributes redirectAttributes)
    {
        if (!usuarioLogadoService.podeGerenciarCadastros()) {
            redirectAttributes.addFlashAttribute(
                    "msgErro",
                    "Você não possui permissão para editar departamentos."
            );

            return "redirect:/departamentos";
        }
        Long idEmpresa =  usuarioLogadoService.getEmpresaId();
        DepartamentoDTO departamento = departamentoService.buscarPorId(id,idEmpresa);
        model.addAttribute("departamento",departamento);
        model.addAttribute("pageTitle","Departamentos: Editar - Cedro ERP");
        model.addAttribute("activeMenu","pessoal");
        String queryParams = urlUtils.departamentoQuery(filtro, page);
        model.addAttribute("queryParams",queryParams);
        return "departamentos/form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Long id,
            @ModelAttribute("filtro") DepartamentoDTO filtro,
            @RequestParam(defaultValue = "0") int page,
            RedirectAttributes redirectAttributes) {

        String queryParams = urlUtils.departamentoQuery(filtro, page);

        if (!usuarioLogadoService.podeGerenciarCadastros()) {
            redirectAttributes.addFlashAttribute("Você não possui permissão para excluir departamentos.");
            return "redirect:/departamentos?" + queryParams;
        }

        try {

            Long idEmpresa = usuarioLogadoService.getEmpresaId();

            departamentoService.excluir(id,idEmpresa);

            redirectAttributes.addFlashAttribute("msgOk","Registro excluído com sucesso!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("msgErro", e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute("msgErro","Erro ao excluir o registro.");
        }

        return "redirect:/departamentos?" + queryParams;
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(
            @PathVariable Long id,
            @ModelAttribute("filtro") DepartamentoDTO filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String acao,
            Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        DepartamentoDTO dto = departamentoService.buscarPorId(id,idEmpresa );

        model.addAttribute("departamento", dto);

        if ("excluir".equals(acao)) {

            boolean departamentoEmUso =   departamentoService.departamentoEstaEmUso(id,idEmpresa);
            model.addAttribute("modoExclusao",true);
            model.addAttribute("departamentoEmUso", departamentoEmUso);
        }

        model.addAttribute("pageTitle", "Departamentos:Detalhes - Cedro ERP");
        model.addAttribute("activeMenu","pessoal");

        String queryParams = urlUtils.departamentoQuery(filtro, page);
        model.addAttribute("queryParams", queryParams);

        return "departamentos/detalhes";
    }

    @PostMapping("/alterar-status/{id}")
    public String alterarStatus(
            @PathVariable Long id,
            @ModelAttribute("filtro") DepartamentoDTO filtro,
            @RequestParam(defaultValue = "0") int page,
            RedirectAttributes redirectAttributes)
    {
        try {

            if (!usuarioLogadoService.podeGerenciarCadastros()) {
                redirectAttributes.addFlashAttribute("msgErro","Você não possui permissão para alterar o status.");
                return "redirect:/departamentos";
            }
            Long idEmpresa =  usuarioLogadoService.getEmpresaId();
            DepartamentoDTO departamento = departamentoService.buscarPorId(id, idEmpresa);
            Boolean novoStatus =!departamento.getStatus();
            departamentoService.alterarStatus(id,idEmpresa,novoStatus);
            redirectAttributes.addFlashAttribute("msgOk",novoStatus
                            ? "Departamento ativado com sucesso!"
                            : "Departamento inativado com sucesso!"
            );


        } catch (Exception e) {

            e.printStackTrace();
            redirectAttributes.addFlashAttribute(
                    "msgErro",
                    "Erro ao alterar o status: " + e.getMessage()
            );
        }
        String queryParams = urlUtils.departamentoQuery(filtro,page);

        return "redirect:/departamentos?" + queryParams;
    }


    @PostMapping("/editar/{id}")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("departamento") DepartamentoDTO dto,
            BindingResult resultDepartamento,
            @ModelAttribute("filtro") DepartamentoDTO filtro,
            @RequestParam(defaultValue = "0") int page,
            Model model,
            RedirectAttributes redirectAttributes)
    {

        if (!usuarioLogadoService.podeGerenciarCadastros()) {
            redirectAttributes.addFlashAttribute(
                    "msgErro",
                    "Você não possui permissão para editar departamentos."
            );
            return "redirect:/departamentos";
        }

        dto.setId(id);
        String queryParams = urlUtils.departamentoQuery(filtro,page);

        if (resultDepartamento.hasErrors()) {
            model.addAttribute("pageTitle","Departamentos: Editar - Cedro ERP");
            model.addAttribute("activeMenu","pessoal");
            model.addAttribute("queryParams",queryParams);
            return "departamentos/form";
        }


        try {

            Long idEmpresa =usuarioLogadoService.getEmpresaId();
            departamentoService.atualizar(id,dto,idEmpresa);

            redirectAttributes.addFlashAttribute(
                    "msgOk",
                    "Registro alterado com sucesso!"
            );


        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute(
                    "msgErro",
                    e.getMessage()
            );


        } catch (Exception e) {

            e.printStackTrace();
            redirectAttributes.addFlashAttribute(
                    "msgErro",
                    "Erro ao alterar o registro."
            );
        }

        return "redirect:/departamentos?" + queryParams;
    }

}
