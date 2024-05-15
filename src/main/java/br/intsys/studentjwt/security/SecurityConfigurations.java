package br.intsys.studentjwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.intsys.studentjwt.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// Configuracoes de autenticacao
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// Configuracoes de autorizacao
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().mvcMatchers(HttpMethod.POST, "/auth").permitAll()
				.mvcMatchers(HttpMethod.POST, "/auth/*").permitAll()
				.mvcMatchers(HttpMethod.POST, "/student").hasRole("TESTE")
				.mvcMatchers(HttpMethod.PUT, "/student/*").hasRole("TESTE")
				.mvcMatchers(HttpMethod.DELETE, "/student/*").hasRole("TESTE")
				.mvcMatchers(HttpMethod.POST, "/usuario").permitAll()
				.mvcMatchers(HttpMethod.GET, "/usuario/verify").permitAll()
				.mvcMatchers(HttpMethod.GET, "/usuario/perfil/*").hasRole("ADMINISTRADOR")
				.mvcMatchers(HttpMethod.POST, "/usuario/perfil/*").hasRole("ADMINISTRADOR")
				.mvcMatchers(HttpMethod.DELETE, "/usuario/perfil/*").hasRole("ADMINISTRADOR")
				.mvcMatchers(HttpMethod.POST, "/password/forgot").permitAll()
				.mvcMatchers(HttpMethod.GET, "/password/read").permitAll()
				.mvcMatchers(HttpMethod.POST, "/password/reset").permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(new AuthenticationFilter(jwtService, usuarioRepository),
						UsernamePasswordAuthenticationFilter.class);
	}

	// Configuracoes de recursos estaticos(js, css, imagens, etc.)
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**",
				"/swagger-resources/**");
	}
}