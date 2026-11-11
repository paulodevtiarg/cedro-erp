package br.com.dpsistemas.cedroerp.controllers;


import br.com.dpsistemas.cedroerp.dtos.UsuarioSessaoDTO;
import br.com.dpsistemas.cedroerp.models.Usuario;
import br.com.dpsistemas.cedroerp.services.ClienteService;
import br.com.dpsistemas.cedroerp.services.UsuarioLogadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {
	@Autowired
	private ClienteService clienteService; // Injete o ClienteService
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;
	@GetMapping
	public String home( @RequestParam(required = false) Integer ano,
	                    HttpSession session,
	                    Model model) {

		// DEPOIS (Correto):
		UsuarioSessaoDTO usuario = (UsuarioSessaoDTO) session.getAttribute("usuarioLogado");
		if (usuario == null) {
			return "redirect:/login";
		}

		//Contagem de clientes
		// 🔥 AQUI ESTÁ A MÁGICA:
		// Pega o ID da empresa (ajuste o nome do atributo se no seu sistema for diferente)
		Long idEmpresa = usuarioLogadoService.getEmpresaId(); // ou usuario.getIdEmpresa()

		// Busca a contagem real no banco
		long totalClientes = clienteService.contarPorEmpresa(idEmpresa);
		// ==========================================
		// MOCK DE DADOS PARA A DASHBOARD (MADEIREIRA)
		// ==========================================
		model.addAttribute("faturamentoMes", "45.500,00");
		model.addAttribute("volumeMes", 120);
		model.addAttribute("pedidosPendentes", 8);
		model.addAttribute("totalClientes", totalClientes); //ja dinamico
		model.addAttribute("estoqueToras", 450);
		model.addAttribute("estoquePatio", 300);
		model.addAttribute("estoqueSerrada", 200);
		model.addAttribute("perdasMes", 5);
		model.addAttribute("cargasTransito", 3);
		model.addAttribute("totalTodasAno", 1450);

		// Gráfico de Linha (Vendas em m³ no ano)
		String vendasJson = "{\"1\":50, \"2\":80, \"3\":120, \"4\":90, \"5\":150, \"6\":200, " +
				"\"7\":180, \"8\":220, \"9\":250, \"10\":300, \"11\":280, \"12\":350}";
		model.addAttribute("internacoesJson", vendasJson);

		// Gráfico de Linha (Faturamento em R$ no ano)
		String faturamentoJson = "{\"1\":15000, \"2\":25000, \"3\":40000, \"4\":30000, \"5\":50000, \"6\":70000, " +
				"\"7\":60000, \"8\":80000, \"9\":90000, \"10\":110000, \"11\":100000, \"12\":130000}";
		model.addAttribute("internacoesGeralJson", faturamentoJson);

		// Gráfico de Pizza (Tipos de Madeira)
		String pizzaJson = "{\"Madeira Serrada\":45, \"Tábuas\":30, \"Dormentes\":15, \"Lenha\":10}";
		model.addAttribute("internacoesPorUnidadeJson", pizzaJson);

		// Gráfico de Barras (Estoque de Madeira Bruta)
		Map<String, Long> estoqueBrutoMock = new HashMap<>();
		estoqueBrutoMock.put("Tora de Pinus", 80L);
		estoqueBrutoMock.put("Tora de Eucalipto", 60L);
		estoqueBrutoMock.put("Tora Nativa", 40L);
		model.addAttribute("idadeUnidade", estoqueBrutoMock);

		// Gráfico de Barras (Estoque de Madeira Processada)
		Map<String, Long> estoqueProcessadoMock = new HashMap<>();
		estoqueProcessadoMock.put("Sarrafo", 70L);
		estoqueProcessadoMock.put("Ripa", 50L);
		estoqueProcessadoMock.put("Pontalete", 30L);
		model.addAttribute("idadeGeral", estoqueProcessadoMock);

		// Gráfico de Cidades (Top Compradores)
		String cidadesJson = "{\"Palmas\":150, \"Araguaína\":120, \"Gurupi\":80, \"Porto Nacional\":60, \"Paraíso\":40}";
		model.addAttribute("cidadeUnidadeJson", cidadesJson);
		model.addAttribute("cidadeGeralJson", cidadesJson);

		// Cores para as barras (usando o método corPorIndice que você já tem)
		List<String> cores = new ArrayList<>();
		for (int i = 0; i < 5; i++) cores.add(corPorIndice(i));
		model.addAttribute("coresUnidade", cores);
		model.addAttribute("coresGeral", cores);

		model.addAttribute("pageTitle", "Home - Cedro ERP");
		model.addAttribute("activeMenu", "dashboard");
		return "home/index";
	}
	private String corPorIndice(int i) {
		switch (i) {
			case 0: return "bg-danger";
			case 1: return "bg-warning";
			case 2: return "bg-info";
			case 3: return "bg-success";
			default: return "bg-primary";
		}
	}



}