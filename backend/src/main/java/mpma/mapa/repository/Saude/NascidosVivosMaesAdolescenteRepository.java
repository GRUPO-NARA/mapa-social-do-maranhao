package mpma.mapa.repository.Saude;

import mpma.mapa.entity.Saude.NascidosVivosMaesAdolescenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface NascidosVivosMaesAdolescenteRepository extends JpaRepository<NascidosVivosMaesAdolescenteEntity, Long> {
    @Query(
            value = """
                    SELECT json_build_object(
                                        'Nascidos Vivos de Mães Adolescentes', t.valor,
                                        'Referência dos Dados', t.referencia,
                                        'Fonte dos Dados', t.fonte
                                 )
                                 FROM saude.nascidos_vivos_maes_adolescentes as t
                                 JOIN dados_estadual.referencias_codigos_municipais rcm on t.cod_municipio = rcm.codigo_ibge
                                 WHERE rcm.municipio = :municipio
                                 GROUP BY t.valor, t.referencia, t.fonte
                                 ORDER BY t.referencia DESC
                                 LIMIT 1;
                    """
            , nativeQuery = true
    )
    String buscarNascidosVivosMaesAdolescentesDoMunicipio(String municipio);
}
