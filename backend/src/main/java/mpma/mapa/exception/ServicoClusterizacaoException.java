package mpma.mapa.exception;

public class ServicoClusterizacaoException extends RuntimeException {

    public ServicoClusterizacaoException(String mensagem) {
        super(mensagem);
    }

    public ServicoClusterizacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
