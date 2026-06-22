package mpma.mapa.repository.Saude;

import mpma.mapa.entity.Saude.CorberturaDaEstrategiaDeSaudeFamiliarEntity; // Sincronizado com o "r" da sua Entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CoberturaDaEstrategiaDeSaudeFamiliarRepository extends JpaRepository<CorberturaDaEstrategiaDeSaudeFamiliarEntity, Long> {

    @Query(
            value = """
            SELECT json_build_object(
                                'Cobertura da Estratégia de Saúde Familiar', t.valor,
                                'Referência dos Dados', t.referencia,
                                'Fonte dos Dados', t.fonte
                         )
                         FROM saude.corbertura_da_estrategia_de_saude_familiar as t -- CORRIGIDO: Adicionado o "r" para espelhar a tabela da Entity
                         JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                         WHERE rcm.municipio = :municipio
                         GROUP BY t.valor, t.referencia, t.fonte
                         ORDER BY t.referencia DESC
                         LIMIT 1;
            """
            , nativeQuery = true
    )
    String buscarCoberturaDaEstrategiaDeSaudeFamiliarDoMunicipio(@Param("municipio") String municipio);
}