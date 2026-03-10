package br.com.wss.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import br.com.wss.services.model.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(Email email) throws MessagingException {
        Context context = new Context();

        if (email.getVariables() != null) {
            context.setVariables(email.getVariables());
        }

        String htmlContent = templateEngine.process(email.getTemplate(), context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        helper.setText(htmlContent, true); // true = HTML

        mailSender.send(message);
    }
}
