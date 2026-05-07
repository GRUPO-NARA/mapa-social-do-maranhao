package mpma.mapasocial.backend.repository.saude;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdadeMedianaRepository extends JpaRepository<mpma.mapasocial.backend.entity.saude.idadeMedianaEntity, Long> {
    @Query(value = "SELECT idade_mediana.valor FROM saude.idade_mediana " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON idade_mediana.cod_municipio = i.codigo_ibge " +
            "WHERE idade_mediana.referencia = :ano AND i.municipio ILIKE :municipio;", nativeQuery = true)
    Double buscarIdadeMedianaPorMunicipio(@Param("ano") String ano, @Param("municipio") String municipio);
}
