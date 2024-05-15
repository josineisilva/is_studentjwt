package br.intsys.studentjwt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.intsys.studentjwt.model.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
	@Query(value = "SELECT perfil.* FROM perfil,usuarioperfil "
			+ "WHERE perfil.id=usuarioperfil.perfil AND usuarioperfil.usuario=?1", nativeQuery = true)
	List<Perfil> findByUsuario(Integer usuario_id);
}