package it.ncc.BackAndNcc.tour;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourResponse {
    private Long id;
    private String pickUp;
    private String dropOff;
    private int passengers;
    private String date;
    private String time;
    private List<String> optionalStops;
    private String passengerName;
    private String email;
    private String phoneNumber;
    private double price;
    private String startLocation;
    private String endLocation;
    private String duration;

}
