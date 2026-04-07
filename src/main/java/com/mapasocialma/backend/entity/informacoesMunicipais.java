package com.mapasocialma.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "informacoes", schema = "dados_gerais")
public class informacoesMunicipais {
    @Id
    @Size(max = 7)
    @Column(name = "codigo_ibge", nullable = false, length = 7)
    private String codigoIbge;

    @Size(max = 100)
    @NotNull
    @Column(name = "nome_municipio", nullable = false, length = 100)
    private String nomeMunicipio;

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

}