package br.intsys.studentjwt.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import br.intsys.studentjwt.model.Student;

public class StudentPostDTO {
	@NotBlank
	private String name;
	private Integer age;
	@Email
	private String email;

	public String getName() {
		return name;
	}

	public Integer getAge() {
		return age;
	}

	public String getEmail() {
		return email;
	}

	public void update(Student obj) {
		obj.setName(name);
		obj.setAge(age);
		obj.setEmail(email);
	}
}
