package it.ncc.BackAndNcc.tour;


import it.ncc.BackAndNcc.mail.EmailService;
import it.ncc.BackAndNcc.mail.PdfGenerator;
import it.ncc.BackAndNcc.prenotazioni.Prenotazione;
import it.ncc.BackAndNcc.prenotazioni.PrenotazioneRequest;
import it.ncc.BackAndNcc.prenotazioni.PrenotazioniResponse;
import it.ncc.BackAndNcc.prenotazioni.PriceDataRequest;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@ControllerAdvice
@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository tourRepository;
    private final EmailService emailService;


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

        return tours.stream()
                .map(tour -> {
                    TourResponse response = new TourResponse();
                    BeanUtils.copyProperties(tour, response); // Copia le proprietà
                    return response;
                })
                .sorted(Comparator.comparing(response -> LocalTime.parse(response.getTime())))
                .collect(Collectors.toList());

    }


    public void bookNow(@Valid TourResponse request){
        Tour tour = new Tour();
        BeanUtils.copyProperties(request, tour);
        tourRepository.save(tour);
        try {
            String htmlContent = "<html>"
                    + "<body style='background-color: #f4f7f6; font-family: Arial, sans-serif; padding: 30px; margin: 0; color: #333;'>"
                    + "<div style='max-width: 800px; margin: 0 auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);'>"
                    + "<h1 style='color:#292A2D; margin-botton:40px; text-align: center'>"
                    + " <strong> Booking Details <strong/>"
                    + "</h1>"
                    + "<h5 style='font-size: 18px; margin-bottom: 20px; font-weight: 300; color: #444; line-height: 1.6;text-align: center'>"
                    + "Hello " + request.getPassengerName() + ", below you will find all the details of your reservation. If you have any questions, feel free to reply to this email."
                    + "</h5>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Reservation ID:</strong> " + tour.getId() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Passenger Name:</strong>"
                    + "<span style='background-color: #ffff99; padding: 6px 10px; border-radius: 4px; font-weight: bold; color: #333;'>"
                    + request.getPassengerName() + ""
                    + "</span>"
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Passengers:</strong> " + request.getPassengers() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Date:</strong> " + request.getDate() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Hour:</strong> " + request.getTime() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Pickup:</strong> " + request.getPickUp() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Dropoff:</strong> " + request.getDropOff() + ""
                    + "</p>"
                    + "<!-- Email -->"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Email:</strong> " + request.getEmail() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Phone:</strong> " + "+" + request.getPhoneNumber() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Time tour:</strong> " + request.getDuration()+ "hours" + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Price:</strong> " + request.getPrice() + "€" + ""
                    + "</p>"
                    + "<!-- Footer -->"
                    + "<div style='text-align: center; margin-top: 40px; font-size: 14px; color: #888; margin-bottom: 30px;'>"
                    + "<p style='margin: 0;'>Thank you for choosing our services!</p>"
                    + "<p style='margin-top: 5px;'>For further assistance, don't hesitate to reach out to us.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

              String filePath = "booking_details.pdf";
              PdfGenerator.generateHtmlPdf(filePath, htmlContent);

            File pdfFile = new File(filePath);

            emailService.sendEmail(request.getEmail(), "Confirm booking","File pdf", pdfFile );
            pdfFile.delete();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteTour(Long tourId) {

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour non trovato"));
        tour.getOptionalStops().clear();
        tourRepository.save(tour);
        tourRepository.delete(tour);
    }

    public TourResponse updateTour(Long id, Tour request) {
        Tour existingTour = tourRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour non trovato con ID: " + id));
        BeanUtils.copyProperties(request, existingTour, "id");
        Tour updatedTour = tourRepository.save(existingTour);
        TourResponse response = new TourResponse();
        BeanUtils.copyProperties(updatedTour, response);

        return response;

    }

}
