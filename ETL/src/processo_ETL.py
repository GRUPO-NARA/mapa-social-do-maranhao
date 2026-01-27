from coleta import ColetaESalvamento
from tratamento_dados_municipais import TratamentoDadosMunicipais
import asyncio

if __name__ == '__main__':
    asyncio.run(ColetaESalvamento().realizar_coleta())
    TratamentoDadosMunicipais().executar_processo_de_tratamento()