package it.ncc.BackAndNcc.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymantController {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;



    @PostMapping("/create-payment-intent")
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    public PaymentIntentResponse createPaymentIntent(@RequestBody PaymentRequest request) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount() )
                .setCurrency("eur")
                .setDescription(request.getPrenotazioneEmail())
                .build();


        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return new PaymentIntentResponse(paymentIntent.getClientSecret());
    }

    @PostMapping("/confirm-payment")
    @ResponseStatus(HttpStatus.OK)
    public PaymentConfirmationResponse confirmPayment(@RequestBody PaymentConfirmationRequest request) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        PaymentIntent paymentIntent = PaymentIntent.retrieve(request.getPaymentIntentId());

        return new PaymentConfirmationResponse(
                paymentIntent.getId(),
                paymentIntent.getStatus(),
                paymentIntent.getAmount() / 100.0
        );
    }

}
