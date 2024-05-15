package br.intsys.studentjwt.validator;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.intsys.studentjwt.anotations.PerfilFK;
import br.intsys.studentjwt.model.Perfil;
import br.intsys.studentjwt.repository.PerfilRepository;

public class PerfilFKValidator implements ConstraintValidator<PerfilFK, Integer> {
	@Autowired
	private PerfilRepository repository;

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		Optional<Perfil> search = repository.findById((long) value);
		if (search.isPresent()) {
			return true;
		}
		return false;
	}
}