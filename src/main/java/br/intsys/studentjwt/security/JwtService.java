package br.intsys.studentjwt.security;

import java.util.Date;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import br.intsys.studentjwt.model.Usuario;
import br.intsys.studentjwt.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.bytebuddy.utility.RandomString;

@Service
public class JwtService {
	@Autowired
	private UsuarioRepository repository;

	@Value("${jwt.expiration}")
	private String expiration;

	@Transactional
	public String generate(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
		int usuario_id = ((Usuario) authentication.getPrincipal()).getId();
		Usuario user = repository.findById(usuario_id).get();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		String secret = RandomString.make(200);
		user.setSecret(secret);
		return Jwts.builder().setSubject(user.getId().toString()).setExpiration(dataExpiracao)
				.claim("id", user.getId())
				.claim("name", user.getName())
				.claim("roles", authorities)
				.signWith(SignatureAlgorithm.HS256, user.jwtSecret()).compact();
	}

	public boolean validate(String jwt) {
		try {
			Usuario user = getUsuario(jwt);
			Jwts.parser().setSigningKey(user.jwtSecret()).parseClaimsJws(jwt);
			return true;
		} catch (Exception e) {
			System.out.println("Falha na autenticacao: " + e.getMessage());
			return false;
		}
	}

	public Integer autenticate(String jwt) {
		System.out.println("JWT Autenticando");
		Usuario user = getUsuario(jwt);
		Claims claims = Jwts.parser().setSigningKey(user.jwtSecret()).parseClaimsJws(jwt).getBody();
		System.out.println("Usuario: " + claims.get("id", Integer.class));
		System.out.println("Name: " + claims.get("name", String.class));
		return Integer.parseInt(claims.getSubject());
	}

	public Usuario getUsuario(String jwt) {
		Usuario ret = null;
		if (jwt != null) {
			String[] splitJwt = jwt.split("\\.");
			String unsignedJwt = splitJwt[0] + "." + splitJwt[1] + ".";
			Jwt<?, ?> parsedJwt = Jwts.parser().parse(unsignedJwt);
			Claims claims = (Claims) parsedJwt.getBody();
			int usuario_id = Integer.parseInt(claims.getSubject());
			ret = repository.findById(usuario_id).get();
		}
		return ret;
	}
}