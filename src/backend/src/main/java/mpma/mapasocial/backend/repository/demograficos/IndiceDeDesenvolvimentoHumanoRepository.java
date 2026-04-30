package mpma.mapasocial.backend.repository.demograficos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import mpma.mapasocial.backend.entity.demograficos.indiceDeDesenvolvimentoHumanoEntity;

@Repository
public interface IndiceDeDesenvolvimentoHumanoRepository extends JpaRepository<indiceDeDesenvolvimentoHumanoEntity, Long> {
    @Query(value = "SELECT idh.valor FROM demograficos.indice_de_desenvolvimento_humano idh\n" +
            "JOIN dados_estadual.referencias_codigos_municipais i ON idh.cod_municipio = i.codigo_ibge\n" +
            "WHERE idh.referencia = :ano AND i.municipio ILIKE :municipio;", nativeQuery = true)
    Double buscarIndiceDeDesenvolvimentoHumanoPorMunicipio(@Param("ano") Integer ano, @Param("municipio") String municipio);
}
