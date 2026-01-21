from pathlib import Path
import asyncio
import aiohttp # type: ignore
import json
import time

class ColetaESalvamento:
    """
    Classe para baixar dados de URLs de forma automática e rápida.
    """
    
    def __init__(self):
        # Localização do script
        self.base_dir = Path(__file__).resolve().parent
    
        # Pasta onde ficam os arquivos JSON com as URLs (ajustado para subir um nível)
        self.fontes_dir = self.base_dir.parent / 'fontes'
        
        # Pasta onde os arquivos baixados serão guardados
        self.diretorio_saida = self.base_dir.parent / 'dados_coletados'
        self.diretorio_saida.mkdir(parents=True, exist_ok=True)

    async def salvar_dados(self, dados, nome_do_arquivo, subpasta_nome, categoria_nome):
        """
        Cria a estrutura de pastas e grava os dados.
        """
        # Define o caminho: dados_coletados / nome_da_categoria / nome_do_arquivo_json_sem_extensao
        pasta_destino = self.diretorio_saida / categoria_nome / subpasta_nome.replace('.json', '')
        pasta_destino.mkdir(parents=True, exist_ok=True)
        
        caminho_final = pasta_destino / nome_do_arquivo
        
        # Salva o conteúdo de forma síncrona (escrita em disco)
       
        caminho_final.write_bytes(dados)

    async def processar_url(self, sessao, nome_do_arquivo, url, nome_json, nome_categoria):
        """
        Tenta baixar o conteúdo de uma única URL.
        """
        try:
            async with sessao.get(url, timeout=10) as resposta:
                if resposta.status == 200:
                    dados = await resposta.read()
                    await self.salvar_dados(dados, nome_do_arquivo, nome_json, nome_categoria)
                else:
                    print(f"Status {resposta.status} para: {url}")
        except Exception as e:
            print(f"Erro ao baixar {url}: {e}")

    async def realizar_coleta(self):
        """
        Varre as pastas e inicia os downloads.
        """
        tarefas = []
        
        if not self.fontes_dir.exists():
            print(f"Erro: A pasta de fontes não existe em {self.fontes_dir}")
            return

        async with aiohttp.ClientSession() as sessao:
            # Itera sobre as subpastas dentro de 'fontes'
            for pasta_categoria in self.fontes_dir.iterdir():
                if pasta_categoria.is_dir():
                    
                    # Itera sobre arquivos .json dentro de cada subpasta
                    for arquivo_json in pasta_categoria.glob("*.json"):
                        try:
                            with open(arquivo_json, 'r', encoding='utf-8') as f:
                                conteudo = json.load(f)
                            
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

            if tarefas:
                print(f"Iniciando download de {len(tarefas)} arquivos...")
                await asyncio.gather(*tarefas)
                print("Coleta concluída!")

if __name__ == '__main__':
    coleta = ColetaESalvamento()
    
    t1 = time.time()
    asyncio.run(coleta.realizar_coleta())
    print(f"Tempo total: {time.time() - t1:.2f} segundos")