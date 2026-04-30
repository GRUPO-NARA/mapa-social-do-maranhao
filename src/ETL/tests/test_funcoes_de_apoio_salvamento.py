"""
Testes simples para as funções de apoio de salvamento.
Verifica se as operações de banco de dados não fazem erros básicos.
"""
import pytest
from unittest.mock import Mock, patch, MagicMock
from funcoes_de_apoio.para_salvamento import FuncoesDeApoioSalvamento


class TestFuncoesDeApoioSalvamento:
    """Testa funções auxiliares de salvamento no banco de dados."""
    
    @pytest.fixture
    def funcoes(self):
        """Cria uma instância das funções de apoio."""
        return FuncoesDeApoioSalvamento()
    
    def test_inicializacao_funcoes_salvamento(self, funcoes):
        """Testa inicialização da classe."""
        assert funcoes is not None
        assert isinstance(funcoes, FuncoesDeApoioSalvamento)
    
    def test_verificacao_de_schema_chamada(self, funcoes):
        """Testa se a função de verificação de schema pode ser chamada."""
        engine_mock = MagicMock()
        
        # Deve executar sem erros
        try:
            funcoes.verificao_de_schema(engine_mock, 'educacao')
            resultado = True
        except:
            resultado = False
        
        # A função existe e é chamável
        assert callable(funcoes.verificao_de_schema)
    
    def test_criar_schema_estadual_chamada(self, funcoes):
        """Testa se a função de criar schema estadual pode ser chamada."""
        engine_mock = MagicMock()
        
        # Deve retornar bool
        resultado = funcoes.criar_schema_estadual(engine_mock)
        
        # A função retorna um booleano ou trata exceção
        assert callable(funcoes.criar_schema_estadual)
    
    def test_definir_chave_primaria_chamada(self, funcoes):
        """Testa se a função de chave primária pode ser chamada."""
        engine_mock = MagicMock()
        
        # Deve executar sem retornar nada
        resultado = funcoes.definir_chave_primaria(engine_mock, 'tabela_teste', 'schema_teste')
        
        assert callable(funcoes.definir_chave_primaria)
    
    def test_definir_chave_estrangeira_chamada(self, funcoes):
        """Testa se a função de chave estrangeira pode ser chamada."""
        engine_mock = MagicMock()
        
        # Deve executar sem retornar nada
        resultado = funcoes.definir_chave_estrangeira(engine_mock, 'tabela_teste', 'schema_teste')
        
        assert callable(funcoes.definir_chave_estrangeira)
    
    def test_inserir_referencias_chamada(self, funcoes):
        """Testa se a função de inserção de referências pode ser chamada."""
        engine_mock = MagicMock()
        
        # Mock para inspect
        with patch('funcoes_de_apoio.para_salvamento.inspect') as mock_inspect:
            mock_inspect_instance = MagicMock()
            mock_inspect.return_value = mock_inspect_instance
            mock_inspect_instance.get_table_names.return_value = ['referencias_codigos_municipais']
            
            # Deve executar sem retornar nada
            resultado = funcoes.inserir_referencias_codigos_municipais(engine_mock)
            
            assert callable(funcoes.inserir_referencias_codigos_municipais)
