package it.ncc.BackAndNcc.tour;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDataRequestTour {
    private List<String> optionalStops;
    @Min(value = 1, message = "Deve esserci almeno 1 passeggero")
    @Max(value = 8, message = "Massimo 8 passeggeri consentiti")
    private int passengers;
    @NotBlank(message = "La località di partenza è obbligatoria")
    private String startLocation;
    @NotBlank(message = "La località di arrivo è obbligatoria")
    private String endLocation;
}
