package br.com.dpsistemas.cedroerp.mappers;

import br.com.dpsistemas.cedroerp.dtos.ClienteDTO;
import br.com.dpsistemas.cedroerp.models.Cliente;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.Municipio;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDTO toDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }

        ClienteDTO dto = new ClienteDTO();

        // CHAVES E RELACIONAMENTOS
        dto.setId(entity.getId());

        if (entity.getEmpresa() != null) {
            dto.setIdEmpresa(entity.getEmpresa().getId());
            dto.setNomeEmpresa(entity.getEmpresa().getRazaoSocial()); // ou getNomeFantasia()
        }

        if (entity.getMunicipio() != null) {
            dto.setIdMunicipio(entity.getMunicipio().getId());
        }

        // DADOS PRINCIPAIS
        dto.setTipoPessoa(entity.getTipoPessoa());
        dto.setNome(entity.getNome());
        dto.setRazaoSocial(entity.getRazaoSocial());
        dto.setCpf(entity.getCpf());
        dto.setCnpj(entity.getCnpj());
        dto.setInscricaoEstadual(entity.getInscricaoEstadual());
        dto.setEmailPrincipal(entity.getEmailPrincipal());
        dto.setTelefone(entity.getTelefone());
        dto.setCelular(entity.getCelular());
        dto.setWhatsapp(entity.getWhatsapp());

        // ENDEREÇO
        dto.setCep(entity.getCep());
        dto.setLogradouro(entity.getLogradouro());
        dto.setNumero(entity.getNumero());
        dto.setComplemento(entity.getComplemento());
        dto.setBairro(entity.getBairro());
        dto.setCidade(entity.getCidade());
        dto.setUf(entity.getUf());
        dto.setPais(entity.getPais());

        // PESSOA FÍSICA
        dto.setDataNascimentoInicio(entity.getDataNascimentoInicio());
        dto.setGenero(entity.getGenero());
        dto.setEstadoCivil(entity.getEstadoCivil());

        // PESSOA JURÍDICA
        dto.setCnae(entity.getCnae());
        dto.setRegimeTributario(entity.getRegimeTributario());
        dto.setMei(entity.getMei());

        // OBSERVAÇÕES
        dto.setObs(entity.getObs());

        // ATRIBUTOS HERDADOS DO BASEENTITY
        dto.setStatus(entity.getStatus());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setDataAlteracao(entity.getDataAlteracao());

        return dto;
    }

    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }

        Cliente entity = new Cliente();

        // CHAVES E RELACIONAMENTOS
        entity.setId(dto.getId());

        if (dto.getIdEmpresa() != null) {
            Empresa empresa = new Empresa();
            empresa.setId(dto.getIdEmpresa());
            entity.setEmpresa(empresa);
        }

        if (dto.getIdMunicipio() != null) {
            Municipio municipio = new Municipio();
            municipio.setId(dto.getIdMunicipio());
            entity.setMunicipio(municipio);
        } else {
            entity.setMunicipio(null);
        }

        // DADOS PRINCIPAIS
        entity.setTipoPessoa(dto.getTipoPessoa());
        entity.setNome(dto.getNome());
        entity.setRazaoSocial(dto.getRazaoSocial());
        entity.setCpf(dto.getCpf());
        entity.setCnpj(dto.getCnpj());
        entity.setInscricaoEstadual(dto.getInscricaoEstadual());
        entity.setEmailPrincipal(dto.getEmailPrincipal());
        entity.setTelefone(dto.getTelefone());
        entity.setCelular(dto.getCelular());
        entity.setWhatsapp(dto.getWhatsapp());

        // ENDEREÇO
        entity.setCep(dto.getCep());
        entity.setLogradouro(dto.getLogradouro());
        entity.setNumero(dto.getNumero());
        entity.setComplemento(dto.getComplemento());
        entity.setBairro(dto.getBairro());
        entity.setCidade(dto.getCidade());
        entity.setUf(dto.getUf());
        entity.setPais(dto.getPais());

        // PESSOA FÍSICA
        entity.setDataNascimentoInicio(dto.getDataNascimentoInicio());
        entity.setGenero(dto.getGenero());
        entity.setEstadoCivil(dto.getEstadoCivil());

        // PESSOA JURÍDICA
        entity.setCnae(dto.getCnae());
        entity.setRegimeTributario(dto.getRegimeTributario());
        entity.setMei(dto.getMei());

        // OBSERVAÇÕES
        entity.setObs(dto.getObs());

        // ATRIBUTOS HERDADOS DO BASEENTITY
        entity.setStatus(dto.getStatus());
        entity.setDataCadastro(dto.getDataCadastro());
        entity.setDataAlteracao(dto.getDataAlteracao());

        return entity;
    }
}