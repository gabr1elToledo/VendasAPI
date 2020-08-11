package com.qintess.curso.domain.repository;

import com.qintess.curso.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepo extends JpaRepository<Usuario,Integer> {

    Optional<Usuario> findByLogin(String login);
}
