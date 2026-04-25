import pytest 

from pipeline.tratamento import FluxoDeTratamento
from unittest.mock import patch, MagicMock

@pytest.fixture
def fluxo():
    """Cria uma instância de FluxoDeTratamento com mock da conexão com banco."""
    with patch('pipeline.tratamento.ConfiguracoesProcessoDeETL'):
        with patch('pipeline.tratamento.FluxoDeColeta'):
            with patch('pipeline.tratamento.FluxoDeSalvamento') as mock_salvamento:
                with patch('pipeline.tratamento.load_dotenv'):
                    with patch.dict('os.environ', {'URL_BANCO_LOCAL': 'sqlite:///:memory:'}):
                        # Mock da conexão com banco
                        mock_salvamento_instance = MagicMock()
                        mock_salvamento_instance.criar_conexao_com_banco_de_dados.return_value = MagicMock()
                        mock_salvamento.return_value = mock_salvamento_instance
                        
                        return FluxoDeTratamento()

def test_inicializacao_fluxo_de_tratamento(fluxo):
    """Teste para verificar a inicialização correta do FluxoDeTratamento e suas variáveis de instância."""
    assert fluxo is not None # Verifica se a instância foi criada com sucesso
    assert hasattr(fluxo, 'coleta') # Verifica se a variável de coleta foi inicializada
    assert hasattr(fluxo, 'salvamento') # Verifica se a variável de salvamento foi inicializada
    assert hasattr(fluxo, 'salvamento_logger') # Verifica se a variável de logger para salvamento foi inicializada
    assert hasattr(fluxo, 'conexao_com_o_banco') # Verifica se a variável de conexão com o banco de dados foi inicializada

