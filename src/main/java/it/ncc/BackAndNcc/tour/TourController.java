package it.ncc.BackAndNcc.tour;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {
    private final TourService tourService;
}
