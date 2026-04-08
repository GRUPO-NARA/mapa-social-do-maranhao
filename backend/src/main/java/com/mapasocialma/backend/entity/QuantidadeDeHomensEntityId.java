package com.mapasocialma.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor

public class QuantidadeDeHomensEntityId implements Serializable {
    private static final long serialVersionUID = 4581076261209893520L;

    @Size(max = 7)
    @Column(name = "cod_municipio", length = 7)
    private String codMunicipio;

    @Column(name = "referencia", length = Integer.MAX_VALUE)
    private String referencia;

    @Column(name = "fonte", length = Integer.MAX_VALUE)
    private String fonte;

    @Column(name = "indicador", length = Integer.MAX_VALUE)
    private String indicador;

    @Column(name = "valor")
    private Long valor;

    @Column(name = "unidade", length = Integer.MAX_VALUE)
    private String unidade;

}

