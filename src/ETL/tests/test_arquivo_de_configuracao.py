"""
Testes para o arquivo de configuração do ETL.
Verifica se as configurações de logging são criadas corretamente.
"""
import pytest
import logging
from pathlib import Path
from arquivo_de_configuracao import ConfiguracoesProcessoDeETL


class TestConfiguracoes:
    """Testa a classe de configurações do ETL."""
    
    def test_inicializacao_das_configuracoes(self):
        """Testa se a classe é inicializada sem erros."""
        config = ConfiguracoesProcessoDeETL()
        assert config is not None
        assert isinstance(config.diretorio_atual, Path)
    
    def test_criacao_de_diretorio_de_logs(self):
        """Testa se o diretório de logs é criado."""
        config = ConfiguracoesProcessoDeETL()
        assert config.diretorio_dos_loggings.exists()
        assert config.diretorio_dos_loggings.is_dir()
    
    def test_configuracao_logging_para_coleta(self):
        """Testa se o logger de coleta é criado corretamente."""
        config = ConfiguracoesProcessoDeETL()
        logger = config.configuracoes_logging_para_coleta()
        
        # Verifica se é um logger do Python
        assert isinstance(logger, logging.Logger)
        # Verifica se tem o nome correto
        assert logger.name == 'coleta'
        # Verifica se tem handlers
        assert len(logger.handlers) > 0
    
    def test_configuracao_logging_para_tratamento(self):
        """Testa se o logger de tratamento é criado corretamente."""
        config = ConfiguracoesProcessoDeETL()
        logger = config.configuracoes_logging_para_tratamento()
        
        assert isinstance(logger, logging.Logger)
        assert logger.name == 'tratamento'
        assert len(logger.handlers) > 0
    
    def test_configuracao_logging_para_salvamento(self):
        """Testa se o logger de salvamento é criado corretamente."""
        config = ConfiguracoesProcessoDeETL()
        logger = config.configuracoes_logging_para_salvamento()
        
        assert isinstance(logger, logging.Logger)
        assert logger.name == 'salvamento'
        assert len(logger.handlers) > 0
