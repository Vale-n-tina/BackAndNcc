package it.ncc.BackAndNcc.tour;


import it.ncc.BackAndNcc.prenotazioni.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByDate(String Date);
}
