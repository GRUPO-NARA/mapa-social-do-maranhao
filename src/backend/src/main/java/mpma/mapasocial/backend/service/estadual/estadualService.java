package mpma.mapasocial.backend.service.estadual;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mpma.mapasocial.backend.repository.estadual.referenciasCodigosMunicipaisRepository;

import java.util.List;


@Service
/**
 * Serviço da camada Estadual responsável por operações relacionadas aos municípios do estado.
 *
 * Esta classe atua como ponte entre o Controller e o Repository de códigos municipais,
 * encapsulando a lógica de obtenção da lista de municípios e a política de retorno.
 */
public class estadualService {
    @Autowired
    private referenciasCodigosMunicipaisRepository referenciasCodigosMunicipaisRepository;

    /**
     * Recupera a lista de municípios do estado a partir do repositório de referências de códigos municipais.
     *
     * O método delega a consulta ao repositório e trata o resultado, retornando null quando não houver municípios cadastrados.
     *
     * @return List<String> com os nomes dos municípios do estado ou null se nenhum município for encontrado
     */
    public List<String> listaDosMunicipiosDoEstado(){
        List<String> listaDeMunicipios = referenciasCodigosMunicipaisRepository.getMunicipios();

        if (listaDeMunicipios.isEmpty()){
            return null;
        }

        return listaDeMunicipios;
    }
}
