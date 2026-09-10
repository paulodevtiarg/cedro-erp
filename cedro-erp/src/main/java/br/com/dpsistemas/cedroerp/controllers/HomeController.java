package br.com.dpsistemas.cedroerp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class HomeController {


	@GetMapping
	public String home( @RequestParam(required = false) Integer ano,
	                    HttpSession session,
	                    Model model) {
		model.addAttribute("pageTitle", "Home - Cedro ERP");
		model.addAttribute("activeMenu", "dashboard");
		return "home/index";
	}



}