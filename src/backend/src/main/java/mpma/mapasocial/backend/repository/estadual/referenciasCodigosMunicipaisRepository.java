package mpma.mapasocial.backend.repository.estadual;

import mpma.mapasocial.backend.entity.estadual.referenciasCodigosMunicipaisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para a entidade de referências de códigos municipais estaduais.
 *
 * Esta interface expõe operações de leitura sobre a tabela de códigos municipais,
 * permitindo recuperar a lista de municípios cadastrados para uso em filtros e buscas.
 */
@Repository
public interface referenciasCodigosMunicipaisRepository extends JpaRepository<referenciasCodigosMunicipaisEntity, String> {

    /**
     * Recupera todos os nomes de municípios cadastrados na tabela de referências.
     *
     * @return List<String> com os nomes dos municípios disponíveis
     */
    @Query("SELECT r.municipio FROM referenciasCodigosMunicipaisEntity r")
    List<String> getMunicipios();
}
