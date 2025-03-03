package it.ncc.BackAndNcc.prenotazioni;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Validated
@ControllerAdvice
@Service
@RequiredArgsConstructor
public class PrenotazioneService {
    private final PrenotazioneRepository prenotazioneRepository;

    public double priceCalculation(PriceDataRequest request) {

        double distanceKm = request.getDistanceM() / 1000.0;


        double pricePerKm;
        if (distanceKm < 10) {
            pricePerKm = 5.0; // Sotto i 10 km
        } else if (distanceKm >= 10 && distanceKm < 20) {
            pricePerKm = 3.0; // Da 10 a 20 km
        } else if (distanceKm >= 20 && distanceKm < 40) {
            pricePerKm = 2.3; // Da 20 a 40 km
        } else {
            pricePerKm = 1.6; // Oltre i 40 km
        }


        double basePrice = distanceKm * pricePerKm;

        int passengers = request.getPassengers();
        int totalLuggage = request.getSuitcases();

        double passengerSupplement = 0.0;
        double luggageSupplement = 0.0;

        // Supplemento per passeggeri
        if (passengers == 3 || passengers == 4) {
            passengerSupplement += 12.0; // Supplemento per 3 o 4 persone, indipendentemente dai bagagli
        } else if (passengers >= 5 && passengers <= 7) {
            passengerSupplement += 20.0; // Supplemento di 20 € per 5-7 persone
        } else if (passengers == 8) {
            passengerSupplement += 20.0 + 10.0; // Supplemento per 7 persone (20 €) + 10 €
        }

        // Supplemento per bagagli
        if (totalLuggage > passengers) {
            luggageSupplement += 5.0 * (totalLuggage - passengers); // Supplemento per ogni bagaglio in più rispetto ai passeggeri
        }

        // 5. Applicare i supplementi per i seggiolini per bambini
        double childSeatSupplement = 0.0;
        switch (request.getChildSeats()) {
            case "1ChildSeat":
                childSeatSupplement = 8.0; // Supplemento per 1 seggiolino
                break;
            case "2ChildSeat":
                childSeatSupplement = 16.0; // Supplemento per 2 seggiolini
                break;
            case "3ChildSeat":
                childSeatSupplement = 24.0; // Supplemento per 3 seggiolini
                break;
            case "4ChildSeat":
                childSeatSupplement = 32.0; // Supplemento per 4 seggiolini
                break;
            default:
                // "NoChildSeats" o altri casi: nessun supplemento
                childSeatSupplement = 0.0;
                break;
        }

        // 6. Calcolare il prezzo totale
        double totalPrice = basePrice + passengerSupplement + luggageSupplement + childSeatSupplement;

        int roundedPrice = (int) Math.round(totalPrice);

        return roundedPrice;
    }
}
