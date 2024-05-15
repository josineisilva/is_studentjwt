package br.intsys.studentjwt.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(Usuarioperfil.class)
public class Usuarioperfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer usuario;

	@Id
	private Integer perfil;

    public Usuarioperfil() {
    }
    
    public Usuarioperfil(Integer usuario, Integer perfil) {
            this.usuario = usuario;
            this.perfil = perfil;
    }
	
	public Integer getUsuario() {
		return usuario;
	}
	public void setUsuario(Integer usuario) {
		this.usuario = usuario;
	}

	public Integer getPerfil() {
		return perfil;
	}
	public void setPerfil(Integer perfil) {
		this.perfil = perfil;
	}
}