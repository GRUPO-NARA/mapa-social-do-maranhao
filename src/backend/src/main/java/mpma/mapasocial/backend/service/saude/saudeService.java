package mpma.mapasocial.backend.service.saude;

import mpma.mapasocial.backend.repository.saude.IdadeMedianaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.repository.query.Param;

/**
 * Serviço da camada Saúde responsável por orquestrar consultas de saúde e atuar como ponte entre API e repositório.
 *
 * Esta classe delega a busca de indicadores de saúde para o repositório apropriado,
 * isolando a complexidade de acesso a dados e preservando a assinatura do serviço.
 */
@Service
public class saudeService {
    @Autowired
    private IdadeMedianaRepository idadeMedianaRepository;

    /**
     * Ponte de negócio entre Controller e Repository para o indicador de idade mediana.
     *
     * Recebe o ano de referência e o nome do município, e deve repassar esses parâmetros
     * para a consulta do repositório de Saúde. O método isola a complexidade de busca
     * na camada de serviço.
     *
     * @param ano ano de referência usado na busca do indicador
     * @param municipio nome do município com grafia correta para busca com ILIKE (ex: 'São Luís')
     * @return Double valor da idade mediana ou null caso nenhum registro seja encontrado
     */
    public Double buscarIdadeMedianaPorMunicipio(String ano, String municipio) {
        return null;
    }
}
