from typing import BinaryIO

from arquivo_de_configuracao import ConfiguracoesProcessoDeETL
import pandas as pd

config = ConfiguracoesProcessoDeETL()
logger_tratamento = config.configuracoes_logging_para_tratamento()

def tratamento_dados_SIDRA(arquivo: BinaryIO, informacoes_do_arquivo: dict):
    """Realiza o tratamento dos dados coletados da fonte SIDRA e retorna uma tabela tratada."""

    logger_tratamento.info(f"Iniciando tratamento do indicador: {informacoes_do_arquivo.get('indicador')} da fonte SIDRA.")

    if not arquivo:
        logger_tratamento.error("Arquivo para tratamento não encontrado.")
        return None
    
    # Extração de informações relevantes do arquivo e do indicador para o tratamento
    nome_do_indicador = informacoes_do_arquivo.get("indicador")
    fonte = informacoes_do_arquivo.get("fonte")                        
    tabela = pd.read_csv(arquivo, sep=';', header=3)
    anos = [str(ano) for ano in range(2012, 2027)]
    referencia = [ano for ano in tabela.columns if ano in anos][0]
    linha_referente_ao_primeiro_municipio = tabela['Cód.'].loc[tabela['Cód.'] == '2100055'].index[0]
    linha_referente_ao_ultimo_municipio = tabela['Cód.'].loc[tabela['Cód.'] == '2114007'].index[0]
    tabela = tabela.iloc[linha_referente_ao_primeiro_municipio:linha_referente_ao_ultimo_municipio + 1,:]
  
                        
    # Padronização de colunas
    tabela.rename(columns={'Cód.':'cod_municipio'}, inplace = True)
    tabela['referencia'] = referencia
    tabela['fonte'] = fonte
    tabela['indicador'] = nome_do_indicador
                    
    tabela.drop(columns={tabela.iloc[:,1].name}, inplace = True)
    tabela.rename(columns={str(referencia):'valor'}, inplace = True)
                        
    formato_de_visualizacao_da_tabela = ['cod_municipio', 'referencia', 'fonte', 'indicador', 'valor']
    tabela = tabela[formato_de_visualizacao_da_tabela]

    # Adiciona coluna de chave primária incremental
    tabela.insert(0, 'id', range(1, len(tabela) + 1))

    # Conversão robusta de valores: trata tanto ',' quanto '.' como decimais
    valores_convertidos = []
    for valor in tabela['valor']:
        try:
            valor_convertido_para_string = str(valor).strip().replace(',', '.')
            valor_float = float(valor_convertido_para_string)
            
            if valor_float % 1 == 0:
                valores_convertidos.append(int(valor_float))
            else:
                valores_convertidos.append(round(valor_float, 3))
        except (ValueError, TypeError):
            logger_tratamento.warning(f"Não foi possível converter o valor '{valor}' para número.")
            valores_convertidos.append(valor)  # Mantém o valor original se a conversão falhar

    tabela['valor'] = valores_convertidos

    logger_tratamento.info(f"Tratamento do indicador '{nome_do_indicador}' da fonte SIDRA concluído com sucesso.")
    return tabela