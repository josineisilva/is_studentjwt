package br.intsys.studentjwt.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.intsys.studentjwt.model.Usuario;

public class UsuarioPostDTO {
	@NotBlank
	private String name;
	@Email
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private String confirm;
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getConfirm() {
		return confirm;
	}
	
	public void update(Usuario obj) {
		obj.setName(name);
		obj.setEmail(email);
	}
}