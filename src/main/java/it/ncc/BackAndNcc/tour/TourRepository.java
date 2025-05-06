package it.ncc.BackAndNcc.tour;


import it.ncc.BackAndNcc.prenotazioni.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByDate(String Date);

    @Query("SELECT p FROM Tour p WHERE " +
            "LOWER(p.pickUp) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.dropOff) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.passengerName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +

            "LOWER(p.startLocation) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.endLocation) LIKE LOWER(CONCAT('%', :search, '%')) OR " +

            "LOWER(p.driverName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +

            "LOWER(p.driverDetails) LIKE LOWER(CONCAT('%', :search, '%'))"

           )
    List<Tour> searchByKeyword(String search);
}
