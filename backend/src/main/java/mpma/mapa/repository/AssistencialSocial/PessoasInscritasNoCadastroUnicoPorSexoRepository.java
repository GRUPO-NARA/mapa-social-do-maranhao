package mpma.mapa.repository.AssistencialSocial;

import mpma.mapa.entity.AssistencialSocial.PessoasInscritasNoCadastroUnicoPorSexoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoasInscritasNoCadastroUnicoPorSexoRepository extends JpaRepository<PessoasInscritasNoCadastroUnicoPorSexoEntity, Long> {
    @Query(value = "SELECT ps.valor FROM assistencia_social.pessoas_inscritas_cadastro_unico_sexo ps " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON ps.cod_municipio = i.codigo_ibge " +
            "WHERE ps.referencia = :ano AND i.municipio ILIKE :municipio",
            nativeQuery = true)
    Long buscarPessoasInscritasNoCadastroUnicoPorSexo(@Param("ano") Integer ano, @Param("municipio") String municipio);
}
