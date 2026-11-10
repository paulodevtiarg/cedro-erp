package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.ClienteDTO;
import br.com.dpsistemas.cedroerp.dtos.UsuarioDTO;
import br.com.dpsistemas.cedroerp.enumerators.*;
import br.com.dpsistemas.cedroerp.exceptions.ClienteJaCadastradoException;
import br.com.dpsistemas.cedroerp.services.ClienteService;
import br.com.dpsistemas.cedroerp.services.EmpresaService;
import br.com.dpsistemas.cedroerp.services.MunicipioService;
import br.com.dpsistemas.cedroerp.services.UsuarioLogadoService;
import br.com.dpsistemas.cedroerp.utils.UrlUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
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
import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MunicipioService municipioService;
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
    /*
    * Action de novo Registro
    * 02/10/2026
    *
    * */
    @GetMapping("/novo")
    public String novo(Model model)
    {
        ClienteDTO clienteDTO = new ClienteDTO();
        model.addAttribute("cliente", clienteDTO);
        model.addAttribute("tipoPessoas", TipoPessoaEnum.values());
        model.addAttribute("generos", GeneroEnum.values());
        model.addAttribute("estadosCivil", EstadoCivilEnum.values());
        model.addAttribute("estados", EstadosEnum.values());
        model.addAttribute("regimeTributario", RegimeTributarioEnum.values());
        model.addAttribute("municipios", List.of()); // AJAX carrega ao trocar UF
        model.addAttribute("pageTitle", "Cliente :Novo - Cedro ERP");
        model.addAttribute("activeMenu", "cadastros");

        return "clientes/form";
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

    @PostMapping("/salvar")
    public String salvar(HttpSession session,
                         @Valid @ModelAttribute("cliente") ClienteDTO dto,
                         BindingResult resulCliente,
                         Model model,
                         RedirectAttributes redirectAttributes)throws IOException
    {
        // ============================================================
        // 1) Se Bean Validation falhou, volta pro form com os erros
        // ============================================================
        if (resulCliente.hasErrors()) {

            prepararFormulario(dto, model);
            return "clientes/form";
        }
        // ============================================================
        // 2) Tenta salvar — o service valida unicidade (CPF/CNPJ)
        // ============================================================
        try {
            Long idEmpresa = usuarioLogadoService.getEmpresaId();

            clienteService.salvar(dto, idEmpresa);

            redirectAttributes.addFlashAttribute("msgOk", "Cliente salvo com sucesso!");


            return "redirect:/clientes";

        } catch (ClienteJaCadastradoException e) {

            // 3) Joga o erro no campo certo (cpf ou cnpj)
            resulCliente.rejectValue(e.getCampo(), "erro." + e.getCampo(), e.getMessage());

            // Se o service retornou o ID do cliente existente, guarda pra tela mostrar link
            if (e.getIdClienteExistente() != null) {
                model.addAttribute("idClienteExistente", e.getIdClienteExistente());
            }

            prepararFormulario(dto, model);
            return "clientes/form";

        } catch (IllegalArgumentException | EntityNotFoundException e) {

            // 4) Outros erros de negócio → erro global (banner no topo do form)
            model.addAttribute("msgErro", e.getMessage());
            prepararFormulario(dto, model);
            return "clientes/form";
        }

    }

    private void prepararFormulario(ClienteDTO dto, Model model) {

        model.addAttribute("cliente", dto);
        model.addAttribute("tipoPessoas", TipoPessoaEnum.values());
        model.addAttribute("generos", GeneroEnum.values());
        model.addAttribute("estadosCivil", EstadoCivilEnum.values());
        model.addAttribute("estados", EstadosEnum.values());
        model.addAttribute("regimeTributario", RegimeTributarioEnum.values());

        // Carrega municípios do UF selecionado (para o select não vir vazio após erro)
        if (dto.getUf() != null) {
            model.addAttribute("municipios",
                    municipioService.buscarPorUf(dto.getUf().name()));
        } else {
            model.addAttribute("municipios", List.of());
        }

        boolean ehNovo = (dto.getId() == null);
        model.addAttribute("pageTitle",
                "Cliente :" + (ehNovo ? "Novo" : "Editar") + " - Cedro ERP");
        model.addAttribute("activeMenu", "cadastros");
    }




}
