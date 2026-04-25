from pathlib import Path
import logging


class ConfiguracoesProcessoDeETL:
    def __init__(self):
        self.diretorio_atual = Path(__file__).resolve().parent
        self.arquivo_que_contem_as_urls = self.diretorio_atual / 'informacoes' / 'dados_coletados.json'

        if not self.arquivo_que_contem_as_urls.exists():
            logging.error(f"Arquivo de configuração de URLs não encontrado: {self.arquivo_que_contem_as_urls}")
        self.diretorio_dos_loggings = self.diretorio_atual / 'logs'

        if not self.diretorio_dos_loggings.exists():
            self.diretorio_dos_loggings.mkdir(parents=True, exist_ok=True)

    def configuracoes_logging_para_coleta(self):
        logger_coleta = logging.getLogger('coleta')
        # Remove handlers existentes para evitar duplicação
        logger_coleta.handlers.clear()
        handler_coleta = logging.FileHandler(self.diretorio_dos_loggings / 'coleta.log')
        handler_coleta.setFormatter(logging.Formatter(fmt='%(asctime)s - %(filename)s - %(funcName)s - %(levelname)s - %(message)s'))
        logger_coleta.addHandler(handler_coleta)
        logger_coleta.setLevel(logging.INFO)
        return logger_coleta
    
    def configuracoes_logging_para_tratamento(self):
        logger_tratamento = logging.getLogger('tratamento')
        # Remove handlers existentes para evitar duplicação
        logger_tratamento.handlers.clear()
        handler_tratamento = logging.FileHandler(self.diretorio_dos_loggings / 'tratamento.log')
        handler_tratamento.setFormatter(logging.Formatter(fmt='%(asctime)s - %(filename)s - %(funcName)s - %(levelname)s - %(message)s'))
        logger_tratamento.addHandler(handler_tratamento)
        logger_tratamento.setLevel(logging.INFO)
        return logger_tratamento
    
    def configuracoes_logging_para_salvamento(self):
        logger_salvamento = logging.getLogger('salvamento')
        # Remove handlers existentes para evitar duplicação
        logger_salvamento.handlers.clear()
        handler_salvamento = logging.FileHandler(self.diretorio_dos_loggings / 'salvamento.log')
        handler_salvamento.setFormatter(logging.Formatter(fmt='%(asctime)s - %(filename)s - %(funcName)s - %(levelname)s - %(message)s'))
        logger_salvamento.addHandler(handler_salvamento)
        logger_salvamento.setLevel(logging.INFO)
        return logger_salvamento
    
    