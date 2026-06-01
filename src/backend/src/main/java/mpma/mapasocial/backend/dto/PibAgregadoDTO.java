package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO para retorno de dados agregados do PIB estadual ou regional.
 *
 * Contém a referência temporal e o valor agregado do PIB.
 */
public record PibAgregadoDTO(
        String referencia,
        @JsonProperty("pib_agregado")
        Double valor
) {
}
