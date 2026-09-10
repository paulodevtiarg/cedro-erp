package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.CnaeDTO;
import br.com.dpsistemas.cedroerp.dtos.CnaeListaDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CnaeService {

    private final ObjectMapper objectMapper;

    private List<CnaeDTO> cnaes = Collections.emptyList();

    private Map<String, CnaeDTO> cnaePorCodigo = Collections.emptyMap();

    public CnaeService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void carregarCnaes() {

        try {

            ClassPathResource resource =
                    new ClassPathResource("data/cnaes.json");

            CnaeListaDTO dados = objectMapper.readValue(
                    resource.getInputStream(),
                    CnaeListaDTO.class
            );

            this.cnaes = dados.getListaCnae();

            this.cnaePorCodigo = cnaes.stream()
                    .collect(Collectors.toMap(
                            CnaeDTO::getCodigo,
                            Function.identity(),
                            (cnae1, cnae2) -> cnae1
                    ));

            System.out.println(
                    "CNAEs carregados: " + cnaes.size()
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Erro ao carregar arquivo de CNAEs.",
                    e
            );
        }
    }

    public List<CnaeDTO> listarTodos() {
        return cnaes;
    }

    public CnaeDTO buscarPorCodigo(String codigo) {

        if (codigo == null || codigo.isBlank()) {
            return null;
        }

        return cnaePorCodigo.get(codigo);
    }
}
