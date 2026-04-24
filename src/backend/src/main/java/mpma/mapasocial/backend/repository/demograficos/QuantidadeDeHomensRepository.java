package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.quantidadeDeHomensEntity;
@Repository
public interface QuantidadeDeHomensRepository extends JpaRepository<quantidadeDeHomensEntity, Long> {
    @Query(value = "SELECT qh.valor FROM demograficos.quantidade_de_homens qh " +
    "JOIN dados_estadual.referencias_codigos_municipais i ON qh.cod_municipio = i.codigo_ibge " +
    "WHERE i.municipio ILIKE :municipio ", nativeQuery = true)
    Long buscarQuantidadeDeHomensPorMunicipio(@Param("municipio") String municipio);
}
