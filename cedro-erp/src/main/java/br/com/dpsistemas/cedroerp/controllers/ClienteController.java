package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.ClienteDTO;
import br.com.dpsistemas.cedroerp.dtos.UsuarioDTO;
import br.com.dpsistemas.cedroerp.services.ClienteService;
import br.com.dpsistemas.cedroerp.services.EmpresaService;
import br.com.dpsistemas.cedroerp.services.UsuarioLogadoService;
import br.com.dpsistemas.cedroerp.utils.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @Autowired
    private ClienteService clienteService;


    @Autowired
    private UrlUtils urlUtils;



    @GetMapping
    public String listar(@ModelAttribute("filtro") ClienteDTO filtro,
                         @RequestParam(defaultValue = "0") int page,
                         Model model ) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        filtro.setPage(page);
        Page<ClienteDTO> clientes = clienteService.listar(filtro,idEmpresa);
        model.addAttribute("pageTitle", "Clientes - Cedro ERP");
        model.addAttribute("clientes",clientes);
        model.addAttribute("activeMenu", "cadastros");
        model.addAttribute("queryParams", urlUtils.clienteQuery(filtro, page));
        return "clientes/index";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(
            @PathVariable Long id,
            @ModelAttribute("filtro") ClienteDTO filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String acao,
            Model model) {
        Long idEmpresa = usuarioLogadoService.getEmpresaId();
        ClienteDTO dto = clienteService.buscarPorId(id,idEmpresa );

        model.addAttribute("cliente", dto);
        if ("excluir".equals(acao)) {
            model.addAttribute("modoExclusao",true);
        }

        model.addAttribute("pageTitle", "Clientes: Detalhes - Cedro ERP");
        model.addAttribute("activeMenu","cadastros");
        model.addAttribute("queryParams", urlUtils.clienteQuery(filtro, page));

        return "clientes/detalhes";
    }


}
