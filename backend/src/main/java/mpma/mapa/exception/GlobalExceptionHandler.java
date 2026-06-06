package mpma.mapa.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import mpma.mapa.service.Resposta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HashMap<String, Object>> handleGenericException(Exception e) {
        HashMap<String, Object> respostaError = resposta.CorpoDaRespostaError(
                "Erro Interno no Servidor",
                e.getMessage(),
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respostaError);
    }
}
