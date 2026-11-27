package br.com.dpsistemas.cedroerp.specifications;

import br.com.dpsistemas.cedroerp.enumerators.ClassificacaoProdutoEnum;
import br.com.dpsistemas.cedroerp.models.Produto;
import org.springframework.data.jpa.domain.Specification;

public class ProdutoSpecification {

    public static Specification<Produto> empresaIgual(Long idEmpresa) {
        return (root, query, cb) -> cb.equal(root.get("empresa").get("id"), idEmpresa);
    }

    public static Specification<Produto> especieContem(String especie) {
        return (root, query, cb) -> {
            if (especie == null || especie.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("especieMadeira").get("nome")), "%" + especie.toLowerCase() + "%");
        };
    }

    public static Specification<Produto> tipoContem(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null || tipo.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("tipoProduto").get("nome")), "%" + tipo.toLowerCase() + "%");
        };
    }

    public static Specification<Produto> statusIgual(Boolean status) {
        return (root, query, cb) -> {
            if (status == null) return cb.conjunction();
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Produto> classificacaoIgual(String classificacao) {
        return (root, query, cb) -> {
            if (classificacao == null || classificacao.isBlank()) return cb.conjunction();
            return cb.equal(root.get("classificacao"), ClassificacaoProdutoEnum.valueOf(classificacao));
        };
    }
}