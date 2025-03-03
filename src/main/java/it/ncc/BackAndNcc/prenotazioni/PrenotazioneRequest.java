package it.ncc.BackAndNcc.prenotazioni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrenotazioneRequest {
    private String pickUp;
    private String dropOff;
    private int passengers;
    private int suitcases;
    private int backpack;
    private String pickUpDate;
    private String pickUpTime;
    private String transportType;
    private String transportDetails;
    private String nameOnBoard;
    private String childSeats;
    private String requests;
    private String nameAndSurname;
    private String email;
    private String phone;
    private double price;
}
