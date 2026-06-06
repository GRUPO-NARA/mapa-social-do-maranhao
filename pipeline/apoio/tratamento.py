from pandas import DataFrame
from pathlib import Path
import pandas as pd
from configuracoes.config import MapeamentoDiretoriosEArquivos

class FuncoesDeApoioParaTratamento:
    def __init__(self):
        self.referencia_dos_codigos_municipais = MapeamentoDiretoriosEArquivos().arquivo_de_referencia_dos_codigos_municipais

    def corrigir_codigo_ibge(self, tabela_original: DataFrame) -> DataFrame:
        """Corrige os códigos IBGE dos municípios, garantindo que estejam no formato correto."""
        tabela_de_referencia = pd.read_csv(self.referencia_dos_codigos_municipais, sep=',')
        tabela_de_referencia.drop(columns=['nome_municipio'], inplace=True)

        tabela_de_referencia['cod_municipal_7_digitos'] = tabela_de_referencia['cod_municipio'].astype(str).str.zfill(7)
        tabela_de_referencia['cod_municipal_6_digitos'] = tabela_de_referencia['cod_municipal_7_digitos'].str[:6]

        tabela_original = tabela_original
        tabela_original['Código'] = tabela_original['Código'].astype(str).str.zfill(6)

        tabela_original = tabela_original.merge(tabela_de_referencia[['cod_municipal_6_digitos', 'cod_municipal_7_digitos']], left_on='Código', right_on='cod_municipal_6_digitos', how='left')
        tabela_original['Código'] = tabela_original['cod_municipal_7_digitos']
        tabela_original.drop(columns=['cod_municipal_6_digitos', 'cod_municipal_7_digitos'], inplace=True)
        tabela_modificada = tabela_original
        return tabela_modificada

    def remover_acentos(self, texto: str) -> str:
        """Remove acentos e caracteres especiais de um texto, deixando apenas letras, números e underscores."""
        import unicodedata
        texto_normalizado = unicodedata.normalize('NFKD', texto).encode('ASCII', 'ignore').decode('utf-8')
        texto_limpo = ''.join(char for char in texto_normalizado if char.isalnum() or char == '_')
        return texto_limpo
    

    def organizar_dataframe_SAGICAD(self, fonte: str, indicador: str, tabela: DataFrame) -> DataFrame:

        tabela = tabela.copy()
        tabela['fonte'] = fonte
        tabela['indicador'] = indicador

        formatador_de_colunas = ['cod_municipio', 'referencia', 'fonte', 'valor', 'indicador']

        tabela = tabela[formatador_de_colunas]

        tabela['referencia'] = pd.to_datetime(tabela['referencia'], format='%m/%Y',errors='coerce').dt.to_period('M')
        tabela['referencia'] = tabela['referencia'].astype(str)

        # Reorganiza as colunas garantindo que cod_municipio seja a primeira
        colunas = ['cod_municipio']
        outras_colunas = [coluna for coluna in tabela.columns if coluna != 'cod_municipio']
        nova_ordem = colunas + outras_colunas
        tabela = tabela[nova_ordem]
        
        # Ordenação lógica por data e depois por código de município
        tabela = tabela.sort_values(by=['referencia', 'cod_municipio'], ascending=True)
        
        # Reseta o índice para garantir integridade após a ordenação
        tabela = tabela.reset_index(drop=True)

        return tabela
    
    def tratamento_valores_nulos_SAGICAD(self, tabela: DataFrame) -> DataFrame:
        """Trata valores nulos na tabela do SAGICAD, substituindo por -1 e garantindo o tipo correto."""
        tabela = tabela.copy()
        
        tabela['valor'] = tabela['valor'].astype(str).str.replace(',', '.')
        
        # Trata valores nulos e 'nan' como -1 (valor sentinela)
        tabela['valor'] = tabela['valor'].replace('nan', '-1')
        tabela['valor'] = pd.to_numeric(tabela['valor'], errors='coerce').fillna(-1)
        
        # Tenta converter para inteiro; se falhar, mantém como float
        try:
            tabela['valor'] = tabela['valor'].astype('Int64')
        except (ValueError, TypeError):
            tabela['valor'] = tabela['valor'].astype('float64')
        
        return tabela
