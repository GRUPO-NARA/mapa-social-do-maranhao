package mpma.mapasocial.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal do backend Spring Boot.
 *
 * Esta classe contém o método `main` que inicializa o contexto da aplicação,
 * dispara a configuração automática do Spring Boot e registra os componentes
 * do backend para que o serviço fique disponível.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
