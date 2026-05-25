package mpma.mapasocial.backend.entity.assistenciaSocial;

import jakarta.persistence.*;
import mpma.mapasocial.backend.entity.estadual.referenciasCodigosMunicipaisEntity;

@Entity
@Table(name = "pessoas_inscritas_no_cadastro_unico_por_sexo", schema = "assistencia_social")
public class pessoasInscritasNoCadastroUnicoPorSexoEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_municipio")
    private referenciasCodigosMunicipaisEntity codMunicipio;

    @Column(name = "referencia", length = Integer.MAX_VALUE)
    private String referencia;

    @Column(name = "fonte", length = Integer.MAX_VALUE)
    private String fonte;

    @Column(name = "valor")
    private Long valor;

    @Column(name = "indicador", length = Integer.MAX_VALUE)
    private String indicador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public referenciasCodigosMunicipaisEntity getCodMunicipio() {
        return codMunicipio;
    }

    public void setCodMunicipio(referenciasCodigosMunicipaisEntity codMunicipio) {
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

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

}