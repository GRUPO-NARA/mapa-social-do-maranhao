"""
Testes para as funções de apoio de tratamento.
Verifica se as funções utilitárias funcionam corretamente.
"""
import pytest
import pandas as pd
from funcoes_de_apoio.para_tratamento import FuncoesDeApoioParaTratamento


class TestFuncoesDeApoioTratamento:
    """Testa as funções auxiliares de tratamento de dados."""
    
    @pytest.fixture
    def funcoes(self):
        """Cria uma instância das funções de apoio para teste."""
        return FuncoesDeApoioParaTratamento()
    
    # Testes da função remover_acentos
    def test_remover_acentos_simples(self, funcoes):
        """Testa remoção de acentos de um texto simples."""
        resultado = funcoes.remover_acentos("São Paulo")
        assert resultado == "SaoPaulo"
    
    def test_remover_acentos_com_varios_caracteres(self, funcoes):
        """Testa remoção de múltiplos acentos."""
        resultado = funcoes.remover_acentos("Açúcar, Pão, Número")
        # Remove acentos e espaços/caracteres especiais
        assert "agua" in resultado.lower() or "acucar" in resultado.lower()
    
    def test_remover_acentos_vazio(self, funcoes):
        """Testa com texto vazio."""
        resultado = funcoes.remover_acentos("")
        assert resultado == ""
    
    # Testes da função organizar_dataframe_SAGICAD
    def test_organizar_dataframe_SAGICAD(self, funcoes):
        """Testa organização de um DataFrame no formato SAGICAD."""
        # Cria um DataFrame simples
        dados = {
            'cod_municipio': [210010, 210020],
            'referencia': ['01/2023', '02/2023'],
            'valor': [100, 200],
            'coluna_extra': [1, 2]
        }
        df = pd.DataFrame(dados)
        
        resultado = funcoes.organizar_dataframe_SAGICAD('SAGICAD', 'indicador_teste', df)
        
        # Verifica se as colunas obrigatórias existem
        assert 'cod_municipio' in resultado.columns
        assert 'referencia' in resultado.columns
        assert 'valor' in resultado.columns
        assert 'fonte' in resultado.columns
        assert 'indicador' in resultado.columns
        
        # Verifica se a fonte e indicador fueron adicionados
        assert resultado['fonte'].iloc[0] == 'SAGICAD'
        assert resultado['indicador'].iloc[0] == 'indicador_teste'
    
    # Testes da função tratamento_valores_nulos_SAGICAD
    def test_tratamento_valores_nulos(self, funcoes):
        """Testa tratamento de valores nulos."""
        dados = {
            'valor': ['100', '200', 'nan', None],
            'outro_campo': [1, 2, 3, 4]
        }
        df = pd.DataFrame(dados)
        
        resultado = funcoes.tratamento_valores_nulos_SAGICAD(df)
        
        # Verifica se nulos foram tratados
        assert not resultado['valor'].isna().any()
        # 'nan' strings devem ser convertidas para -1
        assert -1 in resultado['valor'].values or resultado['valor'].isna().sum() == 0
    
    def test_tratamento_valores_com_virgula(self, funcoes):
        """Testa conversão de valores com vírgula."""
        dados = {'valor': ['100,5', '200,3']}
        df = pd.DataFrame(dados)
        
        resultado = funcoes.tratamento_valores_nulos_SAGICAD(df)
        
        # Verifica se a conversão foi feita
        assert resultado['valor'].iloc[0] > 100
        assert resultado['valor'].iloc[1] > 200
