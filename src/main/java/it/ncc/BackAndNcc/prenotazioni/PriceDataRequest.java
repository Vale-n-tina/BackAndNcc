package it.ncc.BackAndNcc.prenotazioni;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDataRequest {
    private double distanceM;
    private int passengers;
    private int suitcases;
    private int backpack;
    private String pickUpTime;
    private String childSeats;
}
