package it.ncc.BackAndNcc.prenotazioni;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
@RequiredArgsConstructor
public class PrenotazioneController {
    public final PrenotazioneService prenotazioneService;

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

}
