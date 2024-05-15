package br.intsys.studentjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.intsys.studentjwt.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByResetpassToken(String token);
    Optional<Usuario> findByVerifyToken(String token);
}