package mpma.mapa.repository.Saude;

import mpma.mapa.entity.Saude.InternacoesDiabetesMellitusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface InternacoesDiabetesMellitusRepository extends JpaRepository<InternacoesDiabetesMellitusEntity, Long> {
    @Query(
            value = """
                    SELECT json_build_object(
                                        'Internações por Diabetes Mellitus', t.valor,
                                        'Referência dos Dados', t.referencia,
                                        'Fonte dos Dados', t.fonte
                                 )
                                 FROM saude.internacoes_diabetes_mellitus as t
                                 JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                                 WHERE rcm.municipio = :municipio
                                 GROUP BY t.valor, t.referencia, t.fonte
                                 ORDER BY t.referencia DESC
                                 LIMIT 1;
                    """
            , nativeQuery = true
    )
    String buscarInternacoesDiabetesMellitusDoMunicipio(String municipio);
}
