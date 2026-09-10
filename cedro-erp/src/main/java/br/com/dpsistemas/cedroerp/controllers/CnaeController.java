package br.com.dpsistemas.cedroerp.controllers;

import br.com.dpsistemas.cedroerp.dtos.CnaeDTO;
import br.com.dpsistemas.cedroerp.services.CnaeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CnaeController {
    private final CnaeService cnaeService;

    public CnaeController(CnaeService cnaeService) {
        this.cnaeService = cnaeService;
    }

    @GetMapping("/teste/cnaes")
    public List<CnaeDTO> listar() {
        return cnaeService.listarTodos();
    }
}
