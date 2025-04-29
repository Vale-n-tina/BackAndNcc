package it.ncc.BackAndNcc.prenotazioni;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverDetailsRequest {
    private String driverName;
    private String driverPhone;
    private String driverDetails;
    @Schema(description = "Stato pagamento autista", defaultValue = "false")
    private Boolean driverPaid = false; // Inizializza a false
}