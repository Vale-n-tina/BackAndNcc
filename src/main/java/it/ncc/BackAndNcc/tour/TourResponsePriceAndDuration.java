package it.ncc.BackAndNcc.tour;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourResponsePriceAndDuration {
    private double price;
    private String duration;
}