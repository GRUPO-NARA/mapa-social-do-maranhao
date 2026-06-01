package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que representa a evolução do IDH municipal ao longo do tempo.
 *
 * Contém a referência temporal e o valor de IDH para aquele período.
 */
public record EvolucaoIDHMunicipalDTO(
        String referencia,
        @JsonProperty("IDH")
        Double IDH
) {
}
