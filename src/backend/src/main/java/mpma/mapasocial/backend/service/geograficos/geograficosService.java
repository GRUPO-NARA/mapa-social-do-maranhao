package mpma.mapasocial.backend.service.geograficos;
import mpma.mapasocial.backend.repository.geograficos.AreaTerritorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mpma.mapasocial.backend.repository.geograficos.DensidadeDemograficaRepository;

/**
 * Serviço da camada Geográficos responsável por orquestrar consultas de dados geográficos.
 *
 * Esta classe atua como ponte entre o Controller e os Repositories de geográficos,
 * recebendo o município e delegando a consulta à camada de dados.
 */
@Service
public class geograficosService {
    @Autowired
    private AreaTerritorialRepository areaTerritorialRepository;

    @Autowired
    private DensidadeDemograficaRepository densidadeDemograficaRepository;

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de área territorial municipal.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método devolve o valor mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Double valor da área territorial do município
     */
    public Double areaTerritorialDoMunicipio(String municipio){
        return areaTerritorialRepository.buscarAreaTerritoralPorMunicipio(municipio);
    }

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de densidade demográfica municipal.
     *
     * Recebe o nome do município, repassa para a query nativa do repositório correspondente e isola a complexidade de busca.
     * O método devolve o valor mais recente disponível no banco de dados para esse indicador.
     *
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Double valor da densidade demográfica do município
     */
    public Double densidadeDemograficaDoMunicipio(String municipio){
        return densidadeDemograficaRepository.buscarDensidadeDemograficaPorMunicipio(municipio);
    }

}
