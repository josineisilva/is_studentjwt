package br.intsys.studentjwt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.intsys.studentjwt.model.Usuarioperfil;

public interface UsuarioperfilRepository extends JpaRepository<Usuarioperfil, Usuarioperfil> {
    List<Usuarioperfil> findByUsuarioOrderByPerfil(Integer usuario);
    Optional<Usuarioperfil> findByUsuarioAndPerfil(Integer usuario, Integer perfil);
}