package mpma.mapa.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import mpma.mapa.service.Resposta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private Resposta resposta;

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<HashMap<String, Object>> handleRateLimiter(RequestNotPermitted e) {
        HashMap<String, Object> respostaRateLimit = resposta.CorpoDaRespostaError(
                "Acesso Bloqueado",
                "Limite de requisições atingido. Por favor, tente novamente em 30 segundos.",
                String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value())
        );

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", "30")
                .body(respostaRateLimit);
    }

    @ExceptionHandler(ServicoPredicaoException.class)
    public ResponseEntity<HashMap<String, Object>> handleServicoPredicao(ServicoPredicaoException e) {
        logger.warn("Falha no serviço de predição", e);
        HashMap<String, Object> respostaError = resposta.CorpoDaRespostaError(
                "Serviço de predição indisponível",
                "Serviço temporariamente indisponível",
                String.valueOf(HttpStatus.BAD_GATEWAY.value())
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(respostaError);
    }

    @ExceptionHandler(ServicoClusterizacaoException.class)
    public ResponseEntity<HashMap<String, Object>> handleServicoClusterizacao(
            ServicoClusterizacaoException e
    ) {
        logger.warn("Falha no serviço de clusterização", e);
        HashMap<String, Object> respostaError = resposta.CorpoDaRespostaError(
                "Serviço de clusterização indisponível",
                "Serviço temporariamente indisponível",
                String.valueOf(HttpStatus.BAD_GATEWAY.value())
        );
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(respostaError);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HashMap<String, Object>> handleInvalidArgument(IllegalArgumentException e) {
        HashMap<String, Object> respostaError = resposta.CorpoDaRespostaError(
                "Dados inválidos",
                e.getMessage(),
                String.valueOf(HttpStatus.BAD_REQUEST.value())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respostaError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HashMap<String, Object>> handleGenericException(Exception e) {
        logger.error("Erro interno inesperado", e);
        HashMap<String, Object> respostaError = resposta.CorpoDaRespostaError(
                "Erro Interno no Servidor",
                "Erro interno ao processar a solicitação",
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respostaError);
    }
}
