package it.ncc.BackAndNcc.payment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentIntentResponse {

    private String clientSecret;



}
