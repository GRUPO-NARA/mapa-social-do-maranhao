"""
Script principal para executar o processo completo de ETL
(Coleta, Tratamento e Salvamento de dados)
"""

import asyncio
import sys
from pathlib import Path
import time

# Adiciona o diretório pipeline ao path para importações
sys.path.insert(0, str(Path(__file__).parent))

from fluxo.coleta import FluxoDeColeta
from fluxo.tratamento import FluxoDeTratamento
from fluxo.salvamento import FluxoDeSalvamento


async def executar_etl_completo():
    """Executa o pipeline completo de ETL"""
    try:
        print("Iniciando o processo de ETL...\n")
        
        # 1. COLETA - Uma única instância
        print("-" * 30)
        print("1 - COLETA DE DADOS")
        print("-" * 30)
        coleta = FluxoDeColeta()
        urls = coleta.SeparacaoDasUrlsParaColeta()
        print(f"{len(urls)} URLs preparadas para coleta\n")
        print("Coleta realizada com sucesso\n")
        
        # 2. TRATAMENTO - Passa a mesma instância de coleta
        print("-" * 30)
        print("2 - TRATAMENTO DE DADOS")
        print("-" * 30)
        tratamento = FluxoDeTratamento(coleta)
        await tratamento.Tratamento()
        print("\nDados tratados com sucesso\n")
        
        # 3. SALVAMENTO - Passa a mesma instância de tratamento
        print("-" * 30)
        print("3 - SALVAMENTO NO BANCO DE DADOS")
        print("-" * 30)
        salvamento = FluxoDeSalvamento(tratamento)
        conexao = salvamento.EstabelecerConexao()
        print("Conexão com banco de dados estabelecida")
        
        # Salva todos os indicadores processados
        urls = coleta.SeparacaoDasUrlsParaColeta()
        indicadores_salvos = 0
        for indice in range(len(urls)):
            informacoes_do_indicador = coleta.InformacoesDoIndicador(indice)
            if informacoes_do_indicador:
                resultado = await salvamento.Salvar(informacoes_do_indicador, conexao, indice)
                if resultado:
                    indicadores_salvos += 1
        
        print(f"\n{indicadores_salvos} indicador(es) salvo(s) com sucesso\n")
        
        # Fecha a conexão
        conexao.dispose()
    
        print("ETL concluído com sucesso!")

        
    except Exception as e:
        print(f"\n❌ ERRO NO PROCESSO DE ETL: {e}\n")
        raise


if __name__ == "__main__":
    inicio = time.time()
    try:
        asyncio.run(executar_etl_completo())
    except KeyboardInterrupt:
        print("\n\n⚠️  Processo interrompido pelo usuário\n")
        sys.exit(1)
    except Exception as e:
        print(f"\n⚠️  Execução finalizada com erro\n")
        sys.exit(1)
    fim = time.time()
    duracao = fim - inicio
    print(f"\nTempo total de execução: {duracao:.2f} segundos\n")
