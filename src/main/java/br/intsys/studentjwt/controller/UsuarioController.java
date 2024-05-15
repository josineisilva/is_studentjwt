package br.intsys.studentjwt.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.intsys.studentjwt.dto.UsuarioGetDTO;
import br.intsys.studentjwt.dto.UsuarioPostDTO;
import br.intsys.studentjwt.dto.UsuarioperfilDTO;
import br.intsys.studentjwt.model.Perfil;
import br.intsys.studentjwt.model.Usuario;
import br.intsys.studentjwt.model.Usuarioperfil;
import br.intsys.studentjwt.repository.PerfilRepository;
import br.intsys.studentjwt.repository.UsuarioRepository;
import br.intsys.studentjwt.repository.UsuarioperfilRepository;
import br.intsys.studentjwt.service.EmailService;
import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private PerfilRepository perfilRepository;
	@Autowired
	private UsuarioperfilRepository usuarioperfilRepository;
	@Autowired
	private EmailService emailService;

	@PostMapping
	@Transactional
	public ResponseEntity<?> post(@RequestBody @Valid UsuarioPostDTO body, UriComponentsBuilder uriBuilder) {
		ResponseEntity<?> ret = ResponseEntity.badRequest().build();
		boolean inserir = false;
		String password = body.getPassword();
		String confirm = body.getConfirm();
		if (password.equals(confirm)) {
			String email = body.getEmail();
			Optional<Usuario> search = repository.findByEmail(email);
			if (search.isPresent()) {
				Usuario usuario = search.get();
				if (!usuario.isVerified()) {
					repository.delete(usuario);
					inserir = true;
				} else
					ret = ResponseEntity.unprocessableEntity().build();
			} else
				inserir = true;
			if (inserir) {
				Usuario item = new Usuario();
				body.update(item);
				item.setVerified(false);
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				String encodedPassword = passwordEncoder.encode(password);
				item.setPassword(encodedPassword);
				String token = getToken();
				LocalDateTime limite = LocalDateTime.now().plusHours(12);
				item.setVerifyToken(token);
				item.setVerifyLimit(limite);
				System.out.println("Inserindo " + email);
				repository.save(item);
				URI verifyUri = uriBuilder.path("/usuario/verify").buildAndExpand(token).toUri();
				String link = verifyUri + "?token=" + token;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
				String content = "<p>Ola %username%,</p>" + "<p>Voce solicitou a inclusão do seu e-mail.</p>"
						+ "<p>Click no link abaixo para verificar o e-mail:</p>" + "<p><a href=\"" + link
						+ "\">Verificar email</a></p>" + "<p>Esse link sera válido até " + limite.format(formatter)
						+ "</p>" + "<p>Ignore esse email se nao foi voce que solicitou a inclusão do usuário.</p>";
				List<Usuario> users = new ArrayList<>();
				users.add(item);
				emailService.send("Verificação de usuário", content, users);
				URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(item.getId()).toUri();
				ret = ResponseEntity.created(uri).body(new UsuarioGetDTO(item));
			}
		}
		return ret;
	}

	@GetMapping("/perfil/{id}")
	@Transactional
	public ResponseEntity<List<Perfil>> getPefil(@PathVariable Integer id) {
		ResponseEntity<List<Perfil>> ret = ResponseEntity.notFound().build();
		Optional<Usuario> search = repository.findById(id);
		if (search.isPresent()) {
			List<Perfil> lista = perfilRepository.findByUsuario(id);
			ret = ResponseEntity.ok(lista);
		} else
			System.out.println("Usuario nao encontrado");
		return ret;
	}

	@PostMapping("/perfil/{id}")
	@Transactional
	public ResponseEntity<String> addPerfil(@PathVariable Integer id, @RequestBody @Valid UsuarioperfilDTO newData) {
		Optional<Usuario> search = repository.findById(id);
		if (search.isPresent()) {
			String errorMsg = "";
			Integer perfil_id = newData.getPerfil_id();
			Optional<Usuarioperfil> other = usuarioperfilRepository.findByUsuarioAndPerfil(id, perfil_id);
			if (!other.isPresent()) {
				Optional<Perfil> perfilSearch = perfilRepository.findById((long) perfil_id);
				if (perfilSearch.isPresent()) {
					Usuarioperfil usuarioperfil = new Usuarioperfil(id, perfil_id);
					usuarioperfilRepository.save(usuarioperfil);
					return ResponseEntity.ok("Perfil inserido no usuario");
				} else
					errorMsg = "Perfil invalido";
			} else
				errorMsg = "Perfil ja existe para o usuario";
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMsg);
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/perfil/{id}")
	@Transactional
	public ResponseEntity<String> removePerfil(@PathVariable Integer id, @RequestBody @Valid UsuarioperfilDTO newData) {
		Optional<Usuario> search = repository.findById(id);
		if (search.isPresent()) {
			String errorMsg = "";
			Integer perfil_id = newData.getPerfil_id();
			Optional<Usuarioperfil> optional = usuarioperfilRepository.findByUsuarioAndPerfil(id, perfil_id);
			if (optional.isPresent()) {
				Usuarioperfil item = optional.get();
				usuarioperfilRepository.delete(item);
				return ResponseEntity.ok("Perfil removido do usuario");
			} else
				errorMsg = "Perfil nao cadastrado para o usuario";
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMsg);
		}
		return ResponseEntity.notFound().build();
	}

	String getToken() {
		String ret = "";
		while (true) {
			ret = RandomString.make(30);
			Optional<Usuario> search = repository.findByVerifyToken(ret);
			if (!search.isPresent()) {
				break;
			}
		}
		return ret;
	}
}