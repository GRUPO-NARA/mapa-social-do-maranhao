from pathlib import Path
import asyncio
import aiohttp
import json
import time

class ColetaESalvamento:
    
    def __init__(self):
        
        self.base_dir = Path(__file__).resolve().parent
        
        self.fontes_dir = self.base_dir.parent / 'fontes'
        
        self.dados_brutos_dir = self.base_dir.parent / 'dados' / 'dados-brutos'

    async def salvar_dados(self, dados, nome_do_arquivo, subpasta):
        
        pasta_destino = self.dados_brutos_dir / subpasta.replace('.json', '')
        
        pasta_destino.mkdir(parents=True, exist_ok=True)
        
        caminho_final = pasta_destino / nome_do_arquivo
        
        with open(caminho_final, 'wb') as f:
            f.write(dados)

    async def processar_url(self, sessao, nome_do_arquivo, url, nome_fonte):
        try:
            async with sessao.get(url, timeout=10) as resposta:
                if resposta.status == 200:
                    dados = await resposta.read()
                    await self.salvar_dados(dados, nome_do_arquivo, nome_fonte)
        except Exception as e:
            print(f"Erro ao baixar {url}: {e}")

    async def realizar_coleta(self):
        tarefas = []
        
        async with aiohttp.ClientSession() as sessao:
    
            for arquivo_json in self.fontes_dir.glob('*.json'):
                try:
                    with open(arquivo_json, 'r', encoding='utf-8') as f:
                        conteudo = json.load(f)
                        
                        for nome_item, url in conteudo.items():
                        
                            tarefa = asyncio.create_task(
                                self.processar_url(sessao, nome_item, url, arquivo_json.name)
                            )
                            tarefas.append(tarefa)
                except Exception as e:
                    print(f"Erro ao ler arquivo {arquivo_json.name}: {e}")

            if tarefas:
                await asyncio.gather(*tarefas)

if __name__ == '__main__':
    coleta = ColetaESalvamento()
    t1 = time.time()
    asyncio.run(coleta.realizar_coleta())
    print(f"Tempo total: {time.time() - t1:.2f} segundos")