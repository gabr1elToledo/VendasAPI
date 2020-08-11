package com.qintess.curso.domain.repository;

import com.qintess.curso.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepo extends JpaRepository<Produto, Integer> {
}
