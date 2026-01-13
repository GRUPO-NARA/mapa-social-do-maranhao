from pathlib import Path
import asyncio
import aiohttp # type: ignore
import json
import time

class ColetaESalvamento:
    """
    Classe para baixar dados de URLs de forma automática e rápida.
    Lê arquivos JSON com listas de links e salva o conteúdo no computador.
    """
    
    def __init__(self):
        """
        Configura as pastas onde o script vai buscar os links e salvar os arquivos.
        """
        # Pega a pasta onde este script está salvo
        self.base_dir = Path(__file__).resolve().parent
        
        # Pasta onde ficam os arquivos JSON com as URLs
        self.fontes_dir = self.base_dir.parent / 'fontes'
        
        # Pasta onde os arquivos baixados serão guardados
        self.dados_brutos_dir = self.base_dir.parent / 'dados' / 'dados-brutos'

    async def salvar_dados(self, dados, nome_do_arquivo, subpasta):
        """
        Cria a pasta necessária e grava os dados baixados no disco.
        """
        # Define o nome da pasta de destino removendo o '.json' do nome da fonte
        pasta_destino = self.dados_brutos_dir / subpasta.replace('.json', '')
        
        # Cria as pastas caso elas não existam
        pasta_destino.mkdir(parents=True, exist_ok=True)
        
        caminho_final = pasta_destino / nome_do_arquivo
        
        # Salva o conteúdo baixado (em formato binário)
        with open(caminho_final, 'wb') as f:
            f.write(dados)

    async def processar_url(self, sessao, nome_do_arquivo, url, nome_fonte):
        """
        Tenta baixar o conteúdo de uma única URL.
        """
        try:
            # Faz o pedido de acesso ao link com limite de 10 segundos de espera
            async with sessao.get(url, timeout=10) as resposta:
                # Se o site responder que está tudo OK (status 200)
                if resposta.status == 200:
                    dados = await resposta.read()
                    # Envia os dados para serem salvos
                    await self.salvar_dados(dados, nome_do_arquivo, nome_fonte)
        except Exception as e:
            # Mostra erro se não conseguir baixar
            print(f"Erro ao baixar {url}: {e}")

    async def realizar_coleta(self):
        """
        Lê as fontes de links e inicia todos os downloads ao mesmo tempo.
        """
        tarefas = []
        
        # Cria uma sessão de internet para realizar os downloads
        async with aiohttp.ClientSession() as sessao:
    
            # Procura todos os arquivos .json na pasta de fontes
            for arquivo_json in self.fontes_dir.glob('*.json'):
                try:
                    with open(arquivo_json, 'r', encoding='utf-8') as f:
                        conteudo = json.load(f)
                        
                        # Para cada nome e link dentro do arquivo JSON
                        for nome_item, url in conteudo.items():
                            # Prepara a tarefa de download
                            tarefa = asyncio.create_task(
                                self.processar_url(sessao, nome_item, url, arquivo_json.name)
                            )
                            tarefas.append(tarefa)
                except Exception as e:
                    print(f"Erro ao ler arquivo {arquivo_json.name}: {e}")

            # Executa todos os downloads da lista simultaneamente
            if tarefas:
                await asyncio.gather(*tarefas)

#if __name__ == '__main__':
    # Cria o objeto de coleta
 #   coleta = ColetaESalvamento()
    
    # Marca o tempo de início
  #  t1 = time.time()
    
    # Roda o processo de coleta assíncrona
   # asyncio.run(coleta.realizar_coleta())
    
    # Mostra quanto tempo demorou no total
   # print(f"Tempo total: {time.time() - t1:.2f} segundos")