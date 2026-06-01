package mpma.mapasocial.backend.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Serviço de construção de resposta padronizada para requisições da API.
 *
 * A classe monta um corpo de resposta contendo status, nome do indicador e os dados
 * solicitados de forma genérica, permitindo que a API retorne sempre uma estrutura
 * consistente para os clientes.
 */
@Service
public class RespostaRequisicao {


    /**
     * Representa o contrato de dados que podem ser incluídos na resposta.
     *
     * A interface selada garante que apenas tipos permitidos possam ser usados como
     * conteúdo da resposta, aumentando a previsibilidade da estrutura retornada.
     */
    public sealed interface dadosRequisitados permits ObjetoDeResposta{}

    /**
     * Objeto simples que envolve os dados reais a serem retornados na resposta.
     *
     * Esta classe permite empacotar qualquer payload como um tipo compatível com
     * o contrato `dadosRequisitados`.
     */
    public record ObjetoDeResposta(Object dados) implements dadosRequisitados{}

    /**
     * Cria o corpo de resposta padrão usado pela API.
     *
     * O método constrói um `Map` com chaves fixas: `status`, `indicador` e `resposta`.
     * A entrada `resposta` armazena o payload encapsulado no tipo selado.
     *
     * @param status resultado da requisição, por exemplo "sucesso" ou "erro"
     * @param indicadorNome nome do indicador retornado na resposta
     * @param dados payload de dados que será incluído no corpo
     * @return mapa estruturado com as informações da resposta
     */
    public Map<String, Object> criarCorpo(String status, String indicadorNome, dadosRequisitados dados) {
        Map<String, Object> corpo = new HashMap<>();

        corpo.put("status", status);
        corpo.put("indicador", indicadorNome);
        corpo.put("resposta", dados);

        return corpo;
    }
}
