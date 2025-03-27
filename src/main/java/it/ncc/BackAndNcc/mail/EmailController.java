package it.ncc.BackAndNcc.mail;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    @Value("${MAIL_USER}")
    private String aziendaEmail;


    @PostMapping("/contact")
    public ResponseEntity<?> contactForm(@RequestBody ContactRequest contactRequest) {
        try {
            // Invia email all'azienda
            emailService.sendEmailWithReplyTo(
                    aziendaEmail,
                    "Nuovo messaggio da " + contactRequest.getName(),
                    "Nome: " + contactRequest.getName() + "\n" +
                            "Email: " + contactRequest.getEmail() + "\n\n" +
                            "Messaggio:\n" + contactRequest.getMessage(),
                    contactRequest.getEmail()
            );
            // Invia email al mittente di conferma
            emailService.sendSimpleEmail(
                    contactRequest.getEmail(),
                    "Confirmation of message receipt",
                    "Dear " + contactRequest.getName() + ",\n\n" +
                            "We have received your message and will reply to you as soon as possible.\n\n" +
                            "Here is a copy of your message:\n" +
                            contactRequest.getMessage()
            );

            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Errore durante l'invio dell'email"));
        }
    }
}



