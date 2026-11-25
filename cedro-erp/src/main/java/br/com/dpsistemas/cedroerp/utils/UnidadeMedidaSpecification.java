package br.com.dpsistemas.cedroerp.utils;

import br.com.dpsistemas.cedroerp.models.UnidadeMedida;
import org.springframework.data.jpa.domain.Specification;

public class UnidadeMedidaSpecification {

    public static Specification<UnidadeMedida> empresaIgual(Long idEmpresa) {
        return (root, query, cb) -> cb.equal(root.get("empresa").get("id"), idEmpresa);
    }

    public static Specification<UnidadeMedida> siglaContem(String sigla) {
        return (root, query, cb) -> {
            if (sigla == null || sigla.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("sigla")), "%" + sigla.toLowerCase() + "%");
        };
    }

    public static Specification<UnidadeMedida> descricaoContem(String descricao) {
        return (root, query, cb) -> {
            if (descricao == null || descricao.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
        };
    }

    public static Specification<UnidadeMedida> statusIgual(Boolean status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }
}