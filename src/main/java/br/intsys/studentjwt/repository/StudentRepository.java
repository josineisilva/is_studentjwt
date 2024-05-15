package br.intsys.studentjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.intsys.studentjwt.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}