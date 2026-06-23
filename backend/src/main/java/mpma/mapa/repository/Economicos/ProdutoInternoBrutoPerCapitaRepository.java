package mpma.mapa.repository.Economicos;

import mpma.mapa.entity.Economicos.ProdutoInternoBrutoPerCapitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoInternoBrutoPerCapitaRepository extends JpaRepository<ProdutoInternoBrutoPerCapitaEntity, Long> {

    @Query(
            value = """
            SELECT json_build_object(
                               'Média do PIB Per Capita', ROUND(AVG(t.valor)::numeric, 2),
                               'Referência dos Dados', 'Média Histórica',
                               'Fonte dos Dados', t.fonte
                        )
                        FROM economicos.pib_per_capita as t
                        JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                        WHERE rcm.municipio = :municipio
                        GROUP BY t.fonte;
            """
            , nativeQuery = true
    )
    String buscarMediaDoPibPerCapitaDoMunicipio(@Param("municipio") String municipio);
}