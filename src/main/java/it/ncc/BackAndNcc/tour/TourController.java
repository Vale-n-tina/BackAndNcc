package it.ncc.BackAndNcc.tour;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

}
