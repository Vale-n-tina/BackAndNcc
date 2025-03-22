package it.ncc.BackAndNcc.prenotazioni;

import it.ncc.BackAndNcc.tour.Tour;
import it.ncc.BackAndNcc.tour.TourResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@ControllerAdvice
@Service
@RequiredArgsConstructor
public class PrenotazioneService {
    private final PrenotazioneRepository prenotazioneRepository;

    public double priceCalculation(@Valid PriceDataRequest request) {

        double distanceKm = request.getDistanceM() / 1000.0;


        double pricePerKm;
        if (distanceKm < 10) {
            pricePerKm = 5.0;
        } else if (distanceKm >= 10 && distanceKm < 20) {
            pricePerKm = 3.0;
        } else if (distanceKm >= 20 && distanceKm < 40) {
            pricePerKm = 2.3;
        } else {
            pricePerKm = 1.6;
        }


        double basePrice = distanceKm * pricePerKm;

        int passengers = request.getPassengers();
        int totalLuggage = request.getSuitcases();

        double passengerSupplement = 0.0;
        double luggageSupplement = 0.0;


        if (passengers == 3 || passengers == 4) {
            passengerSupplement += 12.0; // Supplemento per 3 o 4 persone, indipendentemente dai bagagli
        } else if (passengers >= 5 && passengers <= 7) {
            passengerSupplement += 20.0; // Supplemento di 20 € per 5-7 persone
        } else if (passengers == 8) {
            passengerSupplement += 20.0 + 10.0; // Supplemento per 7 persone (20 €) + 10 €
        }


        if (totalLuggage > passengers) {
            luggageSupplement += 5.0 * (totalLuggage - passengers); // Supplemento per ogni bagaglio in più rispetto ai passeggeri
        }


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


        double totalPrice = basePrice + passengerSupplement + luggageSupplement + childSeatSupplement;

        int roundedPrice = (int) Math.round(totalPrice);

        return roundedPrice;
    }

    public void bookNow(@Valid PrenotazioneRequest request){
        Prenotazione prenotazione = new Prenotazione();
        BeanUtils.copyProperties(request, prenotazione);
        prenotazioneRepository.save(prenotazione);


    }
    public List<PrenotazioniResponse> getPrenotazioniByDate(String date) {
        List<Prenotazione> prenotazioni = prenotazioneRepository.findByPickUpDate(date);
        System.out.println(prenotazioni);
        return prenotazioni.stream()
                .map(prenotazione -> {
                    PrenotazioniResponse response = new PrenotazioniResponse();
                    BeanUtils.copyProperties(prenotazione, response); // Copia le proprietà
                    return response;
                })
                .sorted(Comparator.comparing(response -> LocalTime.parse(response.getPickUpTime())))
                .collect(Collectors.toList()); // Raccogli i risultati in una lista;

    }

    public void deletePrenotazione(Long tourId) {
        // Trova il Tour
        Prenotazione prenotazione= prenotazioneRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour non trovato"));

       prenotazioneRepository.delete(prenotazione);
    }

    public PrenotazioniResponse updatePrenotazione(Long id, Prenotazione request) {
        Prenotazione existingTour = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovato con ID: " + id));
        BeanUtils.copyProperties(request, existingTour, "id");
        Prenotazione updatePrenotazione = prenotazioneRepository.save(existingTour);
        PrenotazioniResponse response = new PrenotazioniResponse();
        BeanUtils.copyProperties(updatePrenotazione, response);

        return response;

    }

}
