import pytest 
from pipeline.coleta import FluxoDeColeta
from pathlib import Path
from unittest.mock import Mock, patch
@pytest.fixture
def fluxo():
    return FluxoDeColeta()

def test_inicializacao_fluxo_de_coleta(fluxo):
    """Teste para verificar a inicialização correta do FluxoDeColeta e suas variáveis de instância."""
    assert fluxo is not None # Verifica se a instância foi criada com sucesso
    assert hasattr(fluxo, 'config') # Verifica se a variável de configuração foi inicializada
    assert fluxo.informacoes_do_arquivo == {} # Verifica se a variável de informações do arquivo foi inicializada como um dicionário vazio
    assert fluxo.lista_de_urls_com_informacoes == [] # Verifica se a variável de lista de URLs com informações foi inicializada como uma lista vazia
    assert fluxo.tarefas_realizadas == False # Verifica se a variável de tarefas realizadas foi inicializada como False

def test_error_leitura_do_arquivo_das_fontes_dos_indicadores(fluxo):
    
    fluxo.arquivo_que_contem_as_urls = Path("pasta_inexistente/arquivo_inexistente.json") # Define um caminho para um arquivo que não existe
    with pytest.raises(FileNotFoundError): # Verifica se a função lança um FileNotFoundError ao tentar ler um arquivo que não existe
        fluxo.leitura_do_arquivo_das_fontes_dos_indicadores()

def test_urls_para_coleta(fluxo):
    """Testa a extração de URLs com dados isolados (mock) do arquivo."""
    
    # Dados de teste isolados - não carrega o arquivo JSON completo
    dados_teste = [
        {
            "categoria": "economicos",
            "indicadores": [
                {
                    "indicador": "produto_interno_bruto",
                    "fonte": "SIDRA",
                    "formato_do_arquivo": "csv",
                    "link": "https://sidra.ibge.gov.br/geratabela/DownloadSelecaoComplexa/-1618334337"
                }
            ]
        }
    ]
    
    # Mock da função para retornar apenas dados de teste
    with patch.object(fluxo, 'leitura_do_arquivo_das_fontes_dos_indicadores', return_value=dados_teste):
        urls = fluxo.urls_para_coleta()
        assert urls == ["https://sidra.ibge.gov.br/geratabela/DownloadSelecaoComplexa/-1618334337"]
        assert len(fluxo.lista_de_urls_com_informacoes) == 1
        assert fluxo.lista_de_urls_com_informacoes == [
            (
                "https://sidra.ibge.gov.br/geratabela/DownloadSelecaoComplexa/-1618334337",
                
                {
                    "categoria": "economicos",
                    "indicador": "produto_interno_bruto",
                    "fonte": "SIDRA",
                    "formato_do_arquivo": "csv"
                }
            )
        ]


