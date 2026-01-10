import pandas as pd
from pathlib import Path
import os
from numpy import int64
import unicodedata
from pandas import DataFrame

class ArquivodeTratamento():
    
    def __init__(self):
        
        self.diretorio_atual = Path(__file__).resolve().parent
        
        self.diretorio_dados = self.diretorio_atual / '..' / 'dados'
        
        self.diretorio_dados_brutos = self.diretorio_atual / '..' / 'dados' / 'dados-brutos'
        
        if self.diretorio_dados_brutos.is_dir():
            
            self.pastas_em_dados_brutos = os.listdir(self.diretorio_dados_brutos)
            
        self.codigos_municipais = self.diretorio_dados / 'codigos_municipais.csv'
        
        self.diretorio_dados_tratados  = self.diretorio_atual / '..' / 'dados' / 'dados-tratados'
        self.diretorio_dados_tratados.mkdir(parents=True, exist_ok=True)
        
    def executar_processo_de_tratamento(self):
        self.arquivos_SIDRA()
        self.arquivos_SAGICAD()

    def remover_acentos(self, texto:str) -> str:
        
        nfkd_form = unicodedata.normalize('NFKD', texto)
        
        return "".join([c for c in nfkd_form if not unicodedata.combining(c)])
    
    def definir_unidade_SAGICAD(self, indicador: str) -> str:
        unidades_previstas = [
            'percentual',
            'numero de pessoas',
            'valor total',
            'valor medio',
            'responsaveis familiares',
            'familias beneficiarias',
            'pessoas beneficiarias',
            'quantidade total',
            'total de familias',
            'quantidade de familias',
            'quantidade de pessoas'
        ]
        
        indicador_para_comparar = indicador.replace('_', ' ')
        
        unidade = ''
        
        for prevista in unidades_previstas:
            
            if prevista in indicador_para_comparar:
                
                prevista = prevista.replace(' ', '_')
                
                unidade = prevista
                
                break
            
        return unidade

    def salvar_arquivo_tratado_SAGICAD(self, indicador: str, fonte: str, diretorio_salvamento: str, dataframe: DataFrame) -> None:
        
        nome_arquivo = f'{diretorio_salvamento}/{str(indicador)}_{str(fonte)}_municipal_ano_long.json'
        
        dataframe.to_json(nome_arquivo, orient='records', force_ascii=False, indent=4, double_precision=3)

    def organizar_dataframe_SAGICAD(self, fonte: str, indicador: str, dataframe: DataFrame) -> DataFrame:
        
        novo_df = dataframe.copy()
        
        unidade = self.definir_unidade_SAGICAD(indicador)
        
        novo_df['unidade'] = unidade
        
        novo_df['fonte'] = fonte
        
        novo_df['indicador'] = indicador
        
        formato_df = [
            'cod_municipio',
            'referencia',
            'fonte',
            'indicador',
            'valor',
            'unidade'
        ]
        
        novo_df = novo_df[formato_df]
        
        novo_df['referencia'] = pd.to_datetime(novo_df['referencia']).dt.to_period('M')
        
        colunas = ['cod_municipio']
        
        outras_colunas = [coluna for coluna in novo_df.columns if coluna != 'cod_municipio']
        
        nova_ordem = colunas + outras_colunas
        
        novo_df = novo_df[nova_ordem]
        
        novo_df = novo_df.sort_values(by=['referencia', 'cod_municipio'], ascending=True)
        
        novo_df = novo_df.reset_index(drop=True)
        
        return novo_df
    
    def tratamento_valores_nulos_SAGICAD(self, dataframe: DataFrame) -> DataFrame:
        colunas_com_valores_nulos = dataframe.isnull().any()
        df_apenas_com_colunas_nulas = dataframe.loc[:,colunas_com_valores_nulos]
        colunas_nulas = df_apenas_com_colunas_nulas.columns
        lista_da_coluna_com_valores_nulos = dataframe[colunas_nulas].values
        quantidade_de_valores_nulos = dataframe[colunas_nulas].count()
        contagem_valores_nulos = 0
        if len(colunas_nulas) > 0:
            for valor in lista_da_coluna_com_valores_nulos:
                if str(valor[0]).endswith('.0'):
                    contagem_valores_nulos += 1
                    if contagem_valores_nulos == quantidade_de_valores_nulos[0]:
                        dataframe[colunas_nulas] = dataframe[colunas_nulas].fillna(0, axis = 1)
                        dataframe[colunas_nulas] = dataframe[colunas_nulas].astype(int64)
                        
        # Resolver problema dos valores no formato float e int 64 
        dataframe['valor'] = dataframe['valor'].astype(str).str.replace(',', '.')
        #dataframe['valor'] = dataframe['valor'].astype('float64')
        dataframe['referencia'] = dataframe['referencia'].astype(str)
        return dataframe
    
    def arquivos_SIDRA(self) -> None: 
        for pasta in self.pastas_em_dados_brutos:
            
            caminho_arquivos = self.diretorio_dados_brutos / pasta
            
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:
                
                if str(arquivo).endswith('SIDRA.csv'):
                    
                    try:
                        caminho_arquivo = caminho_arquivos / arquivo
                        
                        df_header = pd.read_csv(caminho_arquivo, sep=';', header=None, nrows=2)
                        
                        unidade = df_header.iloc[1,0]
                        
                        inicio_chave = unidade.find('(')
                        
                        unidade = unidade[inicio_chave + 1: -1]
                        
                        df_x = pd.read_csv(caminho_arquivo, sep=';', header=3)
                        
                        anos = [str(ano) for ano in range(2012, 2027)]
                        
                        referencia = [ano for ano in df_x.columns if ano in anos][0]
                        
                        unidade = unidade.replace(" ",'_')
                        
                        unidade = self.remover_acentos(unidade)
                        
                        linha_inicio_codigo = df_x['Cód.'].loc[df_x['Cód.'] == '2100055'].index[0]
                        
                        linha_ultimo_codigo_municipal = df_x['Cód.'].loc[df_x['Cód.'] == '2114007'].index[0]
                        
                        df_x = df_x.iloc[linha_inicio_codigo:linha_ultimo_codigo_municipal + 1,:]
                        
                        caminho_normal = arquivo
                        
                        palavra_csv = caminho_normal.find('.csv')
                        
                        caminho_normal = caminho_normal[:palavra_csv]
                        
                        ultimo_unduscore = caminho_normal.rfind('_')
                        
                        caminho_normal = caminho_normal[ultimo_unduscore + 1:]
                        
                        fonte = caminho_normal
                        
                        x = arquivo
                        
                        ultima_barra = x.rfind('/')
                        
                        x = x[ultima_barra + 1:]
                        
                        ultima_unduscore = x.rfind('_')
                        
                        x = x[:ultima_unduscore]
                        
                        indicador = x
                        
                        df_x.rename(columns={'Cód.':'cod_municipio'}, inplace = True)
                        
                        df_x['referencia'] = referencia
                        
                        df_x['fonte'] = fonte
                        
                        df_x['indicador'] = indicador
                        
                        if unidade.lower() == 'habitante_por_quilometro_quadrado':
                            
                            unidade = 'densidade_demografica'
                            
                        df_x['unidade'] = unidade.lower()
                        
                        df_x.drop(columns={df_x.iloc[:,1].name}, inplace = True)
                        
                        df_x.rename(columns={str(referencia):'valor'}, inplace = True)
                        
                        df_x.iloc[1:218]
                        
                        unidades_inteiras = ['pessoas','anos']
                        
                        formato_df = [
                            'cod_municipio',
                            'referencia',
                            'fonte',
                            'indicador',
                            'valor',
                            'unidade'
                        ]
                        
                        df_x = df_x[formato_df]
                        
                        df_x = df_x.iloc[:linha_ultimo_codigo_municipal,:]

                        if unidade.lower() in unidades_inteiras:
                            
                            df_x['valor'] = df_x['valor'].astype(int)
                            
                        else:
                            
                            df_x['valor'] = df_x['valor'].astype(str).str.replace(',','.').astype(float).round(3)

                        nome_arquivo = f'{str(indicador)}_{str(fonte)}_{str(referencia)}_long.json'
                        
                        nova_pasta_dados_tratados = self.diretorio_dados_tratados / pasta
                        
                        nova_pasta_dados_tratados.mkdir(exist_ok=True)     
                                                 
                        diretorio_salvamento = nova_pasta_dados_tratados / nome_arquivo
                        
                        df_x.to_json(diretorio_salvamento, orient='records', force_ascii=False, indent=4, double_precision=3)
                                                      
                    except Exception as e:
                        pass
    
    def arquivos_SAGICAD(self) -> None:
        
        for pasta in self.pastas_em_dados_brutos:
            
            caminho_arquivos = self.diretorio_dados_brutos / pasta
            
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:   
                
                if arquivo.endswith('SAGICAD.csv'):

                    caminho_arquivo = caminho_arquivos / arquivo

                    df = pd.read_csv(caminho_arquivo, sep=',', encoding='latin-1')
                    
                    fonte = arquivo

                    fonte = arquivo[arquivo.rfind('_') + 1:arquivo.find('.')]

                    referencia_codigos_municipais = pd.read_csv(self.codigos_municipais)
                    
                    referencia_codigos_municipais['cod_municipal_7_digitos'] = referencia_codigos_municipais['codigo_municipal'].astype(str).str.zfill(7)
                    
                    referencia_codigos_municipais['cod_municipal_6_digitos'] = referencia_codigos_municipais['cod_municipal_7_digitos'].str[:6]
                    
                    df['Código'] = df['Código'].astype(str).str.zfill(6)
                    
                    df = df.merge(referencia_codigos_municipais[['cod_municipal_6_digitos','cod_municipal_7_digitos']], left_on='Código', right_on='cod_municipal_6_digitos', how="left")
                    
                    df['Código'] = df['cod_municipal_7_digitos']
                    
                    df = df.drop(columns=['cod_municipal_6_digitos','cod_municipal_7_digitos'])
                    
                    colunas = df.columns
                    
                    colunas_formatadas = [self.remover_acentos(coluna.replace(' ','_').lower()) for coluna in colunas]
                    
                    colunas_formatadas = [self.remover_acentos(coluna.replace('/','_').lower()) for coluna in colunas_formatadas]
                    
                    renomear_colunas = dict(zip(colunas,colunas_formatadas))
                    
                    df = df.rename(columns=renomear_colunas)
                    
                    localizacao_coluna_referencia = colunas_formatadas.index('referencia')

                    objetivo = df.iloc[:,localizacao_coluna_referencia:]
                    
                    colunas_apos_referencias = df.iloc[:,localizacao_coluna_referencia+1:].columns
                    
                    quantidade_colunas_apos_referencias = len(colunas_apos_referencias)

                    nova_pasta_dados_tratados = self.diretorio_dados_tratados / pasta
                        
                    nova_pasta_dados_tratados.mkdir(exist_ok=True)     
                                                 
                    diretorio_salvamento = nova_pasta_dados_tratados
                    
                    if quantidade_colunas_apos_referencias > 1:
                        
                        for coluna in colunas_apos_referencias:
                            
                            localizacao_coluna_referencia = colunas_formatadas.index('referencia')
                                       
                            coluna_alvo = df[coluna]
                            
                            novo_df = df.drop(columns=colunas_apos_referencias, axis = 1)
                            
                            novo_df[coluna] = coluna_alvo
                            
                            objetivo = novo_df.iloc[:,localizacao_coluna_referencia:]

                            indicador = objetivo.columns[-1]

                            novo_df.rename(columns={indicador:'valor'}, inplace=True)
                            
                            novo_df.rename(columns={'codigo':'cod_municipio'}, inplace = True)
                            
                            novo_df = self.organizar_dataframe_SAGICAD(fonte, indicador, novo_df)
                            
                            novo_df = self.tratamento_valores_nulos_SAGICAD(novo_df)
                            
                            self.salvar_arquivo_tratado_SAGICAD(indicador, fonte, diretorio_salvamento, novo_df)

                    else:
                        novo_df = df.copy()
                        
                        indicador = novo_df.columns[-1]
                        
                        novo_df.rename(columns={
                            'codigo' : 'cod_municipio',
                            novo_df.columns[-1] : 'valor'
                        }, inplace = True)

                        novo_df = self.organizar_dataframe_SAGICAD(fonte, indicador, novo_df)
                        
                        novo_df = self.tratamento_valores_nulos_SAGICAD(novo_df)
            
                        self.salvar_arquivo_tratado_SAGICAD(indicador, fonte, diretorio_salvamento, novo_df)
                        
#if __name__ == '__main__':
    #pass
    #ArquivodeTratamento().arquivos_SIDRA()
    
    #ArquivodeTratamento().arquivos_SAGICAD()
