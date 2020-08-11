package com.qintess.curso.service.impl;

import com.qintess.curso.domain.entity.Cliente;
import com.qintess.curso.domain.entity.ItemPedido;
import com.qintess.curso.domain.entity.Pedido;
import com.qintess.curso.domain.entity.Produto;
import com.qintess.curso.domain.enums.StatusPedido;
import com.qintess.curso.domain.repository.ClienteRepo;
import com.qintess.curso.domain.repository.ItemPedidoRepo;
import com.qintess.curso.domain.repository.PedidoRepo;
import com.qintess.curso.domain.repository.ProdutoRepo;
import com.qintess.curso.exception.PedidoNaoEncontradoException;
import com.qintess.curso.exception.RegraNegocioException;
import com.qintess.curso.rest.dto.ItemPedidoDTO;
import com.qintess.curso.rest.dto.PedidoDTO;
import com.qintess.curso.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepo pedidoRepo;
    private final ClienteRepo clienteRepo;
    private final ProdutoRepo produtoRepo;
    private final ItemPedidoRepo itemPedidoRepo;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
       Cliente cliente = clienteRepo.findById(idCliente).orElseThrow(() -> new RegraNegocioException("Código do cliente inválido!"));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItens(pedido, dto.getItens());
        pedidoRepo.save(pedido);
        itemPedidoRepo.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepo.findByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizaStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepo.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);
            return pedidoRepo.save(pedido);
        }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItens(Pedido pedido ,List<ItemPedidoDTO> itens){
        if(itens.isEmpty()){
            throw new RegraNegocioException("Não é possivel realizar um pedido sem itens");
        }

        return itens.stream().map(dto -> {
            Integer idProduto = dto.getProduto();
            Produto produto =produtoRepo.findById(idProduto).orElseThrow(() -> new RegraNegocioException("Código de produto inválido: " + idProduto));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            return itemPedido;
        }).collect(Collectors.toList());
    }



}
