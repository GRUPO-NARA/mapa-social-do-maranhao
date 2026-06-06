
# Configurações para Loggers
import logging
from logging import Logger

def LoggerParaColeta() -> Logger:
    logger = logging.getLogger("logger_coleta")
    logger.setLevel(logging.INFO)
    logging.basicConfig(
        level = logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    )
    return logger

def LoggerParaTratamento() -> Logger:
    logger = logging.getLogger("logger_tratamento")
    logger.setLevel(logging.INFO)
    logging.basicConfig(
        level = logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    )
    return logger

def LoggerParaSalvamento() -> Logger:
    logger = logging.getLogger("logger_salvamento")
    logger.setLevel(logging.INFO)
    logging.basicConfig(
        level = logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S'
    )
    return logger

# Mapeamento de diretórios e arquivos
from pathlib import Path

class MapeamentoDiretoriosEArquivos:
    def __init__(self):
        self.diretorio_do_projeto = Path(__file__).parent.parent.parent
        self.arquivo_dos_dados_para_coleta = self.diretorio_do_projeto / 'pipeline' / 'informacoes' / 'dados.json'
        self.arquivo_de_referencia_dos_codigos_municipais = self.diretorio_do_projeto / 'pipeline' / 'informacoes' / 'codigos_municipais.csv'

