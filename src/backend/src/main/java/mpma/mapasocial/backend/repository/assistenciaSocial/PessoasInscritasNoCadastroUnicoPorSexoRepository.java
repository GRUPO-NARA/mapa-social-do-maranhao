package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.pessoasInscritasNoCadastroUnicoPorSexoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoasInscritasNoCadastroUnicoPorSexoRepository extends JpaRepository<pessoasInscritasNoCadastroUnicoPorSexoEntity, Long> {
    @Query(value = "SELECT ps.valor " +
                   "FROM assistencia_social.pessoas_inscritas_no_cadastro_unico_por_sexo ps " +
                   "JOIN dados_estadual.referencias_codigos_municipais i ON ps.cod_municipio = i.codigo_ibge " +
                   "WHERE i.municipio = :municipio " +
                   "ORDER BY ps.referencia DESC " +   // troque 'referencia' pelo nome correto da coluna de data/ano se for outro
                   "LIMIT 1",
           nativeQuery = true)
    Optional<Long> buscarPessoasInscritasNoCadastroUnicoPorSexo(@Param("municipio") String municipio);
}