package it.ncc.BackAndNcc.prenotazioni;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByPickUpDate(String pickUpDate);
    @Query("SELECT p FROM Prenotazione p WHERE " +
            "LOWER(p.pickUp) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.dropOff) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.nameOnBoard) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.nameAndSurname) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.phone) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.driverName) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Prenotazione> searchByKeyword(String search);
}
