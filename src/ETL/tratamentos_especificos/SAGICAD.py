from typing import BinaryIO
from pandas import DataFrame
from funcoes_de_apoio.para_tratamento import FuncoesDeApoioParaTratamento
from arquivo_de_configuracao import ConfiguracoesProcessoDeETL
import pandas as pd

config = ConfiguracoesProcessoDeETL()
logger_tratamento = config.configuracoes_logging_para_tratamento()
funcoes_auxiliares = FuncoesDeApoioParaTratamento()

def tratamento_dados_SAGICAD(arquivo: BinaryIO, informacoes_do_arquivo: dict):

    logger_tratamento.info(f"Iniciando tratamento do indicador: {informacoes_do_arquivo.get('indicador')} da fonte SAGICAD.")

    tabela = pd.read_csv(arquivo, sep=',', encoding='latin-1')                
    fonte = informacoes_do_arquivo.get("fonte")
    tabela_com_codigo_do_ibge_corrigido = funcoes_auxiliares.corrigir_codigo_ibge(tabela)
    
    # Normalização de cabeçalhos
    colunas_formatadas = [funcoes_auxiliares.remover_acentos(col.replace(' ','_').replace('/','_').lower()) for col in tabela_com_codigo_do_ibge_corrigido.columns]
    tabela_com_codigo_do_ibge_corrigido.columns = colunas_formatadas
    
    tabela_corrigida = tabela_com_codigo_do_ibge_corrigido
    # Identificação de colunas de indicadores (após a coluna de referência)
    localizacao_da_coluna_de_referencia = colunas_formatadas.index('referencia')
    colunas_com_indicadores = tabela_com_codigo_do_ibge_corrigido.iloc[:, localizacao_da_coluna_de_referencia + 1:].columns
    
    # Lógica para arquivos Multi-Indicador
    if len(colunas_com_indicadores) > 1:
        for coluna in colunas_com_indicadores:
            tabela_atualizada = tabela_corrigida.drop(columns=colunas_com_indicadores, axis=1)
            tabela_atualizada[coluna] = tabela_corrigida[coluna]
            
            indicador = coluna
            tabela_atualizada.rename(columns={indicador: 'valor', 'codigo': 'cod_municipio'}, inplace=True)                          

            # Tratamento e salvamento via auxiliares
            tabela_atualizada = funcoes_auxiliares.organizar_dataframe_SAGICAD(fonte, indicador, tabela_atualizada)
            tabela_atualizada = funcoes_auxiliares.tratamento_valores_nulos_SAGICAD(tabela_atualizada)
            tabela_atualizada.insert(0, 'id', range(1, len(tabela_atualizada) + 1))
            
            tabela = tabela_atualizada

    else:
        # Lógica para arquivos de indicador único
   
        indicador = colunas_com_indicadores[0]
        tabela_corrigida.rename(columns={'codigo': 'cod_municipio', indicador: 'valor'}, inplace=True)
    
        tabela = funcoes_auxiliares.organizar_dataframe_SAGICAD(fonte, indicador, tabela_corrigida)
        tabela = funcoes_auxiliares.tratamento_valores_nulos_SAGICAD(tabela)
        
        tabela.insert(0, 'id', range(1, len(tabela) + 1))
    
    logger_tratamento.info(f"Tratamento do indicador '{indicador}' da fonte SAGICAD concluído com sucesso.")

    return tabela
                        