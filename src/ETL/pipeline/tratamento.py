import asyncio
from io import BytesIO
from tratamentos_especificos.SAGICAD import tratamento_dados_SAGICAD
from pipeline.coleta import FluxoDeColeta
from pipeline.salvamento import FluxoDeSalvamento
from arquivo_de_configuracao import ConfiguracoesProcessoDeETL
from tratamentos_especificos.SIDRA import tratamento_dados_SIDRA
from tratamentos_especificos.IPEADATA import tratamento_dados_IPEADATA
from tratamentos_especificos.AprendizadoQEDU import tratamento_dados_aprendizado_QEDU
import os
from dotenv import load_dotenv

load_dotenv()

class FluxoDeTratamento:
    """Orquestrador de tratamento específico para cada fonte de dados"""
    def __init__(self):
        self.coleta = FluxoDeColeta()
        self.salvamento = FluxoDeSalvamento()
        self.salvamento_logger = ConfiguracoesProcessoDeETL().configuracoes_logging_para_salvamento()
        URL_DE_CONEXAO = os.environ.get("DB_URL")

        self.conexao_com_o_banco = self.salvamento.criar_conexao_com_banco_de_dados(URL_DE_CONEXAO)

    async def _executar_tratamento_async(self):
        """Coleta dados em bytes, identifica a fonte e aplica tratamento."""
        # Coleta retorna uma lista de bytes (um por URL coletada)
        lista_de_bytes = await self.coleta.conteudo_da_requisicao()
        
        if not lista_de_bytes:
            raise RuntimeError("Nenhum dado foi coletado.")
        
        # Processa CADA URL coletada com suas respectivas informações
        for indice, bytes_do_arquivo in enumerate(lista_de_bytes):
            if not bytes_do_arquivo:  # Pula se não houver dados
                continue
                
            # Pega as informações correspondentes ao índice da coleta
            informacoes_do_arquivo = self.coleta.informacoes_do_indicador(indice)
            
            if not informacoes_do_arquivo:
                continue
            
            fonte_dos_dados = informacoes_do_arquivo.get("fonte")
            indicador = informacoes_do_arquivo.get("indicador")
            
            arquivo = BytesIO(bytes_do_arquivo)

            if fonte_dos_dados == "SIDRA":
     
                tabela = tratamento_dados_SIDRA(arquivo, informacoes_do_arquivo)
   
                resultado = self.salvamento.salvar_tabela_no_banco_de_dados(tabela, informacoes_do_arquivo, self.conexao_com_o_banco)
                
                if resultado:
                    self.salvamento_logger.info(f"Indicador '{indicador}' da fonte SIDRA tratado e salvo com sucesso.")
                else:
                    self.salvamento_logger.error(f"Falha ao salvar o indicador '{indicador}' da fonte SIDRA.")

            elif fonte_dos_dados == "SAGICAD":

                tabela = tratamento_dados_SAGICAD(arquivo, informacoes_do_arquivo)

                resultado = self.salvamento.salvar_tabela_no_banco_de_dados(tabela, informacoes_do_arquivo, self.conexao_com_o_banco)

                if resultado:
                    self.salvamento_logger.info(f"Indicador '{indicador}' da fonte SAGICAD tratado e salvo com sucesso.")
                else:
                    self.salvamento_logger.error(f"Falha ao salvar o indicador '{indicador}' da fonte SAGICAD.")
            
            elif fonte_dos_dados == "IPEADATA":
                
                tabela = tratamento_dados_IPEADATA(arquivo, informacoes_do_arquivo)
                resultado = self.salvamento.salvar_tabela_no_banco_de_dados(tabela, informacoes_do_arquivo, self.conexao_com_o_banco)
                if resultado:
                    self.salvamento_logger.info(f"Indicador '{indicador}' da fonte IPEADATA tratado e salvo com sucesso.")
                else:
                    self.salvamento_logger.error(f"Falha ao salvar o indicador '{indicador}' da fonte IPEADATA.")
            

            else:
                self.salvamento_logger.warning(f"sFonte '{fonte_dos_dados}' não reconhecida para {indicador}")

    def executar_processo_de_tratamento(self):
        """Executa o fluxo de forma síncrona (aguarda o async internamente)."""
        return asyncio.run(self._executar_tratamento_async()) 

