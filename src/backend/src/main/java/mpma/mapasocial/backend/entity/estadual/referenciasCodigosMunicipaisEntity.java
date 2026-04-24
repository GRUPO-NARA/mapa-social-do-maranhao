package mpma.mapasocial.backend.entity.estadual;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "referencias_codigos_municipais", schema = "dados_estadual")
public class referenciasCodigosMunicipaisEntity {
    @Id
    @Column(name = "codigo_ibge", nullable = false, length = Integer.MAX_VALUE)
    private String codigoIbge;

    @Column(name = "municipio", length = Integer.MAX_VALUE)
    private String municipio;

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

}