package it.ncc.BackAndNcc.prenotazioni;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDataRequest {
    @DecimalMin(value = "100", message = "Distanza minima 100 metri")
    private double distanceM;
    @Min(value = 1, message = "Deve esserci almeno 1 passeggero")
    @Max(value = 8, message = "Massimo 8 passeggeri consentiti")
    private int passengers;
    private int suitcases;
    private int backpack;
    private String pickUpTime;
    private String childSeats;
}
