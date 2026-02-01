# Importa a classe de coleta
from coleta import ColetaESalvamento
# Importa a classe de tratamento dos dados municipais
from tratamento_dados_municipais import TratamentoDadosMunicipais
# Biblioteca responsável por executar o processo assíncrono de coleta e salvamento
import asyncio

 # Executa o processo de ETL - Coleta, tratamento e salvamento
if __name__ == '__main__':
    asyncio.run(ColetaESalvamento().realizar_coleta())
    TratamentoDadosMunicipais().executar_processo_de_tratamento()