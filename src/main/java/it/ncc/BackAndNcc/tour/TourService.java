package it.ncc.BackAndNcc.tour;


import it.ncc.BackAndNcc.prenotazioni.Prenotazione;
import it.ncc.BackAndNcc.prenotazioni.PrenotazioneRequest;
import it.ncc.BackAndNcc.prenotazioni.PrenotazioniResponse;
import it.ncc.BackAndNcc.prenotazioni.PriceDataRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@ControllerAdvice
@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;


    public TourResponsePriceAndDuration priceCalculation(@Valid PriceDataRequestTour request){

        double basePrice = 300;

        // Costo aggiuntivo per persone
        int additionalPassengers = Math.max(0, request.getPassengers() - 1); // Persone oltre la prima
        double passengerCost = additionalPassengers * 10;

        // Costo aggiuntivo per fermate opzionali
        int additionalStops = Math.max(0, request.getOptionalStops().size() - 4); // Fermate oltre le 4 incluse
        double stopCost = additionalStops * 80;

        // Costo aggiuntivo per startLocation e endLocation
        double startLocationCost = calculateLocationCost(request.getStartLocation(), request.getPassengers());
        double endLocationCost = calculateLocationCost(request.getEndLocation(), request.getPassengers());



        // Prezzo totale
        double totalPrice = basePrice + passengerCost + stopCost + startLocationCost + endLocationCost;

        // Calcolo della durata del tour
        String duration = calculateTourDuration(request.getOptionalStops().size());

        // Restituisci un oggetto TourResponse
        return new TourResponsePriceAndDuration(totalPrice, duration);
    }

    private double calculateLocationCost(String location, int passengers) {

        switch (location) {
            case "Rome":
                return 0; // Roma non ha costi aggiuntivi
            case "Fiumicino Airport":
                return calculateAirportCost(passengers, 60, 10);
            case "Ciampino Airport":
                return calculateAirportCost(passengers, 50, 10);
            case "Civitavecchia Dock":
                return calculateDockCost(passengers);
            default:
                throw new IllegalArgumentException("Località non valida: " + location);
        }
    }

    private double calculateAirportCost(int passengers, int baseCost, int increment) {
        if (passengers <= 8) {
            return baseCost + (passengers - 1) * increment; // Cambia (passengers - 2) in (passengers - 1)
        } else {
            return baseCost + 7 * increment; // Massimo 110€ per Fiumicino e 100€ per Ciampino
        }
    }

    private double calculateDockCost(int passengers) {
        if (passengers <= 8) {
            return 125 + (passengers - 1) * 10; // Cambia (passengers - 2) in (passengers - 1)
        } else {
            return 195; // Massimo 195€ per Civitavecchia Dock
        }
    }

    private String calculateTourDuration(int numberOfStops) {
        // Durata base: 4 ore
        int baseDurationHours = 4;
        int baseDurationMinutes = 0;

        // Aggiungi 30 minuti per ogni fermata oltre la quarta
        int additionalStops = Math.max(0, numberOfStops - 4);
        int additionalMinutes = additionalStops * 30;

        // Calcola la durata totale in ore e minuti
        int totalMinutes = baseDurationHours * 60 + baseDurationMinutes + additionalMinutes;
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        // Formatta la durata come "X ore e Y minuti"
        if (minutes == 0) {
            return hours + ""; // Restituisce solo le ore se i minuti sono 0
        } else {
            return hours + ":" + minutes; // Restituisce ore:minuti se i minuti sono diversi da 0
        }
    }

    public List<TourResponse> getTourByDate(String date) {
        List<Tour> tours = tourRepository.findByDate(date);
        System.out.println(tours);
        return tours.stream()
                .map(tour -> {
                    TourResponse response = new TourResponse();
                    BeanUtils.copyProperties(tour, response); // Copia le proprietà
                    return response;
                })
                .collect(Collectors.toList()); // Raccogli i risultati in una lista;

    }

    public void bookNow(@Valid TourResponse request){
        Tour tour = new Tour();
        BeanUtils.copyProperties(request, tour);
        tourRepository.save(tour);


    }

    /*public void deleteTour(Long tourId) {
        // Trova il Tour
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour non trovato"));

        // Svuota la lista optionalStops
        tour.getOptionalStops().clear();

        // Salva il Tour (questo rimuoverà i record dalla tabella tour_optional_stops)
        tourRepository.save(tour);

        // Ora puoi eliminare il Tour
        tourRepository.delete(tour);
    }*/

}
