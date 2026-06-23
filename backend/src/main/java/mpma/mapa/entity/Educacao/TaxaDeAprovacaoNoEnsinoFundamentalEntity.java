package mpma.mapa.entity.Educacao;

import jakarta.persistence.*;
import mpma.mapa.entity.Estadual.ReferenciaCodigosMunicipaisEntity;

@Entity
@Table(name = "taxa_de_aprovacao_no_ensino_fundamental", schema = "educacao")
public class TaxaDeAprovacaoNoEnsinoFundamentalEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_municipio")
    private ReferenciaCodigosMunicipaisEntity codMunicipio;

    @Column(name = "referencia")
    private Long referencia;

    @Column(name = "fonte", length = Integer.MAX_VALUE)
    private String fonte;

    @Column(name = "indicador", length = Integer.MAX_VALUE)
    private String indicador;

    @Column(name = "valor")
    private Double valor;

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

    public Long getReferencia() {
        return referencia;
    }

    public void setReferencia(Long referencia) {
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

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}