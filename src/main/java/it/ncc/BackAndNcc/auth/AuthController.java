package it.ncc.BackAndNcc.auth;

import it.ncc.BackAndNcc.error.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService customUserDetailsService;
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
//        appUserService.registerUser(
//                registerRequest.getUsername(),
//                registerRequest.getPassword(),
//                Set.of(Role.ROLE_USER) // Assegna il ruolo di default
//        );
//        return ResponseEntity.ok("Registrazione avvenuta con successo");
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = appUserService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/verifyToken")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authorizationHeader) {
      try

          {System.out.println("Authorization Header: " + authorizationHeader);

          // Estrae il token dall'header
          String token = authorizationHeader.substring(7).replace("\"", "");
              System.out.println("Token: " + token);// Rimuove "Bearer "


          // Ottiene il nome utente dal token
          String username = jwtTokenUtil.getUsernameFromToken(token);
              System.out.println(username);
          // Carica i dettagli dell'utente
          UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

          // Verifica la validità del token
          if (jwtTokenUtil.validateToken(token, userDetails)) {
              return ResponseEntity.ok().body("Token valido");
          } else {
              throw new InvalidTokenException("Token non valido o scaduto");
          }}
       catch (Exception e) {
          // Lancia un'eccezione generica in caso di altri errori
          throw new RuntimeException("Errore durante la verifica del token: " + e.getMessage());
      }

    }

}
