package mpma.mapasocial.backend.repository.assistenciaSocial;

import mpma.mapasocial.backend.entity.assistenciaSocial.pessoasComDeficienciaCadastradasCadastroUnicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoasComDeficienciaCadastradasCadastroUnicoRepository extends JpaRepository<pessoasComDeficienciaCadastradasCadastroUnicoEntity, Long> {

    @Query(value = "SELECT pdcu.valor FROM assistencia_social.pessoas_com_deficiencia_cadastradas_cadastro_unico pdcu " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON pdcu.cod_municipio = i.codigo_ibge " +
            "WHERE i.municipio ILIKE :municipio " +
            "ORDER BY pdcu.referencia DESC " + // Ordena do ano mais recente para o mais antigo
            "LIMIT 1",                         // Garante que apenas o primeiro (mais recente) seja retornado
            nativeQuery = true)
    Long buscarPessoasComDeficienciaCadastradasCadastroUnicoPorMunicipio(@Param("municipio") String municipio);

}