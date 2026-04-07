package com.mapasocialma.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
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

    public String getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(String codMunicipio) {
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

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantidadeDeHomensEntityId entity = (QuantidadeDeHomensEntityId) o;
        return Objects.equals(this.codMunicipio, entity.codMunicipio) &&
                Objects.equals(this.referencia, entity.referencia) &&
                Objects.equals(this.fonte, entity.fonte) &&
                Objects.equals(this.indicador, entity.indicador) &&
                Objects.equals(this.valor, entity.valor) &&
                Objects.equals(this.unidade, entity.unidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codMunicipio, referencia, fonte, indicador, valor, unidade);
    }
}