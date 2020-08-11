package com.qintess.curso.service;

import com.qintess.curso.domain.entity.Pedido;
import com.qintess.curso.domain.enums.StatusPedido;
import com.qintess.curso.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id , StatusPedido statusPedido);
}
