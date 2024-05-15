package br.intsys.studentjwt.service;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import br.intsys.studentjwt.model.Usuario;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender sender;

	@Value("${mail.smtp.user}")
	private String from;

	public void send(String subject, String content, List<Usuario> users) {
		for (Usuario user : users) {
			String body = content;
			try {
				body = body.replace("%username%", user.getName().trim());
				MimeMessage mail = sender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mail);
				helper.setFrom(from);
				helper.setTo(user.getEmail());
				helper.setSubject(subject);
				helper.setText(body, true);
				sender.send(mail);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Erro ao enviar e-mail: " + e.getMessage());
			}
		}
	}
}