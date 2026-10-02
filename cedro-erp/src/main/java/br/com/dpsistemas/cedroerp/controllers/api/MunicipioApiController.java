package br.com.dpsistemas.cedroerp.controllers.api;


import br.com.dpsistemas.cedroerp.dtos.MunicipioDTO;
import br.com.dpsistemas.cedroerp.dtos.MunicipioSelectDTO;
import br.com.dpsistemas.cedroerp.services.MunicipioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/municipios")
public class MunicipioApiController {

    @Autowired
    private MunicipioService municipioService;

    /**
     * Endpoint chamado via AJAX ao alterar a UF no formulário
     * Exemplo: GET /api/municipios/TO
     */
    @GetMapping("/{uf}")
    public ResponseEntity<List<MunicipioSelectDTO>> listarPorUf(@PathVariable String uf) {
        List<MunicipioSelectDTO> municipios = municipioService.buscarPorUf(uf);
        return ResponseEntity.ok(municipios);
    }

    /**
     * Endpoint para buscar dados do município pelo Código IBGE (após consulta no ViaCEP)
     * Exemplo: GET /api/municipios/ibge/1702109
     */
    @GetMapping("/ibge/{codigoIbge}")
    public ResponseEntity<MunicipioDTO> buscarPorCodigoIbge(@PathVariable String codigoIbge) {
        return municipioService.buscarPorCodigoIbge(codigoIbge)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}