import pandas as pd
from pathlib import Path
import os
from funcoes_de_apoio import AuxiliaresTratamento
from operacoes_db import ConexaoPostgres

funcoes_auxiliares = AuxiliaresTratamento()
database = ConexaoPostgres()

class ArquivodeTratamento:
    
    def __init__(self):
        
        self.diretorio_atual = Path(__file__).resolve().parent

        self.diretorio_dados_coletados = self.diretorio_atual / '..' / 'dados_coletados'
        
        if not os.path.exists(self.diretorio_dados_coletados):
            raise Exception("Não foi encontado o diretório dos dados coletados. É preciso realizar o processo de coleta das informações!")

        self.pastas_informacoes_municipais = self.diretorio_dados_coletados / 'informacoes_municipais'

        self.topicos = os.listdir(self.pastas_informacoes_municipais)
        
    def executar_processo_de_tratamento(self):
        
        self.arquivos_SIDRA()
        self.arquivos_SAGICAD()
    
    def arquivos_SIDRA(self) -> None: 
        """
        Processa arquivos CSV vindos do SIDRA (IBGE).
        Lida com cabeçalhos complexos, extrai unidades de medida e filtra municípios.
        """
       
        for pasta in self.topicos:
            caminho_arquivos = self.pastas_informacoes_municipais / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:
                
                if str(arquivo).endswith('SIDRA.csv'):
                    try:
                        caminho_arquivo = caminho_arquivos / arquivo
                        
                        # Lê apenas as primeiras linhas para descobrir a unidade de medida
                        dataframe_header = pd.read_csv(caminho_arquivo, sep=';', header=None, nrows=2)
                        unidade = dataframe_header.iloc[1,0]
                        inicio_chave = unidade.find('(')
                        unidade = unidade[inicio_chave + 1: -1] # Extrai o texto dentro dos parênteses
                        
                        # Lê os dados reais começando da linha 4
                        dataframe = pd.read_csv(caminho_arquivo, sep=';', header=3)
                        
                        # Identifica qual coluna é o ano de referência (entre 2012 e 2026)
                        anos = [str(ano) for ano in range(2012, 2027)]
                        referencia = [ano for ano in dataframe.columns if ano in anos][0]
                        
                        # Limpa a string da unidade
                        unidade = unidade.replace(" ",'_')
                        unidade = funcoes_auxiliares.remover_acentos(unidade)
                        
                        # Filtra apenas os municípios do Maranhão (pelos códigos de início e fim)
                        linha_inicio_codigo = dataframe['Cód.'].loc[dataframe['Cód.'] == '2100055'].index[0]
                        linha_ultimo_codigo_municipal = dataframe['Cód.'].loc[dataframe['Cód.'] == '2114007'].index[0]
                        dataframe = dataframe.iloc[linha_inicio_codigo:linha_ultimo_codigo_municipal + 1,:]
                        
                        # Extrai informações de Fonte e Indicador do nome do arquivo
                        caminho_normal = arquivo
                        palavra_csv = caminho_normal.find('.csv')
                        caminho_normal = caminho_normal[:palavra_csv]
                        fonte = caminho_normal[caminho_normal.rfind('_') + 1:]
                        indicador = arquivo[:arquivo.rfind('_')]
                        
                        # Padroniza as colunas do DataFrame
                        dataframe.rename(columns={'Cód.':'cod_municipio'}, inplace = True)
                        dataframe['referencia'] = referencia
                        dataframe['fonte'] = fonte
                        dataframe['indicador'] = indicador
                        
                        if unidade.lower() == 'habitante_por_quilometro_quadrado':
                            unidade = 'densidade_demografica'
                        dataframe['unidade'] = unidade.lower()
                        
                        # Remove a coluna de nome do município e renomeia o ano para 'valor'
                        dataframe.drop(columns={dataframe.iloc[:,1].name}, inplace = True)
                        dataframe.rename(columns={str(referencia):'valor'}, inplace = True)
                        
                        # Formata o tipo de dado da coluna valor
                        unidades_inteiras = ['pessoas','anos']
                        formato_dataframe = ['cod_municipio', 'referencia', 'fonte', 'indicador', 'valor', 'unidade']
                        dataframe = dataframe[formato_dataframe]

                        if unidade.lower() in unidades_inteiras:
                            dataframe['valor'] = dataframe['valor'].astype(int)
                        else:
                            dataframe['valor'] = dataframe['valor'].astype(str).str.replace(',','.').astype(float).round(3)
                        
                        
                         
                        nome_topico = pasta
                        database.verificar_existencia_schema(nome_topico)
                        database.inserir_dados(
                            indicador,
                            nome_topico,
                            dataframe
                        )
                       
                    except Exception:
                        pass
    
    def arquivos_SAGICAD(self) -> None:
        
        for pasta in self.topicos:
            caminho_arquivos = self.pastas_informacoes_municipais / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:   
                if arquivo.endswith('SAGICAD.csv'):
                    caminho_arquivo = caminho_arquivos / arquivo

                    dataframe = pd.read_csv(caminho_arquivo, sep=',', encoding='latin-1')
                    
                    # Extrai a fonte do nome do arquivo
                    fonte = arquivo[arquivo.rfind('_') + 1:arquivo.find('.')]

                    dataframe = funcoes_auxiliares.corrigir_codigo_ibge(dataframe)
                    
                    # Padroniza nomes das colunas (minúsculo, sem acento, com underscore)
                    colunas_formatadas = [funcoes_auxiliares.remover_acentos(col.replace(' ','_').replace('/','_').lower()) for col in dataframe.columns]
                    dataframe.columns = colunas_formatadas
                    
                    # Identifica as colunas que vêm após a coluna de 'referencia' (indicadores)
                    localizacao_ref = colunas_formatadas.index('referencia')
                    colunas_indicadores = dataframe.iloc[:, localizacao_ref + 1:].columns
                    
                    topico = pasta
                    # Se houver mais de um indicador no mesmo arquivo, separa em arquivos diferentes
                    if len(colunas_indicadores) > 1:
                        for coluna in colunas_indicadores:
                            dataframe_atualizado = dataframe.drop(columns=colunas_indicadores, axis = 1)
                            dataframe_atualizado[coluna] = dataframe[coluna]
                            
                            indicador = coluna
                            
                            dataframe_atualizado.rename(columns={indicador: 'valor', 'codigo': 'cod_municipio'}, inplace=True)
                            
                            # Usa funções auxiliares para organizar e salvar
                            dataframe_atualizado = funcoes_auxiliares.organizar_dataframe_SAGICAD(fonte, indicador, dataframe_atualizado)
                            dataframe_atualizado = funcoes_auxiliares.tratamento_valores_nulos_SAGICAD(dataframe_atualizado)
                            database.verificar_existencia_schema(topico)
                            database.inserir_dados(
                                indicador,
                                topico,
                                dataframe_atualizado
                            )   

                    else:
                        # Processo para arquivo com apenas um indicador
                        
                        indicador = colunas_indicadores[0]
                        
                        dataframe.rename(columns={'codigo': 'cod_municipio', indicador: 'valor'}, inplace=True)
                        
                        dataframe = funcoes_auxiliares.organizar_dataframe_SAGICAD(fonte, indicador, dataframe)
                        dataframe = funcoes_auxiliares.tratamento_valores_nulos_SAGICAD(dataframe)
                        database.verificar_existencia_schema(topico)
                        database.inserir_dados(
                            indicador,
                            topico,
                            dataframe
                        )  
                    
                        
                        

if __name__ == '__main__':
    # Inicia o processo completo
    ArquivodeTratamento().executar_processo_de_tratamento()
    #ArquivodeTratamento().arquivos_taxas_de_rendimento_AI_QEDU()
    #ArquivodeTratamento().arquivos_SIDRA()
    #ArquivodeTratamento().arquivos_SAGICAD()