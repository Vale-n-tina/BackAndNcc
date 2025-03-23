package it.ncc.BackAndNcc.tour;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String pickUp;
    private String dropOff;
    private int passengers;
    private String date;
    private String time;
    @ElementCollection
    private List<String> optionalStops;
    private String passengerName;
    private String email;
    private String phoneNumber;
    private double price;
    private String startLocation;
    private String endLocation;
    private String duration;


}
