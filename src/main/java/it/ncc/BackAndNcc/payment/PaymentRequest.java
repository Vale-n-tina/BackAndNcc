package it.ncc.BackAndNcc.payment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long amount; // Importo in euro
    private String currency;
    private String prenotazioneEmail;
}
