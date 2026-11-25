package br.com.dpsistemas.cedroerp.specifications;

import br.com.dpsistemas.cedroerp.models.Cliente;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class ClienteSpecification {

    private ClienteSpecification() {
    }

    public static Specification<Cliente> empresaIgual(Long idEmpresa) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("empresa").get("id"),
                        idEmpresa
                );
    }

    public static Specification<Cliente> nomeContem(String nome) {

        return (root, query, cb) -> {

            if (nome == null || nome.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.upper(
                            root.get("nome")
                    ),
                    "%" + nome.trim().toUpperCase(Locale.ROOT) + "%"
            );
        };
    }

    public static Specification<Cliente> cpfContem(String cpf) {

        return (root, query, cb) -> {

            if (cpf == null || cpf.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    root.get("cpf"),
                    "%" + cpf + "%"
            );
        };
    }

    public static Specification<Cliente> cnpjContem(String cnpj) {

        return (root, query, cb) -> {

            if (cnpj == null || cnpj.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    root.get("cnpj"),
                    "%" + cnpj + "%"
            );
        };
    }

    public static Specification<Cliente> razaoSocialContem(
            String razaoSocial) {

        return (root, query, cb) -> {

            if (razaoSocial == null || razaoSocial.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.upper(
                            root.get("razaoSocial")
                    ),
                    "%" +
                            razaoSocial.trim()
                                    .toUpperCase(Locale.ROOT) +
                            "%"
            );
        };
    }

    public static Specification<Cliente> cidadeContem(
            String cidade) {

        return (root, query, cb) -> {

            if (cidade == null || cidade.isBlank()) {
                return cb.conjunction();
            }

            return cb.like(
                    cb.upper(
                            root.get("cidade")
                    ),
                    "%" +
                            cidade.trim()
                                    .toUpperCase(Locale.ROOT) +
                            "%"
            );
        };
    }

    public static Specification<Cliente> statusIgual(
            Boolean status) {

        return (root, query, cb) -> {

            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(
                    root.get("status"),
                    status
            );
        };
    }
}