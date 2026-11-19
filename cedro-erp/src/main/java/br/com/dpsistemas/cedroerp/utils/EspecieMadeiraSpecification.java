package br.com.dpsistemas.cedroerp.specifications;

import br.com.dpsistemas.cedroerp.models.EspecieMadeira;
import org.springframework.data.jpa.domain.Specification;

public class EspecieMadeiraSpecification {

    public static Specification<EspecieMadeira> empresaIgual(Long idEmpresa) {
        return (root, query, cb) -> cb.equal(root.get("empresa").get("id"), idEmpresa);
    }

    public static Specification<EspecieMadeira> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<EspecieMadeira> statusIgual(Boolean status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }
}