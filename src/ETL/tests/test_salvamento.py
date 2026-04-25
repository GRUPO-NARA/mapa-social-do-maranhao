"""
Testes simples para a classe de salvamento.
Verifica inicialização e métodos básicos de conexão com banco de dados.
"""
import pytest
from unittest.mock import Mock, patch, MagicMock
from pipeline.salvamento import FluxoDeSalvamento


class TestFluxoDeSalvamento:
    """Testa a classe responsável pelo salvamento de dados no banco."""
    
    def test_inicializacao_fluxo_salvamento(self):
        """Testa se a classe é inicializada corretamente."""
        with patch('pipeline.salvamento.ConfiguracoesProcessoDeETL'):
            with patch('pipeline.salvamento.FuncoesDeApoioSalvamento'):
                salvamento = FluxoDeSalvamento()
                assert salvamento is not None
    
    def test_criar_conexao_com_banco_de_dados_sucesso(self):
        """Testa criação de conexão com banco de dados."""
        with patch('pipeline.salvamento.ConfiguracoesProcessoDeETL'):
            with patch('pipeline.salvamento.FuncoesDeApoioSalvamento'):
                salvamento = FluxoDeSalvamento()
                
                # Mock da função create_engine
                with patch('pipeline.salvamento.create_engine') as mock_engine:
                    mock_connection = Mock()
                    mock_engine.return_value = mock_connection
                    
                    url_teste = "postgresql://usuario:senha@localhost/banco"
                    resultado = salvamento.criar_conexao_com_banco_de_dados(url_teste)
                    
                    # Verifica se create_engine foi chamado com a URL correta
                    mock_engine.assert_called_once_with(url_teste)
                    assert resultado == mock_connection
    
    def test_criar_conexao_com_banco_erro(self):
        """Testa erro ao criar conexão com banco de dados."""
        with patch('pipeline.salvamento.ConfiguracoesProcessoDeETL'):
            with patch('pipeline.salvamento.FuncoesDeApoioSalvamento'):
                salvamento = FluxoDeSalvamento()
                
                # Mock para simular erro
                with patch('pipeline.salvamento.create_engine') as mock_engine:
                    mock_engine.side_effect = Exception("Erro de conexão")
                    
                    with pytest.raises(Exception):
                        salvamento.criar_conexao_com_banco_de_dados("url_invalida")
    
    def test_salvar_tabela_com_informacoes_vazias(self):
        """Testa salvamento com informações de indicador vazias."""
        with patch('pipeline.salvamento.ConfiguracoesProcessoDeETL'):
            with patch('pipeline.salvamento.FuncoesDeApoioSalvamento'):
                salvamento = FluxoDeSalvamento()
                
                import pandas as pd
                tabela = pd.DataFrame({'coluna': [1, 2, 3]})
                conexao = Mock()
                
                resultado = salvamento.salvar_tabela_no_banco_de_dados(
                    tabela, 
                    {}, 
                    conexao
                )
                
                # Deve retornar False quando informações estão vazias
                assert resultado == False
    
    def test_salvar_tabela_vazia(self):
        """Testa salvamento de tabela vazia."""
        with patch('pipeline.salvamento.ConfiguracoesProcessoDeETL'):
            with patch('pipeline.salvamento.FuncoesDeApoioSalvamento'):
                salvamento = FluxoDeSalvamento()
                
                import pandas as pd
                tabela_vazia = pd.DataFrame()
                informacoes = {
                    'indicador': 'teste',
                    'categoria': 'educacao'
                }
                conexao = Mock()
                
                resultado = salvamento.salvar_tabela_no_banco_de_dados(
                    tabela_vazia,
                    informacoes,
                    conexao
                )
                
                # Deve retornar False quando tabela está vazia
                assert resultado == False
    
    def test_salvar_tabela_none(self):
        """Testa salvamento com tabela None."""
        with patch('pipeline.salvamento.ConfiguracoesProcessoDeETL'):
            with patch('pipeline.salvamento.FuncoesDeApoioSalvamento'):
                salvamento = FluxoDeSalvamento()
                
                informacoes = {
                    'indicador': 'teste',
                    'categoria': 'educacao'
                }
                conexao = Mock()
                
                resultado = salvamento.salvar_tabela_no_banco_de_dados(
                    None,
                    informacoes,
                    conexao
                )
                
                # Deve retornar False quando tabela é None
                assert resultado == False
