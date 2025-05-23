package it.ncc.BackAndNcc.prenotazioni;

import it.ncc.BackAndNcc.tour.Tour;
import it.ncc.BackAndNcc.tour.TourResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {
    public final PrenotazioneService prenotazioneService;
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @PostMapping("/price-calculation")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll()")
    public double priceCalculation(@RequestBody PriceDataRequest request) {
        return prenotazioneService.priceCalculation(request);
    }


    @PostMapping("/bookNow")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public void bookNow(@RequestBody PrenotazioneRequest request) {
        prenotazioneService.bookNow(request);
    }


    @GetMapping("/by-date")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PrenotazioniResponse> getPrenotazioniByDate(@RequestParam String date) {
        return prenotazioneService.getPrenotazioniByDate(date);

    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deletePrenotazione(@PathVariable Long id) {
        prenotazioneService.deletePrenotazione(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PrenotazioniResponse updatePrenotazione(@PathVariable Long id, @RequestBody Prenotazione request) {
        return prenotazioneService.updatePrenotazione(id, request);
    }

    @GetMapping("/by-id/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PrenotazioniResponse getPrenotazioniById(@PathVariable Long id) {
        return prenotazioneService.getPrenotazioniById(id);

    }

    @GetMapping("/maps-key")
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)// O qualsiasi ruolo tu voglia richiedere
    public ApiKeyResponse getGoogleMapsApiKey() {
        return new ApiKeyResponse(googleMapsApiKey);
    }


    @PutMapping("/{id}/update-driver")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Prenotazione> updateDriverDetails(
            @PathVariable Long id,
            @RequestBody DriverDetailsRequest updateDTO) {

        Prenotazione updated = prenotazioneService.updateDriverDetails(id, updateDTO);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PrenotazioniResponse> searchPrenotazioni(@RequestParam String keyword){
        return prenotazioneService.searchPrenotazioniByKeyword(keyword);
    }



}
