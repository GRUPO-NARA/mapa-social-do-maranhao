"""
Script principal para executar o processo completo de ETL
(Coleta, Tratamento e Salvamento de dados)
"""

import asyncio
import os
import sys
from pathlib import Path
import time
import schedule

# Adiciona o diretório pipeline ao path para importações
sys.path.insert(0, str(Path(__file__).parent))

from fluxo.coleta import FluxoDeColeta
from fluxo.tratamento import FluxoDeTratamento
from fluxo.salvamento import FluxoDeSalvamento


def agendamento_habilitado() -> bool:
    return os.getenv("PIPELINE_AGENDADOR_ATIVADO", "false").strip().lower() in {
        "1",
        "true",
        "yes",
        "sim",
    }


def intervalo_agendamento_minutos() -> int:
    valor = os.getenv("PIPELINE_AGENDADOR_INTERVALO", "2")
    try:
        intervalo = int(valor)
    except ValueError as erro:
        raise ValueError("PIPELINE_AGENDADOR_INTERVALO deve ser um numero inteiro") from erro

    if intervalo < 1:
        raise ValueError("PIPELINE_AGENDADOR_INTERVALO deve ser maior que zero")

    return intervalo


def executar_ao_iniciar_agendamento() -> bool:
    return os.getenv("PIPELINE_EXECUCAO_AUTOMATICA", "true").strip().lower() in {
        "1",
        "true",
        "yes",
        "sim",
    }


async def executar_etl_completo():
    """Executa o pipeline completo de ETL"""
    try:
        print("Iniciando a execução do pipeline...\n")
        print("-" * 30)
        print("Iniciando a Coleta de Dados")
        coleta = FluxoDeColeta()
        urls = coleta.SeparacaoDasUrlsParaColeta()
        print(f"{len(urls)} URLs preparadas para coleta\n")
        print("Coleta realizada com sucesso\n")

        print("-" * 30)
        print("Iniciando o Tratamento de Dados")
        tratamento = FluxoDeTratamento(coleta)
        await tratamento.Tratamento()
        print("\nDados tratados com sucesso\n")

        print("-" * 30)
        print("Iniciando o Salvamento no Banco de Dados")
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

        print("Execução do pipeline finalizada com sucesso\n")


    except Exception as e:
        print(f"\nErro no processo de pipeline: {e}\n")
        raise


if __name__ == "__main__":
    tempo_inicio_pipeline = time.time()
    try:
        if agendamento_habilitado():

            intervalo = intervalo_agendamento_minutos()

            if executar_ao_iniciar_agendamento():
                asyncio.run(executar_etl_completo())

            # Agendamento da execução do pipeline a cada intervalo definido
            # É possível mudar o intervalo para minutos, horas ou dias, conforme necessário
            # Para utilização em testes, é recomendado utilizar minutos, para produção, horas ou dias
            schedule.every(intervalo).minutes.do(
                lambda: asyncio.run(executar_etl_completo())
            )

            while True:
                schedule.run_pending()
                time.sleep(1)
        else:
            asyncio.run(executar_etl_completo())
    except KeyboardInterrupt:
        print("Processo interrompido")
        sys.exit(1)
    except Exception as e:
        print("Execução finalizada com erro")
        sys.exit(1)
    termino_pipeline = time.time()
    duracao_do_pipeline = termino_pipeline - tempo_inicio_pipeline
    print(f"\nTempo de execução do pipeline: {duracao_do_pipeline:.2f} segundos\n")
