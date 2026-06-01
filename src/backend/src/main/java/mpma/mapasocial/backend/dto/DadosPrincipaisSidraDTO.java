package mpma.mapasocial.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO que representa os dados principais do SIDRA retornados pela API.
 *
 * Cada instância contém o indicador consultado, o valor correspondente e a
 * data de coleta associada.
 */
public record DadosPrincipaisSidraDTO(
        @JsonProperty("indicador")
        String indicador,

        @JsonProperty("valor")
        String valor,

        @JsonProperty("data_coleta")
        String data_coleta
) {
}
