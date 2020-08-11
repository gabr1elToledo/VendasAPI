package com.qintess.curso.domain.repository;

import com.qintess.curso.domain.entity.Cliente;
import com.qintess.curso.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface PedidoRepo extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByCliente( Cliente cliente);

    @Query("select p from Pedido p left join fetch p.itens where p.id= :id")
    Optional<Pedido> findByIdFetchItens(@Param("id") Integer id);
}
