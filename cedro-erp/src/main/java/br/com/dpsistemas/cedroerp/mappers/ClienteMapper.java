package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.ClienteDTO;
import br.com.dpsistemas.cedroerp.models.Cliente;
import br.com.dpsistemas.cedroerp.models.Empresa;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    /*
     * ==================================================
     * ENTITY -> DTO
     * ==================================================
     */

    public ClienteDTO toDTO(Cliente cliente) {

        if (cliente == null) {
            return null;
        }

        ClienteDTO dto = new ClienteDTO();

        dto.setId(cliente.getId());

        /*
         * EMPRESA
         */
        if (cliente.getEmpresa() != null) {

            dto.setIdEmpresa(
                    cliente.getEmpresa().getId()
            );

            dto.setNomeEmpresa(
                    cliente.getEmpresa().getRazaoSocial()
            );
        }


        /*
         * DADOS PRINCIPAIS
         */
        dto.setTipoPessoa(
                cliente.getTipoPessoa()
        );

        dto.setNome(
                cliente.getNome()
        );

        dto.setRazaoSocial(
                cliente.getRazaoSocial()
        );

        dto.setCpf(
                cliente.getCpf()
        );

        dto.setCnpj(
                cliente.getCnpj()
        );

        dto.setInscricaoEstadual(
                cliente.getInscricaoEstadual()
        );

        dto.setEmailPrincipal(
                cliente.getEmailPrincipal()
        );



        dto.setTelefone(
                cliente.getTelefone()
        );

        dto.setCelular(
                cliente.getCelular()
        );

        dto.setWhatsapp(
                cliente.getWhatsapp()
        );


        /*
         * ENDEREÇO
         */
        dto.setCep(
                cliente.getCep()
        );

        dto.setLogradouro(
                cliente.getLogradouro()
        );

        dto.setNumero(
                cliente.getNumero()
        );

        dto.setComplemento(
                cliente.getComplemento()
        );

        dto.setBairro(
                cliente.getBairro()
        );

        dto.setCidade(
                cliente.getCidade()
        );

        dto.setUf(
                cliente.getUf()
        );

        dto.setPais(
                cliente.getPais()
        );


        /*
         * PESSOA FÍSICA
         */
        dto.setDataNascimentoInicio(
                cliente.getDataNascimentoInicio()
        );

        dto.setGenero(
                cliente.getGenero()
        );

        dto.setEstadoCivil(
                cliente.getEstadoCivil()
        );


        /*
         * PESSOA JURÍDICA
         */
        dto.setCnae(
                cliente.getCnae()
        );

        dto.setRegimeTributario(
                cliente.getRegimeTributario()
        );

        dto.setMei(
                cliente.getMei()
        );


        /*
         * OUTROS
         */
        dto.setObs(
                cliente.getObs()
        );


        /*
         * BASE ENTITY
         */
        dto.setStatus(
                cliente.getStatus()
        );

        dto.setDataCadastro(
                cliente.getDataCadastro()
        );

        dto.setDataAlteracao(
                cliente.getDataAlteracao()
        );


        return dto;
    }


    /*
     * ==================================================
     * DTO -> NOVA ENTITY
     * ==================================================
     */

    public Cliente toEntity(
            ClienteDTO dto,
            Empresa empresa) {

        if (dto == null) {
            return null;
        }

        Cliente cliente = new Cliente();

        cliente.setEmpresa(
                empresa
        );


        /*
         * DADOS PRINCIPAIS
         */
        cliente.setTipoPessoa(
                dto.getTipoPessoa()
        );

        cliente.setNome(
                normalizar(dto.getNome())
        );

        cliente.setRazaoSocial(
                normalizar(dto.getRazaoSocial())
        );

        cliente.setCpf(
                somenteNumeros(dto.getCpf())
        );

        cliente.setCnpj(
                somenteNumeros(dto.getCnpj())
        );

        cliente.setInscricaoEstadual(
                normalizar(dto.getInscricaoEstadual())
        );

        cliente.setEmailPrincipal(
                normalizarEmail(dto.getEmailPrincipal())
        );



        cliente.setTelefone(
                somenteNumeros(dto.getTelefone())
        );

        cliente.setCelular(
                somenteNumeros(dto.getCelular())
        );

        cliente.setWhatsapp(
                Boolean.TRUE.equals(dto.getWhatsapp())
        );


        /*
         * ENDEREÇO
         */
        cliente.setCep(
                somenteNumeros(dto.getCep())
        );

        cliente.setLogradouro(
                normalizar(dto.getLogradouro())
        );

        cliente.setNumero(
                normalizar(dto.getNumero())
        );

        cliente.setComplemento(
                normalizar(dto.getComplemento())
        );

        cliente.setBairro(
                normalizar(dto.getBairro())
        );

        cliente.setCidade(
                normalizar(dto.getCidade())
        );

        cliente.setUf(
                dto.getUf()
        );

        cliente.setPais(
                normalizar(dto.getPais())
        );


        /*
         * PESSOA FÍSICA
         */
        cliente.setDataNascimentoInicio(
                dto.getDataNascimentoInicio()
        );

        cliente.setGenero(
                dto.getGenero()
        );

        cliente.setEstadoCivil(
                dto.getEstadoCivil()
        );


        /*
         * PESSOA JURÍDICA
         */
        cliente.setCnae(
                normalizar(dto.getCnae())
        );

        cliente.setRegimeTributario(
                dto.getRegimeTributario()
        );

        cliente.setMei(
                Boolean.TRUE.equals(dto.getMei())
        );


        /*
         * OUTROS
         */
        cliente.setObs(
                normalizar(dto.getObs())
        );


        /*
         * STATUS
         */
        if (dto.getStatus() != null) {

            cliente.setStatus(
                    dto.getStatus()
            );
        }


        /*
         * NÃO MEXEMOS EM:
         *
         * id
         * dataCadastro
         * dataAlteracao
         *
         * A BaseEntity controla as datas.
         */

        return cliente;
    }


    /*
     * ==================================================
     * DTO -> ATUALIZA ENTITY EXISTENTE
     * ==================================================
     */

    public void updateEntity(
            ClienteDTO dto,
            Cliente cliente,
            Empresa empresa) {

        if (dto == null || cliente == null) {
            return;
        }

        cliente.setEmpresa(
                empresa
        );


        /*
         * DADOS PRINCIPAIS
         */
        cliente.setTipoPessoa(
                dto.getTipoPessoa()
        );

        cliente.setNome(
                normalizar(dto.getNome())
        );

        cliente.setRazaoSocial(
                normalizar(dto.getRazaoSocial())
        );

        cliente.setCpf(
                somenteNumeros(dto.getCpf())
        );

        cliente.setCnpj(
                somenteNumeros(dto.getCnpj())
        );

        cliente.setInscricaoEstadual(
                normalizar(dto.getInscricaoEstadual())
        );

        cliente.setEmailPrincipal(
                normalizarEmail(dto.getEmailPrincipal())
        );


        cliente.setTelefone(
                somenteNumeros(dto.getTelefone())
        );

        cliente.setCelular(
                somenteNumeros(dto.getCelular())
        );

        cliente.setWhatsapp(
                Boolean.TRUE.equals(dto.getWhatsapp())
        );


        /*
         * ENDEREÇO
         */
        cliente.setCep(
                somenteNumeros(dto.getCep())
        );

        cliente.setLogradouro(
                normalizar(dto.getLogradouro())
        );

        cliente.setNumero(
                normalizar(dto.getNumero())
        );

        cliente.setComplemento(
                normalizar(dto.getComplemento())
        );

        cliente.setBairro(
                normalizar(dto.getBairro())
        );

        cliente.setCidade(
                normalizar(dto.getCidade())
        );

        cliente.setUf(
                dto.getUf()
        );

        cliente.setPais(
                normalizar(dto.getPais())
        );


        /*
         * PESSOA FÍSICA
         */
        cliente.setDataNascimentoInicio(
                dto.getDataNascimentoInicio()
        );

        cliente.setGenero(
                dto.getGenero()
        );

        cliente.setEstadoCivil(
                dto.getEstadoCivil()
        );


        /*
         * PESSOA JURÍDICA
         */
        cliente.setCnae(
                normalizar(dto.getCnae())
        );

        cliente.setRegimeTributario(
                dto.getRegimeTributario()
        );

        cliente.setMei(
                Boolean.TRUE.equals(dto.getMei())
        );


        /*
         * OUTROS
         */
        cliente.setObs(
                normalizar(dto.getObs())
        );


        /*
         * STATUS
         */
        if (dto.getStatus() != null) {

            cliente.setStatus(
                    dto.getStatus()
            );
        }


        /*
         * NÃO MEXEMOS EM:
         *
         * id
         * dataCadastro
         * dataAlteracao
         *
         * O ID identifica a entidade já carregada.
         * As datas são controladas pela BaseEntity.
         */
    }


    /*
     * ==================================================
     * NORMALIZAÇÕES
     * ==================================================
     */

    private String normalizar(String valor) {

        if (valor == null) {
            return null;
        }

        String normalizado = valor.trim();

        return normalizado.isEmpty()
                ? null
                : normalizado;
    }


    private String normalizarEmail(String valor) {

        if (valor == null) {
            return null;
        }

        String normalizado =
                valor.trim().toLowerCase();

        return normalizado.isEmpty()
                ? null
                : normalizado;
    }


    private String somenteNumeros(String valor) {

        if (valor == null) {
            return null;
        }

        String normalizado =
                valor.replaceAll("\\D", "");

        return normalizado.isEmpty()
                ? null
                : normalizado;
    }
}