package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.quantidadeDeResidentesRuraisEntity;

@Repository
public interface QuantidadeDeResidentesRuraisRepository extends JpaRepository<quantidadeDeResidentesRuraisEntity, Long> {
    @Query(value = "SELECT qrr.valor FROM demograficos.quantidade_de_residentes_rurais qrr " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = qrr.cod_municipio " +
            "WHERE i.municipio = :municipio;", nativeQuery = true)
    Long buscarQuantidadeDeResidentesRuraisPorMunicipio(@Param("municipio") String municipio);
}
