
from pipeline.tratamento import FluxoDeTratamento
import time

def ETL():
    """
    Fluxo de execução do processo de ETL:
    1. Coleta dos dados:
        - Leitura do arquivo JSON que contém as URLs para coleta dos dados.
        - Realização das requisições para coleta dos dados.
        - Redirecionamento dos dados coletados para tratamento.
    2. Tratamento dos dados:
        - Escolha do tratamento específico para cada fonte de dados.
        - Tratamento dos dados conforme a fonte de origem.
    3. Carga dos dados:
        - Armazenamento dos dados tratados em um banco de dados ou arquivo.
    """

    tratamento = FluxoDeTratamento()
    tratamento.executar_processo_de_tratamento()


if __name__ == "__main__":
    inicio = time.time()
    ETL()
    fim = time.time()
    print("Processo de ETL concluído com sucesso.")
    print(f"Tempo total de execução do processo de ETL: {fim - inicio:.2f} segundos")

