import pandas as pd
from pathlib import Path
import os
from funcoes_auxiliares import AuxiliaresTratamento

# Instância da classe auxiliar para usar funções de limpeza e organização
classe_das_funcoes_auxiliares = AuxiliaresTratamento()

class ArquivodeTratamento:
    """
    Classe responsável por processar arquivos de dados brutos das fontes SIDRA e SAGICAD,
    convertendo-os para um formato padronizado (JSON) e salvando-os na pasta de dados tratados.
    """
    
    def __init__(self):
        """
        Configura os caminhos de diretórios e prepara as pastas de destino.
        """
        self.diretorio_atual = Path(__file__).resolve().parent
        
        # Define os caminhos das pastas de dados (brutos e tratados)
        self.diretorio_dados = self.diretorio_atual / '..' / 'dados'
        self.diretorio_dados_brutos = self.diretorio_atual / '..' / 'dados' / 'dados-brutos'
        
        # Lista as subpastas dentro de dados-brutos (ex: Saúde, Educação)
        if self.diretorio_dados_brutos.is_dir():
            self.pastas_em_dados_brutos = os.listdir(self.diretorio_dados_brutos)
            
        # Arquivo de referência para converter códigos de 6 para 7 dígitos
        self.codigos_municipais = self.diretorio_dados / 'codigos_municipais.csv'
        
        # Cria a pasta de dados tratados se ela não existir
        self.diretorio_dados_tratados  = self.diretorio_atual / '..' / 'dados' / 'dados-tratados'
        self.diretorio_dados_tratados.mkdir(parents=True, exist_ok=True)
        
    def executar_processo_de_tratamento(self):
        """
        Função principal que dispara o tratamento para cada tipo de fonte.
        """
        self.arquivos_SIDRA()
        self.arquivos_SAGICAD()

    def arquivos_SIDRA(self) -> None: 
        """
        Processa arquivos CSV vindos do SIDRA (IBGE).
        Lida com cabeçalhos complexos, extrai unidades de medida e filtra municípios.
        """
        for pasta in self.pastas_em_dados_brutos:
            caminho_arquivos = self.diretorio_dados_brutos / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:
                # Verifica se o arquivo é do SIDRA
                if str(arquivo).endswith('SIDRA.csv'):
                    try:
                        caminho_arquivo = caminho_arquivos / arquivo
                        
                        # Lê apenas as primeiras linhas para descobrir a unidade de medida
                        df_header = pd.read_csv(caminho_arquivo, sep=';', header=None, nrows=2)
                        unidade = df_header.iloc[1,0]
                        inicio_chave = unidade.find('(')
                        unidade = unidade[inicio_chave + 1: -1] # Extrai o texto dentro dos parênteses
                        
                        # Lê os dados reais começando da linha 4
                        df_x = pd.read_csv(caminho_arquivo, sep=';', header=3)
                        
                        # Identifica qual coluna é o ano de referência (entre 2012 e 2026)
                        anos = [str(ano) for ano in range(2012, 2027)]
                        referencia = [ano for ano in df_x.columns if ano in anos][0]
                        
                        # Limpa a string da unidade
                        unidade = unidade.replace(" ",'_')
                        unidade = classe_das_funcoes_auxiliares.remover_acentos(unidade)
                        
                        # Filtra apenas os municípios do Maranhão (pelos códigos de início e fim)
                        linha_inicio_codigo = df_x['Cód.'].loc[df_x['Cód.'] == '2100055'].index[0]
                        linha_ultimo_codigo_municipal = df_x['Cód.'].loc[df_x['Cód.'] == '2114007'].index[0]
                        df_x = df_x.iloc[linha_inicio_codigo:linha_ultimo_codigo_municipal + 1,:]
                        
                        # Extrai informações de Fonte e Indicador do nome do arquivo
                        caminho_normal = arquivo
                        palavra_csv = caminho_normal.find('.csv')
                        caminho_normal = caminho_normal[:palavra_csv]
                        fonte = caminho_normal[caminho_normal.rfind('_') + 1:]
                        indicador = arquivo[:arquivo.rfind('_')]
                        
                        # Padroniza as colunas do DataFrame
                        df_x.rename(columns={'Cód.':'cod_municipio'}, inplace = True)
                        df_x['referencia'] = referencia
                        df_x['fonte'] = fonte
                        df_x['indicador'] = indicador
                        
                        if unidade.lower() == 'habitante_por_quilometro_quadrado':
                            unidade = 'densidade_demografica'
                        df_x['unidade'] = unidade.lower()
                        
                        # Remove a coluna de nome do município e renomeia o ano para 'valor'
                        df_x.drop(columns={df_x.iloc[:,1].name}, inplace = True)
                        df_x.rename(columns={str(referencia):'valor'}, inplace = True)
                        
                        # Formata o tipo de dado da coluna valor
                        unidades_inteiras = ['pessoas','anos']
                        formato_df = ['cod_municipio', 'referencia', 'fonte', 'indicador', 'valor', 'unidade']
                        df_x = df_x[formato_df]

                        if unidade.lower() in unidades_inteiras:
                            df_x['valor'] = df_x['valor'].astype(int)
                        else:
                            df_x['valor'] = df_x['valor'].astype(str).str.replace(',','.').astype(float).round(3)

                        # Salva o resultado em JSON dentro da pasta da respectiva categoria
                        nome_arquivo = f'{str(indicador)}_{str(fonte)}_{str(referencia)}_long.json'
                        nova_pasta_dados_tratados = self.diretorio_dados_tratados / pasta
                        nova_pasta_dados_tratados.mkdir(exist_ok=True)     
                        df_x.to_json(nova_pasta_dados_tratados / nome_arquivo, orient='records', force_ascii=False, indent=4)
                                                      
                    except Exception:
                        pass
    
    def arquivos_SAGICAD(self) -> None:
        """
        Processa arquivos CSV vindos do SAGICAD.
        Faz o merge com códigos municipais e trata indicadores que podem vir em múltiplas colunas.
        """
        for pasta in self.pastas_em_dados_brutos:
            caminho_arquivos = self.diretorio_dados_brutos / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:   
                if arquivo.endswith('SAGICAD.csv'):
                    caminho_arquivo = caminho_arquivos / arquivo
                    df = pd.read_csv(caminho_arquivo, sep=',', encoding='latin-1')
                    
                    # Extrai a fonte do nome do arquivo
                    fonte = arquivo[arquivo.rfind('_') + 1:arquivo.find('.')]

                    # Ajusta códigos municipais (converte códigos de 6 para 7 dígitos)
                    referencia_codigos_municipais = pd.read_csv(self.codigos_municipais)
                    referencia_codigos_municipais['cod_municipal_7_digitos'] = referencia_codigos_municipais['codigo_municipal'].astype(str).str.zfill(7)
                    referencia_codigos_municipais['cod_municipal_6_digitos'] = referencia_codigos_municipais['cod_municipal_7_digitos'].str[:6]
                    
                    df['Código'] = df['Código'].astype(str).str.zfill(6)
                    df = df.merge(referencia_codigos_municipais[['cod_municipal_6_digitos','cod_municipal_7_digitos']], left_on='Código', right_on='cod_municipal_6_digitos', how="left")
                    df['Código'] = df['cod_municipal_7_digitos']
                    df = df.drop(columns=['cod_municipal_6_digitos','cod_municipal_7_digitos'])
                    
                    # Padroniza nomes das colunas (minúsculo, sem acento, com underscore)
                    colunas_formatadas = [classe_das_funcoes_auxiliares.remover_acentos(col.replace(' ','_').replace('/','_').lower()) for col in df.columns]
                    df.columns = colunas_formatadas
                    
                    # Identifica as colunas que vêm após a coluna de 'referencia' (indicadores)
                    localizacao_ref = colunas_formatadas.index('referencia')
                    colunas_indicadores = df.iloc[:, localizacao_ref + 1:].columns
                    
                    nova_pasta_dados_tratados = self.diretorio_dados_tratados / pasta
                    nova_pasta_dados_tratados.mkdir(exist_ok=True)     
                    
                    # Se houver mais de um indicador no mesmo arquivo, separa em arquivos diferentes
                    if len(colunas_indicadores) > 1:
                        for coluna in colunas_indicadores:
                            novo_df = df.drop(columns=colunas_indicadores, axis = 1)
                            novo_df[coluna] = df[coluna]
                            
                            indicador = coluna
                            novo_df.rename(columns={indicador: 'valor', 'codigo': 'cod_municipio'}, inplace=True)
                            
                            # Usa funções auxiliares para organizar e salvar
                            novo_df = classe_das_funcoes_auxiliares.organizar_dataframe_SAGICAD(fonte, indicador, novo_df)
                            novo_df = classe_das_funcoes_auxiliares.tratamento_valores_nulos_SAGICAD(novo_df)
                            classe_das_funcoes_auxiliares.salvar_arquivo_tratado_SAGICAD(indicador, fonte, nova_pasta_dados_tratados, novo_df)

                    else:
                        # Processo para arquivo com apenas um indicador
                        indicador = colunas_indicadores[0]
                        df.rename(columns={'codigo': 'cod_municipio', indicador: 'valor'}, inplace=True)
                        
                        df = classe_das_funcoes_auxiliares.organizar_dataframe_SAGICAD(fonte, indicador, df)
                        df = classe_das_funcoes_auxiliares.tratamento_valores_nulos_SAGICAD(df)
                        classe_das_funcoes_auxiliares.salvar_arquivo_tratado_SAGICAD(indicador, fonte, nova_pasta_dados_tratados, df)
                        
if __name__ == '__main__':
    # Inicia o processo completo
    ArquivodeTratamento().executar_processo_de_tratamento()