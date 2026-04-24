package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapasocial.backend.entity.demograficos.populacaoResidenteEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PopulacaoResidenteRepository extends JpaRepository<populacaoResidenteEntity, Long> {

    @Query(value = "SELECT pr.valor FROM demograficos.população_residente pr " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON pr.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio ",
            nativeQuery = true)
    Long buscarPopulacaoResidente(@Param("municipio") String municipio);
}
