package mpma.mapa.repository.Demograficos;

import mpma.mapa.entity.Demograficos.QuantidadeDeResidentesUrbanoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface QuantidadeDeResidentesUrbanoRepository extends JpaRepository<QuantidadeDeResidentesUrbanoEntity, Long> {
    @Query(
            value = """
                    SELECT json_build_object(
                                        'Quantidade de Residentes em Área Urbana', t.valor,
                                        'Referência dos Dados', t.referencia,
                                        'Fonte dos Dados', t.fonte
                                 )
                                 FROM demograficos.quantidade_de_residentes_urbanos as t
                                 JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                                 WHERE rcm.municipio = :municipio
                                 GROUP BY t.valor, t.referencia, t.fonte
                                 ORDER BY t.referencia DESC
                                 LIMIT 1;
                    """
            , nativeQuery = true
    )
    String buscarQuantidadeDeResidentesUrbanoDoMunicipio(String municipio);
}
