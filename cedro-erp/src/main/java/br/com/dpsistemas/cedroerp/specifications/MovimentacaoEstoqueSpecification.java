package br.com.dpsistemas.cedroerp.specifications;

import br.com.dpsistemas.cedroerp.enumerators.TipoMovimentacaoEnum;
import br.com.dpsistemas.cedroerp.models.MovimentacaoEstoque;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class MovimentacaoEstoqueSpecification {

    public static Specification<MovimentacaoEstoque> empresaIgual(Long idEmpresa) {
        return (root, query, cb) -> cb.equal(root.get("empresa").get("id"), idEmpresa);
    }

    public static Specification<MovimentacaoEstoque> dataInicioMaiorIgual(LocalDate dataInicio) {
        return (root, query, cb) -> {
            if (dataInicio == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("dataMovimentacao"), dataInicio.atStartOfDay());
        };
    }

    public static Specification<MovimentacaoEstoque> dataFimMenorIgual(LocalDate dataFim) {
        return (root, query, cb) -> {
            if (dataFim == null) return cb.conjunction();
            // Pega até o final do dia (23:59:59)
            return cb.lessThanOrEqualTo(root.get("dataMovimentacao"), dataFim.atTime(23, 59, 59));
        };
    }

    public static Specification<MovimentacaoEstoque> tipoIgual(String tipo) {
        return (root, query, cb) -> {
            if (tipo == null || tipo.isBlank()) return cb.conjunction();
            return cb.equal(root.get("tipoMovimentacao"), TipoMovimentacaoEnum.valueOf(tipo));
        };
    }

    public static Specification<MovimentacaoEstoque> produtoContem(String produto) {
        return (root, query, cb) -> {
            if (produto == null || produto.isBlank()) return cb.conjunction();

            // Join com a medida, variação base e produto
            Join<Object, Object> medida = root.join("produtoMedida");
            Join<Object, Object> base = medida.join("variacaoBase");
            Join<Object, Object> prod = base.join("produto");
            Join<Object, Object> tipoProd = prod.join("tipoProduto");
            Join<Object, Object> especie = prod.join("especieMadeira");

            // Filtra pelo SKU da medida OU pelo Nome do Produto (Tipo + Espécie)
            return cb.or(
                    cb.like(cb.lower(medida.get("sku")), "%" + produto.toLowerCase() + "%"),
                    cb.like(cb.lower(tipoProd.get("nome")), "%" + produto.toLowerCase() + "%"),
                    cb.like(cb.lower(especie.get("nome")), "%" + produto.toLowerCase() + "%")
            );
        };
    }
}