package it.ncc.BackAndNcc.payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmationResponse {

    private String paymentId;
    private String status;
    private Double amount;


}
