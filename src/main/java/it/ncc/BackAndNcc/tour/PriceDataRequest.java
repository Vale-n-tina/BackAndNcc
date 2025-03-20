package it.ncc.BackAndNcc.tour;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDataRequest {
    private List<String> optionalStops;
    private int passengers;
    private String startLocation;
    private String endLocation;
}
