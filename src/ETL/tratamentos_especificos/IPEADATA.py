from typing import BinaryIO
from pandas import DataFrame
import pandas as pd
from arquivo_de_configuracao import ConfiguracoesProcessoDeETL

config = ConfiguracoesProcessoDeETL()
logger_tratamento = config.configuracoes_logging_para_tratamento()

def tratamento_dados_IPEADATA(arquivo: BinaryIO, informacoes_do_arquivo: dict) -> DataFrame:
    """Realiza o tratamento dos dados coletados da fonte IPEADATA e retorna uma tabela tratada."""
    
    logger_tratamento.info(f"Iniciando tratamento do indicador: {informacoes_do_arquivo.get('indicador')} da fonte IPEADATA.")

    tabela = pd.read_csv(arquivo, skiprows=1, sep=';')

    colunas_para_remover = []

    if 'Sigla' in tabela.columns:
        colunas_para_remover.append('Sigla')

    if tabela.columns[-1].startswith('Unnamed:'):
        colunas_para_remover.append(tabela.columns[-1])

    tabela = tabela.drop(columns=colunas_para_remover, axis=1, errors='ignore')

    colunas_base = ['Código', 'Município']
    
    colunas_base = [coluna for coluna in colunas_base if coluna in tabela.columns]

    colunas_com_variaveis_numericas = [coluna for coluna in tabela.columns if coluna not in colunas_base]

    for coluna in colunas_com_variaveis_numericas:
       
        tabela[coluna] = tabela[coluna].astype(str).str.replace(',', '.', regex=False)

        tabela[coluna] = pd.to_numeric(tabela[coluna], errors='coerce')

    tabela_invertida = pd.melt(tabela, id_vars=colunas_base, var_name='referencia', value_name='valor')

    tabela_invertida['referencia'] = pd.to_numeric(tabela_invertida['referencia'], errors='coerce').astype('Int64')

    if 'Código' in tabela_invertida.columns:
        tabela_invertida.rename(columns={'Código': 'cod_municipio'}, inplace=True)

    tabela_invertida['fonte'] = 'IPEADATA'

    indicador = informacoes_do_arquivo.get("indicador")
    
    tabela_invertida['indicador'] = indicador

    colunas_necessarias_para_formatacao = ['cod_municipio', 'referencia', 'fonte', 'indicador', 'valor']
    colunas_formatadas = [coluna for coluna in colunas_necessarias_para_formatacao if coluna in tabela_invertida.columns]
    tabela_invertida = tabela_invertida[colunas_formatadas]
    
    tabela = tabela_invertida.copy()
    tabela['cod_municipio'] = tabela['cod_municipio'].astype(str).str.zfill(7)
    tabela.insert(0, 'id', range(1, len(tabela) + 1))

    logger_tratamento.info(f"Tratamento do indicador '{indicador}' da fonte IPEADATA concluído com sucesso.")

    return tabela
