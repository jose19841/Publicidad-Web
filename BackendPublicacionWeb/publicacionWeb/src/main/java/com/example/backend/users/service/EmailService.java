package com.example.backend.users.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Enviar email en texto plano
     */
    public void sendPlain(String to, String subject, String content) {
        log.info("Enviando email en texto plano a: {} con asunto: {}", to, subject);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom("info@imperial-net.com");
            mailSender.send(message);
            log.info("Email de texto plano enviado exitosamente a: {}", to);
        } catch (Exception e) {
            log.error("Error enviando email en texto plano a: {}", to, e);
            throw new RuntimeException("Error enviando correo de texto plano", e);
        }
    }

    /**
     * Enviar email en formato HTML
     */
    public void sendHtml(String to, String subject, String htmlContent) {
        log.info("Enviando email HTML a: {} con asunto: {}", to, subject);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML
            helper.setFrom("info@imperial-net.com");

            mailSender.send(mimeMessage);
            log.info("Email HTML enviado exitosamente a: {}", to);
        } catch (Exception e) {
            log.error("Error enviando email HTML a: {}", to, e);
            throw new RuntimeException("Error enviando correo HTML", e);
        }
    }
}
