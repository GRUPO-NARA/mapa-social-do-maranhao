package mpma.mapa.entity.Demograficos;

import jakarta.persistence.*;
import mpma.mapa.entity.Estadual.ReferenciaCodigosMunicipaisEntity;

@Entity
@Table(name = "populacao_residente_em_favelas", schema = "demograficos")
public class PopulacaoResidenteEmFavelaEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_municipio")
    private ReferenciaCodigosMunicipaisEntity codMunicipio;

    @Column(name = "referencia", length = Integer.MAX_VALUE)
    private String referencia;

    @Column(name = "fonte", length = Integer.MAX_VALUE)
    private String fonte;

    @Column(name = "indicador", length = Integer.MAX_VALUE)
    private String indicador;

    @Column(name = "valor")
    private Long valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReferenciaCodigosMunicipaisEntity getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(ReferenciaCodigosMunicipaisEntity codMunicipio) {
        this.codMunicipio = codMunicipio;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

}