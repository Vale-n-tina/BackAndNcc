package it.ncc.BackAndNcc.prenotazioni;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrenotazioneRequest {
    @NotBlank(message = "Il luogo di partenza è obbligatorio")
    private String pickUp;
    @NotBlank(message = "La destinazione è obbligatoria")
    private String dropOff;
    @Min(value = 1, message = "Deve esserci almeno 1 passeggero")
    @Max(value = 8, message = "Non possono esserci più di 8 passeggeri")
    private int passengers;
    private int suitcases;
    private int backpack;
    @NotBlank(message = "La data di pick-up è obbligatoria")
    private String pickUpDate;
    @NotBlank(message = "L'ora di pick-up è obbligatoria")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Formato ora non valido (HH:MM)")
    private String pickUpTime;
    private String transportType;
    private String transportDetails;
    private String nameOnBoard;
    private String childSeats;
    private String requests;
    @NotBlank(message = "Nome e cognome sono obbligatori")
    private String nameAndSurname;
    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "Formato email non valido")
    private String email;
    private String phone;
    private double price;

}
