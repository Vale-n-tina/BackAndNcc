package it.ncc.BackAndNcc.tour;


import it.ncc.BackAndNcc.prenotazioni.PrenotazioneRequest;
import it.ncc.BackAndNcc.prenotazioni.PrenotazioniResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;

    @PostMapping("/price-calculation")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("permitAll()")
    public TourResponsePriceAndDuration calculatePrice(@RequestBody PriceDataRequestTour request){
        return tourService.priceCalculation(request);
    }

    @GetMapping("/by-date")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TourResponse> getPrenotazioniByDate(@RequestParam String date) {
        return tourService.getTourByDate(date);

    }

    @PostMapping("/bookNow")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("permitAll()")
    public void bookNow(@RequestBody TourResponse request) {
        tourService.bookNow(request);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TourResponse updateTour(@PathVariable Long id, @RequestBody Tour request) {
        return tourService.updateTour(id, request);
    }
}
