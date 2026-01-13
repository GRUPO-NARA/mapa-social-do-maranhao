# Bibliotecas necessárias para o funcionamento correto das funções
import unicodedata 
from numpy import float64, int64
from pandas import DataFrame
import pandas as pd

class AuxiliaresTratamento:
    """
    Classe responsável por reunir as funções mais utilizadas ou específicas que auxiliam no funcionamento
    das funções principais na classe de tratamento dos dados, com foco em dados do SAGICAD.
    """
    def __init__(self):
        """
        Inicializador da classe AuxiliaresTratamento.
        """
        pass

    def remover_acentos(self, texto: str) -> str:
        """
        Função responsável por remover acentuações e caracteres especiais de normalização de textos.
        
        :param texto: Texto original que contém acentuações.
        :return: Texto convertido para caracteres base (ex: 'á' vira 'a').
        :rtype: str
        """
        # Normaliza a string para decompor caracteres acentuados em caracteres base + acento
        nfkd_form = unicodedata.normalize('NFKD', texto)
        
        # Filtra a string mantendo apenas caracteres que não são marcas de combinação (acentos)
        return "".join([c for c in nfkd_form if not unicodedata.combining(c)])
    
    def definir_unidade_dados_SAGICAD(self, indicador: str) -> str:
        """
        Analisa o nome do indicador e identifica a unidade de medida correspondente 
        com base em palavras-chave pré-definidas.
        
        :param indicador: Nome do indicador técnico a ser analisado.
        :type indicador: str
        :return: A unidade identificada formatada com underscores ou string vazia se não encontrada.
        :rtype: str
        """
        # Lista de unidades conhecidas que podem estar contidas no nome do indicador
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
        
        # Substitui underscores por espaços para facilitar a comparação com a lista
        indicador_para_comparar = indicador.replace('_', ' ')
        
        unidade = ''
        
        # Varre as unidades previstas buscando correspondência no indicador
        for prevista in unidades_previstas:
            if prevista in indicador_para_comparar:
                # Se encontrar, formata a unidade com underscore para o padrão de saída
                prevista = prevista.replace(' ', '_')
                unidade = prevista
                break
            
        return unidade
    
    def salvar_arquivo_tratado_SAGICAD(self, indicador: str, fonte: str, diretorio_salvamento: str, dataframe: DataFrame) -> None:
        """
        Gera um arquivo JSON a partir do DataFrame tratado, utilizando uma nomenclatura padronizada.
        
        :param indicador: Nome do indicador para compor o nome do arquivo.
        :param fonte: Nome da fonte dos dados.
        :param diretorio_salvamento: Caminho da pasta onde o arquivo será gravado.
        :param dataframe: Objeto DataFrame contendo os dados a serem salvos.
        :return: None
        """
        # Define o nome do arquivo seguindo o padrão: indicador_fonte_municipal_ano_long.json
        nome_arquivo = f'{diretorio_salvamento}/{str(indicador)}_{str(fonte)}_municipal_ano_long.json'
        
        # Salva em JSON com suporte a caracteres especiais e indentação para leitura humana
        dataframe.to_json(nome_arquivo, orient='records', force_ascii=False, indent=4, double_precision=3)

    def organizar_dataframe_SAGICAD(self, fonte: str, indicador: str, dataframe: DataFrame) -> DataFrame:
        """
        Padroniza a estrutura do DataFrame, define colunas obrigatórias, 
        formata datas e ordena os registros.
        
        :param fonte: Nome da fonte de dados a ser inserida no DataFrame.
        :param indicador: Nome do indicador a ser inserido no DataFrame.
        :param dataframe: DataFrame original a ser estruturado.
        :return: DataFrame formatado e ordenado.
        :rtype: DataFrame
        """
        novo_df = dataframe.copy()
        
        # Atribui metadados baseados no indicador e parâmetros
        unidade = self.definir_unidade_dados_SAGICAD(indicador)
        novo_df['unidade'] = unidade
        novo_df['fonte'] = fonte
        novo_df['indicador'] = indicador
        
        # Define a ordem e seleção das colunas finais
        formato_df = [
            'cod_municipio',
            'referencia',
            'fonte',
            'indicador',
            'valor',
            'unidade'
        ]
        
        novo_df = novo_df[formato_df]
        
        # Converte a coluna de referência para o formato de período mensal (AAAA-MM)
        novo_df['referencia'] = pd.to_datetime(novo_df['referencia'], format='%m/%Y',errors='coerce').dt.to_period('M')
        novo_df['referencia'] = novo_df['referencia'].astype(str)

        # Reorganiza as colunas garantindo que cod_municipio seja a primeira
        colunas = ['cod_municipio']
        outras_colunas = [coluna for coluna in novo_df.columns if coluna != 'cod_municipio']
        nova_ordem = colunas + outras_colunas
        novo_df = novo_df[nova_ordem]
        
        # Ordenação lógica por data e depois por código de município
        novo_df = novo_df.sort_values(by=['referencia', 'cod_municipio'], ascending=True)
        
        # Reseta o índice para garantir integridade após a ordenação
        novo_df = novo_df.reset_index(drop=True)
        
        return novo_df
    
    def tratamento_valores_nulos_SAGICAD(self, dataframe: DataFrame) -> DataFrame:
        """
        Realiza a limpeza de valores nulos, conversão de tipos numéricos e 
        ajustes específicos para indicadores de contagem ou valor.
        
        :param dataframe: DataFrame contendo a coluna 'valor'.
        :return: DataFrame com a coluna 'valor' tipada corretamente.
        :rtype: DataFrame
        """
        # Padroniza separador decimal de vírgula para ponto
        dataframe['valor'] = dataframe['valor'].astype(str).str.replace(',','.')
       
        # Tratamento específico para o indicador de Trabalho Infantil
        if dataframe['indicador'].iloc[0] == 'quantidade_de_pessoas_do_cadastro_unico_em_situacao_de_trabalho_infantil':
            # Remove sufixos .0 desnecessários e trata nulos como -1 (valor sentinela)
            dataframe['valor'] = dataframe['valor'].astype(str).str.replace('.0', ' ')
            dataframe['valor'] = dataframe['valor'].astype(str).str.replace('nan', '-1')

        # Lista de unidades que devem ser tratadas obrigatoriamente como números inteiros
        unidades_previstas = [
            'numero_de_pessoas',
            'responsaveis_familiares',
            'familias_beneficiarias',
            'pessoas_beneficiarias',
            'quantidade_total',
            'total_de_familias',
            'quantidade_de_familias',
            'quantidade_de_pessoas'
        ]
        
        # Aplica tipagem inteira para contagens e ponto flutuante para valores monetários/percentuais
        if dataframe['unidade'].iloc[0] in unidades_previstas:
            dataframe['valor'] = dataframe['valor'].astype(int64)    
        else:
            dataframe['valor'] = dataframe['valor'].astype(float64)
        
        return dataframe