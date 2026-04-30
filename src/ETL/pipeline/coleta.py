import asyncio
from pathlib import Path
import aiohttp
from aiohttp import ClientSession, TCPConnector
import json
import ssl
from arquivo_de_configuracao import ConfiguracoesProcessoDeETL


class FluxoDeColeta:
    def __init__(self):
        # Importação das configurações e setup do logger para coleta
        self.config = ConfiguracoesProcessoDeETL()
        self.logger_coleta = self.config.configuracoes_logging_para_coleta()

        self.diretorio_atual = self.config.diretorio_atual
        # Localização do arquivo JSON que contém as URLs para coleta dos dados
        self.arquivo_que_contem_as_urls = self.config.arquivo_que_contem_as_urls

        # Variável para armazenar as informações do indicador, preenchida durante a coleta
        self.informacoes_do_arquivo = {}
        # Lista para manter sincronização entre URLs e suas respectivas informações
        self.lista_de_urls_com_informacoes = []

        self.tarefas_realizadas = False
            
    async def coleta(self, url, sessao: ClientSession) -> bytes:
        """Realiza a requisição para coleta dos dados e retorna o conteúdo em bytes."""
        async with sessao.get(url) as resposta:
            if (resposta.status == 200):

                self.logger_coleta.info(f"Coleta bem-sucedida para a URL: {url}")

                return await resposta.read()
            else:
                # Implementação de retry com backoff exponencial para lidar com falhas temporárias na coleta
                tentativas = 3
                for tentativa in range(1, tentativas + 1):

                    self.logger_coleta.warning(f"Falha na coleta para a URL: {url} - Status: {resposta.status} - Tentativa {tentativa}/{tentativas}")
                    
                    await asyncio.sleep(2 ** tentativa)  
                    async with sessao.get(url) as resposta_retry:
                        if resposta_retry.status == 200:
                            
                            self.logger_coleta.info(f"Coleta bem-sucedida na tentativa {tentativa} para a URL: {url}")
                            
                            return await resposta_retry.read()

                self.logger_coleta.error(f"Falha na coleta para a URL: {url} - Status: {resposta.status}")
                return b""

    def leitura_do_arquivo_das_fontes_dos_indicadores(self) -> list:
        """Realiza a leitura do arquivo JSON que contém as URLs para coleta dos dados e retorna as informações dos dados a serem coletados."""
        self.logger_coleta.info("Lendo arquivo de URLs para coleta.")
    
        if not self.arquivo_que_contem_as_urls.exists():
            self.logger_coleta.error(f"Erro ao carregar URLs para coleta")
            raise FileNotFoundError(f"Arquivo que contêm as URLs das fontes dos dados não foi encontrado: {self.arquivo_que_contem_as_urls}")
            
        with open(self.arquivo_que_contem_as_urls, 'r') as arquivo:
            informacoes_dos_dados = json.load(arquivo)

            self.logger_coleta.info("Arquivo de URLs carregado com sucesso.")

            return informacoes_dos_dados['dados']
        
    def urls_para_coleta(self) -> list:
        """Retorna a lista de URLs que serão coletadas, mantendo sincronização com informações."""
        self.logger_coleta.info("Iniciando processo de coleta das URLs.")
        informacoes_dos_dados = self.leitura_do_arquivo_das_fontes_dos_indicadores()
        urls_para_coleta = []
        self.lista_de_urls_com_informacoes = []
        
        for categoria in informacoes_dos_dados:
            for indicador in categoria['indicadores']:
                informacoes_do_indicador = {
                    "categoria": categoria.get('categoria'),
                    "indicador": indicador.get('indicador'),
                    "fonte": indicador.get('fonte'),
                    "formato_do_arquivo": indicador.get('formato_do_arquivo')
                }
                url = indicador.get('link')
                if url:
                    self.logger_coleta.info(f"URL adicionada para coleta: {url}")
                    urls_para_coleta.append(url)
                    # Mantém sincronização entre URL e informações
                    self.lista_de_urls_com_informacoes.append((url, informacoes_do_indicador))
                    
        return urls_para_coleta
    
    def informacoes_do_indicador(self, indice: int = 0) -> dict:
        """Retorna as informações do indicador para um índice específico."""
        if 0 <= indice < len(self.lista_de_urls_com_informacoes):
            return self.lista_de_urls_com_informacoes[indice][1]
        return {}
    
    async def definicao_das_requisicoes_que_serao_realizadas(self):
        """Define as requisições que serão realizadas para coleta dos dados."""
        self.logger_coleta.info("Definindo requisições para coleta.")
        
        # Desabilita verificação SSL para aceitar certificados auto-assinados
        ssl_context = ssl.create_default_context()
        ssl_context.check_hostname = False
        ssl_context.verify_mode = ssl.CERT_NONE
        
        connector = TCPConnector(ssl=ssl_context)
        try:
            async with ClientSession(connector=connector) as sessao:
                urls_dos_indicadores = self.urls_para_coleta()
                tarefas = [self.coleta(url, sessao) for url in urls_dos_indicadores]
                self.tarefas_realizadas = True
                self.logger_coleta.info("Tarefas de coleta definidas.")
                return await asyncio.gather(*tarefas)
        except Exception as e:
            self.logger_coleta.error(f"Erro ao definir requisições para coleta: {e}")
            return []
        
    async def conteudo_da_requisicao(self):
        """Retorna o conteúdo das requisições realizadas."""
        self.logger_coleta.info("Redirecionando para tratamento dos dados coletados.")
        # Esse método é responsável por retornar o conteúdo das requisições realizadas, que será utilizado no tratamento dos dados.
        if not self.tarefas_realizadas:
            self.logger_coleta.warning("As tarefas de coleta ainda não foram realizadas. Executando definição das requisições.")
            await self.definicao_das_requisicoes_que_serao_realizadas()
        
        if self.tarefas_realizadas:
            self.logger_coleta.info("Tarefas de coleta já realizadas. Enviando conteúdo para tratamento.")
            return await self.definicao_das_requisicoes_que_serao_realizadas()
        return []