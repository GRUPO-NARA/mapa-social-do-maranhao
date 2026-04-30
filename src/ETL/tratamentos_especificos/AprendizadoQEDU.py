from typing import BinaryIO

from arquivo_de_configuracao import ConfiguracoesProcessoDeETL
import pandas as pd

config = ConfiguracoesProcessoDeETL()
logger_tratamento = config.configuracoes_logging_para_tratamento()

def tratamento_dados_aprendizado_QEDU(arquivo: BinaryIO, informacoes_do_arquivo: dict):
    tabelas_separadas = []
        
        
    tabelas_separadas.append(pd.read_excel(arquivo, sheet_name='municipios'))
    tabelas_separadas.append(pd.read_excel(arquivo, sheet_name='estado'))
                    
    if tabelas_separadas:
        # Consolida todos os arquivos encontrados em um único DataFrame
        tabela_completa = pd.concat(tabelas_separadas, ignore_index=True)
            
            # Dicionários de mapeamento para tradução de IDs e siglas
        mapeamento_de_dependencias = {0: 'total', 1: 'federal', 2: 'estadual', 3: 'municipal', 4: 'privada', 5: 'publica'}
        mapeamento_ciclo = {'AI': 'anos_iniciais', 'AF': 'anos_finais', 'EM': 'ensino_medio'}
                        
            # Aplicação do mapeamento e limpeza de colunas de ID
        tabela_completa['ciclo'] = tabela_completa['ciclo_id'].map(mapeamento_ciclo)
        tabela_completa['dependencia_adm'] = tabela_completa['dependencia_id'].map(mapeamento_de_dependencias)
        tabela_completa.drop(columns=['dependencia_id','ciclo_id'], inplace = True)
        tabela_completa.rename(columns={'ibge_id' : 'cod_municipio'}, inplace=True)
      
            
            # Tradução dinâmica dos nomes das colunas de proficiência
            # Converte prefixos técnicos (mt_ -> matematica_ | lp_ -> portugues_)
        for coluna in tabela_completa.columns:
            if str(coluna).startswith('mt'):
                tabela_completa.rename(columns={coluna:str(coluna).replace('mt','matematica')}, inplace=True)
            elif str(coluna).startswith('lp'):
                tabela_completa.rename(columns={coluna:str(coluna).replace('lp','portugues')}, inplace=True) 
            
            # Segmentação e Inserção 1: Dados de nível Estadual (Código IBGE 21 para o Maranhão)
        tabela_estadual = tabela_completa[tabela_completa['cod_municipio'] == 21]
        tabela_estadual.insert(0, 'id', range(1, len(tabela_estadual) + 1))

            
            # Segmentação e Inserção 2: Dados de nível Municipal
        tabela_municipal= tabela_completa[tabela_completa['cod_municipio'] != 21]
        tabela_municipal.insert(0, 'id', range(1, len(tabela_municipal) + 1))
            # Trata valores ausentes com valor sentinela -1
        tabela_municipal.fillna(-1, inplace=True)
        return tabela_municipal
    else:
        logger_tratamento.error("Nenhuma tabela encontrada para tratamento.")
        return None

