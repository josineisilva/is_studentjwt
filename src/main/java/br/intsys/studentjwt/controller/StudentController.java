package br.intsys.studentjwt.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.intsys.studentjwt.dto.StudentPostDTO;
import br.intsys.studentjwt.model.Student;
import br.intsys.studentjwt.repository.StudentRepository;

@RestController
@RequestMapping("/student")
public class StudentController {
	@Autowired
	private StudentRepository repository;

	@GetMapping
	public List<Student> get() {
		return repository.findAll(Sort.by("id").ascending());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Student> getById(@PathVariable Integer id) {
		ResponseEntity<Student> ret = ResponseEntity.notFound().build();
		Optional<Student> search = repository.findById(id);
		if (search.isPresent()) {
			ret = ResponseEntity.ok(search.get());
		} else
			System.out.println("Student nao contrado");
		return ret;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<Student> post(@RequestBody @Valid StudentPostDTO body, UriComponentsBuilder uriBuilder) {
		Student item = new Student();
		body.update(item);
		repository.save(item);
		URI uri = uriBuilder.path("/student/{id}").buildAndExpand(item.getId()).toUri();
		return ResponseEntity.created(uri).body(item);
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Student> put(@PathVariable Integer id, @RequestBody @Valid StudentPostDTO body) {
		ResponseEntity<Student> ret = ResponseEntity.badRequest().build();
		Optional<Student> search = repository.findById(id);
		if (search.isPresent()) {
			Student item = search.get();
			body.update(item);
			ret = ResponseEntity.ok(item);
		} else {
			System.out.println("Student nao encontrada");
			ret = ResponseEntity.notFound().build();
		}
		return ret;
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		ResponseEntity<?> ret = ResponseEntity.badRequest().build();
		Optional<Student> search = repository.findById(id);
		if (search.isPresent()) {
			repository.delete(search.get());
			ret = ResponseEntity.ok().build();
		} else {
			System.out.println("Student nao encontrada");
			ret = ResponseEntity.notFound().build();
		}
		return ret;
	}
}