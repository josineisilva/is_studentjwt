package br.intsys.studentjwt.dto;

import br.intsys.studentjwt.model.Usuario;

public class UsuarioGetDTO {
	private String name;
	private String email;

	public UsuarioGetDTO(Usuario obj) {
		this.name = obj.getName();
		this.email = obj.getEmail();
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
}