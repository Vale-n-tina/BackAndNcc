package it.ncc.BackAndNcc.prenotazioni;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "prenotazioni")
@AllArgsConstructor
@NoArgsConstructor
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
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
    private Double price;
    

}
