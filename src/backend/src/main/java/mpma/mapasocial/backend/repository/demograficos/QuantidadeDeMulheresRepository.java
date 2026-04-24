package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.quantidadeDeMulheresEntity;

@Repository
public interface QuantidadeDeMulheresRepository extends JpaRepository<quantidadeDeMulheresEntity, Long> {
    @Query(value = "SELECT qm.valor FROM demograficos.quantidade_de_mulheres qm " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = qm.cod_municipio " +
            "WHERE i.municipio ILIKE :municipio", nativeQuery = true)
    Long buscarQuantidadeDeMulheresPorMunicipio(@Param("municipio") String municipio);
}
