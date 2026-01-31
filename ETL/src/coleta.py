from pathlib import Path # Biblioteca utilizada para identificação e navegação entre diretórios

# Bibliotecas utilizadas para requisição assíncrona HTTP
import asyncio
import aiohttp 
from aiohttp import ClientSession

import json # Biblioteca responsável por realizar a abertura dos arquivos .json das fontes governamentais

class ColetaESalvamento:
    """
    Classe responsável por reunir funções de coleta e salvamento dos dados.

    Essa classe gerencia o processo de requisição assíncrona HTTP dos dados em fontes governamentais
    e o salvamento dos arquivos CSV/XLSX na pasta de dados coletados.
    """
    
    def __init__(self):
        # Localização do script 
        self.base_dir = Path(__file__).resolve().parent
    
        # Pasta onde ficam as subpastas de informações estaduais e municipais, que contêm arquivos .json
        self.fontes_dir = self.base_dir.parent / 'fontes'
        
        # Pasta onde os arquivos baixados serão guardados
        self.diretorio_saida = self.base_dir.parent / 'dados_coletados'
        
        # Verificação se a pasta existe; se não existir, é criada automaticamente.
        self.diretorio_saida.mkdir(parents=True, exist_ok=True) 

    async def salvar_dados(self, dados: bytes, nome_do_arquivo: str, subpasta_nome: str, categoria_nome: str) -> None:
        """
        Função responsável por realizar o salvamento dos dados governamentais na pasta destino.

        Args:
            dados (bytes): Conteúdo do arquivo.
            nome_do_arquivo (str): Nome do arquivo específico para salvamento.
            subpasta_nome (str): Nome do tópico indicador (baseado no arquivo JSON).
            categoria_nome (str): Tipo das informações, podendo ser: estaduais ou municipais.
        
        Returns:
            None.
        """
        # Define o caminho: dados_coletados / nome_da_categoria / nome_do_arquivo_json_sem_extensao
        pasta_destino = self.diretorio_saida / categoria_nome / subpasta_nome.replace('.json', '')
        
        # Verificação de existência da pasta destino
        pasta_destino.mkdir(parents=True, exist_ok=True)
        
        # Caminho final do arquivo na pasta de salvamento
        caminho_final = pasta_destino / nome_do_arquivo
        
        # Salva o conteúdo de forma síncrona (escrita em disco)
        caminho_final.write_bytes(dados)

    async def processar_url(self, sessao: ClientSession, nome_do_arquivo: str, url: str, nome_json: str, nome_categoria: str) -> None:
        """
        Função responsável por realizar a requisição assíncrona HTTP nos sites que contêm os dados governamentais.

        Args:
            sessao (ClientSession): Sessão do cliente responsável pela requisição web.
            nome_do_arquivo (str): Nome do indicador/arquivo a ser salvo.
            url (str): Endereço web da página onde estão localizados os dados.
            nome_json (str): Nome do arquivo fonte, exemplo: dados_assistencia_social.json.
            nome_categoria (str): Tipo das informações, que podem ser: municipais ou estaduais.
        """
        # Tentativa de realizar requisição do tipo GET na URL da vez.
        try:
            # Realiza a requisição e armazena a resposta do servidor.
            async with sessao.get(url, timeout=10) as resposta:
                # Se a resposta do servidor for positiva (código 200).
                if resposta.status == 200:
                    # É realizada a leitura em bytes dos dados presentes na resposta.
                    dados = await resposta.read()
                    # Após a leitura, os dados são redirecionados para a função de salvamento.
                    await self.salvar_dados(dados, nome_do_arquivo, nome_json, nome_categoria)
                # Se a requisição for malsucedida, retorna o status do erro.
                else:
                    print(f"Status {resposta.status} para: {url}")
        # Caso ocorra algum erro, a exceção é retornada para análise.
        except Exception as e:
            print(f"Erro ao baixar {nome_do_arquivo}: {e}")

    async def realizar_coleta(self) -> None:
        """
        Função responsável por realizar a varredura nas pastas das fontes, analisando cada arquivo .json e extraindo seu nome e URL.
        """
        # Lista de tarefas para armazenamento das requisições assíncronas
        tarefas = []
        
        # Verifica se o diretório das fontes existe; caso não exista, exibe uma mensagem de aviso.
        if not self.fontes_dir.exists():
            print(f"Erro: A pasta de fontes não existe em {self.fontes_dir}")
            return

        # Criação de uma sessão do cliente
        async with aiohttp.ClientSession() as sessao:
            # Itera sobre as subpastas dentro de 'fontes'
            for pasta_categoria in self.fontes_dir.iterdir():
                # Verifica se o item é um diretório
                if pasta_categoria.is_dir():
                    
                    # Itera sobre arquivos .json dentro de cada subpasta
                    for arquivo_json in pasta_categoria.glob("*.json"):
                        try:
                            # Realiza a leitura do conteúdo presente no arquivo .json
                            with open(arquivo_json, 'r', encoding='utf-8') as f:
                                conteudo = json.load(f)
                            
                            # Para cada nome e URL, é criada uma nova tarefa assíncrona de processamento
                            for nome_item, url in conteudo.items():
                                tarefa = asyncio.create_task(
                                    self.processar_url(
                                        sessao, 
                                        nome_item, 
                                        url, 
                                        arquivo_json.name, 
                                        pasta_categoria.name
                                    )
                                )
                                tarefas.append(tarefa)
                        except Exception as e:
                            print(f"Erro ao ler arquivo {arquivo_json.name}: {e}")
            
            # Caso a lista de tarefas não esteja vazia
            if tarefas:
                # Realiza todas as tarefas simultaneamente de forma assíncrona
                print(f"Iniciando download de {len(tarefas)} arquivos...")
                await asyncio.gather(*tarefas)
                # Mensagem de finalização da coleta
                print("Coleta concluída!")