package mpma.mapa.exception;

public class ServicoPredicaoException extends RuntimeException {

    public ServicoPredicaoException(String mensagem) {
        super(mensagem);
    }

    public ServicoPredicaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
