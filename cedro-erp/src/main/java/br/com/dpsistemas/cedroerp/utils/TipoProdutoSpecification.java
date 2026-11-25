package br.com.dpsistemas.cedroerp.utils;

import br.com.dpsistemas.cedroerp.models.TipoProduto;
import org.springframework.data.jpa.domain.Specification;

public class TipoProdutoSpecification {

    public static Specification<TipoProduto> empresaIgual(Long idEmpresa) {
        return (root, query, cb) -> cb.equal(root.get("empresa").get("id"), idEmpresa);
    }

    public static Specification<TipoProduto> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<TipoProduto> statusIgual(Boolean status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }
}