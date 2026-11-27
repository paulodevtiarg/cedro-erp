package br.com.dpsistemas.cedroerp.services;

import br.com.dpsistemas.cedroerp.dtos.MovimentacaoEstoqueDTO;
import br.com.dpsistemas.cedroerp.enumerators.TipoMovimentacaoEnum;
import br.com.dpsistemas.cedroerp.mappers.MovimentacaoEstoqueMapper;
import br.com.dpsistemas.cedroerp.models.Empresa;
import br.com.dpsistemas.cedroerp.models.MovimentacaoEstoque;
import br.com.dpsistemas.cedroerp.models.ProdutoMedida;
import br.com.dpsistemas.cedroerp.repositorys.MovimentacaoEstoqueRepository;
import br.com.dpsistemas.cedroerp.repositorys.ProdutoMedidaRepository;
import br.com.dpsistemas.cedroerp.specifications.MovimentacaoEstoqueSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MovimentacaoEstoqueService {

    @Autowired private MovimentacaoEstoqueRepository movimentacaoRepository;
    @Autowired private ProdutoMedidaRepository produtoMedidaRepository;
    @Autowired private MovimentacaoEstoqueMapper mapper;
    @Autowired private EmpresaService empresaService;

    @Transactional(readOnly = true)
    public Page<MovimentacaoEstoqueDTO> listar(MovimentacaoEstoqueDTO filtro, Long idEmpresa) {
        // Paginação
        int pageSize = (filtro.getSize() != null && filtro.getSize() > 0) ? filtro.getSize() : 10;
        Pageable pageable = PageRequest.of(filtro.getPage(), pageSize);

        // Monta os filtros dinâmicos
        Specification<MovimentacaoEstoque> spec = MovimentacaoEstoqueSpecification.empresaIgual(idEmpresa)
                .and(MovimentacaoEstoqueSpecification.dataInicioMaiorIgual(filtro.getDataInicio()))
                .and(MovimentacaoEstoqueSpecification.dataFimMenorIgual(filtro.getDataFim()))
                .and(MovimentacaoEstoqueSpecification.tipoIgual(filtro.getFiltroTipo()))
                .and(MovimentacaoEstoqueSpecification.produtoContem(filtro.getFiltroProduto()));

        // Executa a busca
        return movimentacaoRepository.findAll(spec, pageable).map(mapper::toDTO);
    }

    @Transactional
    public void lancarMovimentacao(MovimentacaoEstoqueDTO dto, Long idEmpresa) {
        // Validação básica
        if (dto.getMotivo() == null || dto.getMotivo().trim().isBlank()) {
            throw new IllegalArgumentException("O motivo/documento é obrigatório.");
        }
        if (dto.getQuantidade() == null || dto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        ProdutoMedida medida = produtoMedidaRepository.findById(dto.getProdutoMedidaId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        // Cria o histórico
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProdutoMedida(medida);
        mov.setTipoMovimentacao(dto.getTipoMovimentacao());
        mov.setQuantidade(dto.getQuantidade());
        mov.setMotivo(dto.getMotivo());
        mov.setDataMovimentacao(LocalDateTime.now());
        mov.setEmpresa(empresaService.buscarPorId(idEmpresa));

        // Atualiza o saldo do Produto
        BigDecimal estoqueAtual = medida.getQuantidadeEstoque() != null ? medida.getQuantidadeEstoque() : BigDecimal.ZERO;

        if (dto.getTipoMovimentacao() == TipoMovimentacaoEnum.ENTRADA) {
            medida.setQuantidadeEstoque(estoqueAtual.add(dto.getQuantidade()));
        } else if (dto.getTipoMovimentacao() == TipoMovimentacaoEnum.SAIDA) {
            if (estoqueAtual.compareTo(dto.getQuantidade()) < 0) {
                throw new IllegalArgumentException("Estoque insuficiente para esta saída.");
            }
            medida.setQuantidadeEstoque(estoqueAtual.subtract(dto.getQuantidade()));
        } else if (dto.getTipoMovimentacao() == TipoMovimentacaoEnum.AJUSTE) {
            // No ajuste, a quantidade informada passa a ser o novo saldo real
            medida.setQuantidadeEstoque(dto.getQuantidade());
        }

        movimentacaoRepository.save(mov);
        produtoMedidaRepository.save(medida);
    }
}