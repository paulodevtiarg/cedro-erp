package br.com.dpsistemas.cedroerp.utils;

import br.com.dpsistemas.cedroerp.dtos.ClienteDTO;
import br.com.dpsistemas.cedroerp.dtos.DepartamentoDTO;
import br.com.dpsistemas.cedroerp.dtos.UsuarioDTO;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class UrlUtils {

    public String departamentoQuery(
            DepartamentoDTO filtro,
            int page) {

        return String.format(
                "filtroNome=%s&filtroStatus=%s&size=%s&page=%d",

                encode(
                        filtro.getFiltroNome() != null
                                ? filtro.getFiltroNome()
                                : ""
                ),

                filtro.getFiltroStatus() != null
                        ? filtro.getFiltroStatus()
                        : "",

                filtro.getSize() != null
                        ? filtro.getSize()
                        : 10,

                page
        );
    }

    public String usuarioQuery(
            UsuarioDTO filtro,
            int page) {

        return String.format(
                "filtroNome=%s&filtroCpf=%s&filtroStatus=%s&size=%s&page=%d",

                encode(
                        filtro.getFiltroNome() != null
                                ? filtro.getFiltroNome()
                                : ""
                ),

                encode(
                        filtro.getFiltroCpf() != null
                                ? filtro.getFiltroCpf()
                                : ""
                ),

                filtro.getFiltroStatus() != null
                        ? filtro.getFiltroStatus()
                        : "",

                filtro.getSize() != null
                        ? filtro.getSize()
                        : 10,

                page
        );
    }

    public String clienteQuery(
            ClienteDTO filtro,
            int page) {
        return String.format(
                "filtroNome=%s"
                        + "&filtroCpf=%s"
                        + "&filtroCnpj=%s"
                        + "&filtroRazaoSocial=%s"
                        + "&filtroCidade=%s"
                        + "&filtroStatus=%s"
                        + "&size=%s"
                        + "&page=%d",
                encode(filtro.getFiltroNome() != null ? filtro.getFiltroNome(): ""),
                encode(filtro.getFiltroCpf() != null ? filtro.getFiltroCpf() : ""),
                encode(filtro.getFiltroCnpj() != null ? filtro.getFiltroCnpj() : ""),
                encode(filtro.getFiltroRazaoSocial() != null ? filtro.getFiltroRazaoSocial() : ""),
                encode(filtro.getFiltroCidade() != null ? filtro.getFiltroCidade() : ""),
                filtro.getFiltroStatus() != null ? filtro.getFiltroStatus() : "", filtro.getSize() != null? filtro.getSize() : 10,
                page
        );
    }
    private String encode(String valor) {

        if (valor == null) {
            return "";
        }

        return URLEncoder.encode(
                valor,
                StandardCharsets.UTF_8
        );
    }
}