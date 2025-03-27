package it.ncc.BackAndNcc.mail;



import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.hibernate.pretty.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${MAIL_USER}")
    private String aziendaEmail;


    public void sendEmailWithReplyTo(String to, String subject, String body, String replyTo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom(aziendaEmail);
        helper.setReplyTo(replyTo);

        mailSender.send(message);
    }
    // Per email senza replyTo (come le conferme)
    public void sendSimpleEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom(aziendaEmail);

        mailSender.send(message);
    }


    public void sendEmail(String to, String subject, String body, File attachment) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        //SimpleMailMessage message = new SimpleMailMessage();

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setFrom(aziendaEmail);
        helper.addAttachment("booking_details.pdf", attachment);
        mailSender.send(message);
        System.out.println("Email inviata con successo a " + to);
    }
}

