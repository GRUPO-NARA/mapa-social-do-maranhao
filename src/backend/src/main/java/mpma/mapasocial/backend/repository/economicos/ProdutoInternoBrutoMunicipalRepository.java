package mpma.mapasocial.backend.repository.economicos;

import org.springframework.data.jpa.repository.JpaRepository;
import mpma.mapasocial.backend.entity.economicos.produtoInternoBrutoMunicipalEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoInternoBrutoMunicipalRepository extends JpaRepository<produtoInternoBrutoMunicipalEntity, Long> {
    @Query(value = "SELECT pib.valor FROM economicos.produto_interno_bruto pib " +
            "JOIN dados_estadual.referencias_codigos_municipais i ON i.codigo_ibge = pib.cod_municipio " +
            "WHERE i.municipio ILIKE :municipio", nativeQuery = true)
    Double buscarPIBPorMunicipio(@Param("municipio") String municipio);
}
