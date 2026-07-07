import asyncio
from aiohttp import ClientSession, TCPConnector, ClientPayloadError
from configuracoes.config import LoggerParaColeta, MapeamentoDiretoriosEArquivos
import json
import os
import ssl
from urllib.parse import urlparse

class FluxoDeColeta:
    def __init__(self):
        self.logger_coleta = LoggerParaColeta()
        self.mapeamento_diretorios_e_arquivos = MapeamentoDiretoriosEArquivos()
        self.informacoes_do_arquivo = {}
        self.lista_de_urls_com_informacoes = []
        self.urls_para_requisicao = []
        self.tarefas_realizadas = False
        self.dados_coletados = []
        # Define o limite de requisições simultâneas (ex: 5 por vez)
        self.semaforo = asyncio.Semaphore(5)
        self.hosts_tls_inseguro = self._carregar_hosts_tls_inseguro()

    def LeituraDosDadosParaColeta(self) -> list:
        """Realiza a leitura do arquivo JSON que contém as URLs para coleta."""
        self.logger_coleta.info("Lendo arquivo de URLs para coleta.")
    
        if not self.mapeamento_diretorios_e_arquivos.arquivo_dos_dados_para_coleta.exists():
            self.logger_coleta.error("Erro ao carregar URLs para coleta")
            raise FileNotFoundError(f"Arquivo não encontrado: {self.mapeamento_diretorios_e_arquivos.arquivo_dos_dados_para_coleta}")
            
        with open(self.mapeamento_diretorios_e_arquivos.arquivo_dos_dados_para_coleta, 'r') as arquivo:
            informacoes_dos_dados = json.load(arquivo)
            self.logger_coleta.info("Arquivo de URLs carregado com sucesso.")
            return informacoes_dos_dados['dados']
    
    def SeparacaoDasUrlsParaColeta(self) -> list:
        """Retorna a lista de URLs que serão coletadas."""
        dados = self.LeituraDosDadosParaColeta()
        self.lista_de_urls_com_informacoes = []
        self.urls_para_requisicao = [] # Limpa a lista para evitar duplicidade em execuções repetidas
        
        for categoria in dados:
            for indicador in categoria['indicadores']:
                informacoes_do_indicador = {
                    "categoria": categoria.get('categoria'),
                    "indicador": indicador.get('indicador'),
                    "fonte": indicador.get('fonte'),
                    "formato_do_arquivo": indicador.get('formato_do_arquivo')
                }
                url = indicador.get('link')
                if url:
                    self.urls_para_requisicao.append(url)
                    self.lista_de_urls_com_informacoes.append((url, informacoes_do_indicador))
                    
        return self.urls_para_requisicao
    
    def InformacoesDoIndicador(self, indice: int = 0) -> dict:
        if 0 <= indice < len(self.lista_de_urls_com_informacoes):
            return self.lista_de_urls_com_informacoes[indice][1]
        return {}
    
    async def RequisicoesQueSeraoRealizadas(self):
        """Define e executa as requisições assíncronas controlando a concorrência."""
        self.logger_coleta.info("Definindo requisições para coleta.")
        
        ssl_context = ssl.create_default_context()
        
        # Headers globais para simular um navegador comum
        headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Accept-Encoding": "gzip, deflate"
        }
        
        connector = TCPConnector(ssl=ssl_context)
        try:
            async with ClientSession(connector=connector, headers=headers) as sessao:
                tarefas = [self.Coleta(url, sessao) for url in self.urls_para_requisicao]
                self.tarefas_realizadas = True
                self.logger_coleta.info("Tarefas de coleta definidas.")
                
                # Executa respeitando o Semáforo interno do método Coleta
                resultados = await asyncio.gather(*tarefas)
                return resultados
        except Exception as e:
            self.logger_coleta.error(f"Erro ao definir requisições para coleta: {e}")
            return []
    
    async def Coleta(self, url, sessao: ClientSession) -> bytes:
        """Realiza a requisição com controle de concorrência e tratamento robusto de erro."""
        tentativas = 3
        
        # O semáforo garante que apenas X requisições rodem em paralelo
        async with self.semaforo:
            for tentativa in range(1, tentativas + 1):
                try:
                    async with sessao.get(url, timeout=30, ssl=self._ssl_para_url(url)) as resposta:
                        if resposta.status == 200:
                            # .read() pode disparar ClientPayloadError se a conexão cair no meio
                            return await resposta.read()
                        
                        self.logger_coleta.warning(
                            f"Falha na coleta: {url} - Status: {resposta.status} - Tentativa {tentativa}/{tentativas}"
                        )
                except (ClientPayloadError, asyncio.TimeoutError, Exception) as e:
                    self.logger_coleta.warning(
                        f"Erro de rede/payload em {url}: {str(e)} - Tentativa {tentativa}/{tentativas}"
                    )
                
                # Se não foi a última tentativa, aguarda antes de tentar novamente (Backoff)
                if tentativa < tentativas:
                    await asyncio.sleep(2 ** tentativa)
            
            self.logger_coleta.error(f"Falha definitiva na coleta para a URL: {url}")
            return b""

    def _carregar_hosts_tls_inseguro(self) -> set[str]:
        hosts = os.getenv(
            "PIPELINE_HOSTS_INSEGUROS",
            "dataimesc.imesc.ma.gov.br"
        )
        return {
            host.strip().lower()
            for host in hosts.split(",")
            if host.strip()
        }

    def _ssl_para_url(self, url: str):
        host = urlparse(url).hostname
        if host and host.lower() in self.hosts_tls_inseguro:
            self.logger_coleta.warning(
                f"Validação TLS desativada apenas para o host permitido: {host}"
            )
            return False
        return None

    async def RespostaDaRequisicao(self):
        if not self.tarefas_realizadas:
            self.logger_coleta.warning("As tarefas de coleta ainda não foram realizadas. Executando definição das requisições.")
            self.dados_coletados = await self.RequisicoesQueSeraoRealizadas()
        
        if self.tarefas_realizadas:
            self.logger_coleta.info("Tarefas de coleta já realizadas. Enviando conteúdo para tratamento.")
            return self.dados_coletados
        return []
