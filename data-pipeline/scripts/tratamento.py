import pandas as pd
from pathlib import Path
import os
from numpy import int64

class ArquivodeTratamento():
    def __init__(self):
        self.diretorio_atual = Path(__file__).resolve().parent
        self.diretorio_dados_brutos = self.diretorio_atual / '..' / 'dados' / 'dados-brutos'
        if self.diretorio_dados_brutos.is_dir():
            self.pastas_em_dados_brutos = os.listdir(self.diretorio_dados_brutos)
        self.diretorio_dados_tratados  = self.diretorio_atual / '..' / 'dados' / 'dados-tratados'
    def tratamento_arquivos_csv_SIDRA(self): 
        for pasta in self.pastas_em_dados_brutos:
            caminho_arquivos = self.diretorio_dados_brutos / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            
            for arquivo in arquivos_na_pasta:
                if str(arquivo).endswith('SIDRA.csv'):
                    
                    try:
                        caminho_arquivo = caminho_arquivos / arquivo
                        df_header = pd.read_csv(caminho_arquivo, sep=';', header=None, nrows=2, engine='python')
                        nome_completo_variavel = df_header.iloc[1, 0]
                      
                        tipo_da_variavel = nome_completo_variavel.split()[-1]

                        df = pd.read_csv(caminho_arquivo, header = 2, sep=';',engine='python')
         
                        coluna_de_texto_principal = df.columns[0]
                        mascara_ibge = df[coluna_de_texto_principal].astype(str).str.contains("IBGE", na=False)
                        try:
                            indice_corte = mascara_ibge.idxmax()
                            if not mascara_ibge.any():
                                df_final = df.copy()
                            else:
                                df_final = df.iloc[:indice_corte].copy()
                        except Exception as e:
                            df_final = df.copy()

                        palavras_na_coluna = ['forma de declaração da idade','unidade da federação e município','município','brasil e município','grupo de idade']
                        for coluna in df.columns:
                            if coluna.lower() in palavras_na_coluna:
                                df_final = df_final.drop(coluna, axis = 1)
                                
                        df_final['Cód.'] = df_final['Cód.'].astype(str).str.strip()
                        mascara_municipio = (df_final['Cód.'].str.len() == 7)
 
                        try:
                            primeiro_rotulo_indice = mascara_municipio.idxmax()
                            posicao_corte = df_final.index.get_loc(primeiro_rotulo_indice)
                            if mascara_municipio.any():
                                df_final = df_final.iloc[posicao_corte:].copy()
                                df_final.reset_index(drop=True, inplace=True)
                            else:
                                pass
                        except Exception as e:
                            pass
                
                        colunas_convertidas = 0
                        for coluna in df_final.columns:
                            palavras_na_coluna = coluna.lower().split(sep=' ')
                            if 'ano' in palavras_na_coluna or 'idade' in palavras_na_coluna:
                            
                                df_final[coluna] = df_final[coluna].astype(str).str.replace(',', '.', regex=False)
                            
                                df_final[coluna] = pd.to_numeric(df_final[coluna], errors='coerce')
                            
                                df_final.dropna(subset=[coluna], inplace=True)
                            
                                if tipo_da_variavel == '(Razão)':
                                    target_dtype = 'Float64'
                                else:
                                    target_dtype = 'Int64'
                                
                                try:
                                    df_final[coluna] = df_final[coluna].astype(target_dtype)
                                
                                    colunas_convertidas += 1
                                except Exception as e:
                                    pass
                                
                                nome_final_target = arquivo.replace('.csv','')
                                df_final = df_final.rename(columns={df_final.columns[-1] : arquivo.replace('.csv','')})
                    
                    
                                possiveis_tipos = ['(Razão)','quadrados)','quadrado)','Reais)']
                             
                                if tipo_da_variavel in possiveis_tipos or tipo_da_variavel in possiveis_tipos or tipo_da_variavel in possiveis_tipos:
                                    df_final[nome_final_target] = df_final[nome_final_target].astype(str).str.replace('.', ',', regex=False)
                                    
                                nome_arquivo_formatado_e_limpo = arquivo.replace('.csv','_limpo.json')
                                
                                nova_pasta_dados_tratados = self.diretorio_dados_tratados / pasta
                                nova_pasta_dados_tratados.mkdir(exist_ok=True)
                                
                                diretorio_salvamento = nova_pasta_dados_tratados / nome_arquivo_formatado_e_limpo
                                
                                df_final.to_json(diretorio_salvamento,
                                                 orient='records', 
                                                force_ascii=False, 
                                                indent=4)
                    except Exception as e:
                        pass
    
    def tratamento_arquivos_csv_SAGICAD(self):
        for pasta in self.pastas_em_dados_brutos:
            caminho_arquivos = self.diretorio_dados_brutos / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            for arquivo in arquivos_na_pasta:   
                if arquivo.endswith('SAGICAD.csv'):
                    caminho_arquivo = caminho_arquivos / arquivo
                    df = pd.read_csv(caminho_arquivo, sep=',', encoding='latin1')
                    coluna_codigo_municipal = df[df.columns[0]]
                    colunas_dataframe = df.columns
                    localizacao_coluna_referencia = colunas_dataframe.get_loc('Referência')
                    df = df.iloc[:, localizacao_coluna_referencia:]
                    colunas_com_valores_nulos = df.isnull().any()
                    df_apenas_com_colunas_nulas = df.loc[:,colunas_com_valores_nulos]
                    colunas_nulas = df_apenas_com_colunas_nulas.columns
                    lista_da_coluna_com_valores_nulos = df[colunas_nulas].values
                    quantidade_de_valores_nulos = df[colunas_nulas].count()
                    contagem_valores_nulos = 0
                    if len(colunas_nulas) > 0:
                        for valor in lista_da_coluna_com_valores_nulos:
                            if str(valor[0]).endswith('.0'):
                                contagem_valores_nulos += 1
                        if contagem_valores_nulos == quantidade_de_valores_nulos[0]:
                            df[colunas_nulas] = df[colunas_nulas].fillna(0, axis = 1)
                            df[colunas_nulas] = df[colunas_nulas].astype(int64)
                    df['Código Municipal'] = coluna_codigo_municipal
                    df['Referência'] = pd.to_datetime(df['Referência']).dt.to_period('M')
                    colunas = ['Código Municipal']
                    outras_colunas = [coluna for coluna in df.columns if coluna != 'Código Municipal']
                    nova_ordem = colunas + outras_colunas
                    df = df[nova_ordem]
                    df = df.sort_values(by=['Referência', 'Código Municipal'], ascending=True)
                    df = df.reset_index(drop=True)
                    
                    df['Referência'] = df['Referência'].astype(str)
                    
                    
                    nome_arquivo_formatado_e_limpo = arquivo.replace('.csv','_limpo.json')
                    nova_pasta_dados_tratados = self.diretorio_dados_tratados / pasta
                    
                    nova_pasta_dados_tratados.mkdir(exist_ok=True)
                                
                    diretorio_salvamento = nova_pasta_dados_tratados / nome_arquivo_formatado_e_limpo
                    print(diretorio_salvamento)
                    df.to_json(diretorio_salvamento,
                        orient='records', 
                        force_ascii=False, 
                        indent=4)
if __name__ == '__main__':
    ArquivodeTratamento().tratamento_arquivos_csv_SIDRA()
    ArquivodeTratamento().tratamento_arquivos_csv_SAGICAD()