package it.ncc.BackAndNcc.prenotazioni;

import it.ncc.BackAndNcc.mail.EmailService;
import it.ncc.BackAndNcc.mail.PdfGenerator;
import it.ncc.BackAndNcc.tour.Tour;
import it.ncc.BackAndNcc.tour.TourResponse;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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
public class PrenotazioneService {
    private final PrenotazioneRepository prenotazioneRepository;
    private final EmailService emailService;
    @Value("${company.email}")
    private String companyEmail;


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
        try {
            String htmlContent = "<html>"
                    + "<body style='background-color: #f4f7f6; font-family: Arial, sans-serif; padding: 30px; margin: 0; color: #333;'>"
                    + "<div style='max-width: 800px; margin: 0 auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);'>"
                    + "<h1 style='color:#292A2D; margin-botton:40px; text-align: center'>"
                    + " <strong> Booking Details <strong/>"
                    + "</h1>"
                    + "<p style='font-size: 14px; margin-bottom: 20px; font-weight: 300; color: #444; line-height: 1.6;'>"
                    + "Hello " + request.getNameAndSurname() + ", below you will find all the details of your reservation. If you have any questions, feel free to reply to this email."
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Reservation ID:</strong> " + prenotazione.getId() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Passenger Name:</strong>"
                    + "<span style='background-color: #ffff99; padding: 6px 10px; border-radius: 4px; font-weight: bold; color: #333;'>"
                    + request.getNameAndSurname() + ""
                    + "</span>"
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Passengers:</strong> " + request.getPassengers() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Date:</strong> " + request.getPickUpDate() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Hour:</strong> " + request.getPickUpTime() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Pickup:</strong> " + request.getPickUp() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Dropoff:</strong> " + request.getDropOff() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Email:</strong> " + request.getEmail() + ""
                    + "</p>"
                    + "<p style='color: #444; margin-left: 20px; font-size: 16px; line-height: 1.8;'>"
                    + "<strong style='color: #333;'>Phone:</strong> " + "+" + request.getPhone() + ""
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

            emailService.sendEmail(request.getEmail(), "Confirm booking","Hi "+ request.getNameAndSurname() +  "," +" Thank you for booking with us! Your reservation has been confirmed. Please find the details attached.", pdfFile );

            emailService.sendEmail(companyEmail, "Nuova prenotazione" + prenotazione.getId() ,"vedere allegato pdf" ,pdfFile);
            pdfFile.delete();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


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

    public PrenotazioniResponse updatePrenotazione(Long id,@Valid Prenotazione request) {
        Prenotazione existingTour = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non trovato con ID: " + id));
        BeanUtils.copyProperties(request, existingTour, "id");
        Prenotazione updatePrenotazione = prenotazioneRepository.save(existingTour);
        PrenotazioniResponse response = new PrenotazioniResponse();
        BeanUtils.copyProperties(updatePrenotazione, response);

        return response;

    }


    public PrenotazioniResponse getPrenotazioniById(Long id) {
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovato con ID: " + id));

        PrenotazioniResponse response = new PrenotazioniResponse();
        BeanUtils.copyProperties(prenotazione, response);

        return response;

    }

}
