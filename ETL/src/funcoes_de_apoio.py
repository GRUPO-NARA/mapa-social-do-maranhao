# Bibliotecas necessárias para o funcionamento correto das funções
from pathlib import Path
from typing import Dict, List, Union
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
        Define os caminhos base para os arquivos de referência de municípios.
        """
        self.DIRETORIO_ARQUIVO = Path(__file__).resolve().parent
        self.arquivo_informacoes_municipios = self.DIRETORIO_ARQUIVO / '..' / 'dados_coletados' / 'informacoes_estaduais' / 'informacoes_municipios.csv'

    def leitura_arquivo_informacoes_municipios(self) -> dict[str, List[str]]:
        """
        Lê o arquivo CSV de informações municipais e extrai listas de códigos e nomes.

        :return: Dicionário contendo as chaves 'cod_municipais' e 'nomes_municipios'.
        :rtype: dict
        """
        dataframe = pd.read_csv(self.arquivo_informacoes_municipios, sep=',')
        return {
            'codigos_municipais' : dataframe['cod_municipio'].to_list(),
            'nomes_municipios' : dataframe['nome_municipio'].to_list()
        }

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

    def corrigir_codigo_ibge(self, dataframe_original: DataFrame) -> DataFrame:
        """
        Ajusta códigos de município de 6 dígitos para o padrão de 7 dígitos usando uma base de referência.

        :param dataframe_original: DataFrame contendo a coluna 'Código'.
        :return: DataFrame com os códigos de município corrigidos e atualizados.
        :rtype: DataFrame
        """
        referencia_codigos_municipais = pd.read_csv(self.arquivo_informacoes_municipios, sep=',')
        referencia_codigos_municipais.drop(columns=['nome_municipio'], inplace=True)
        
        # Cria colunas auxiliares de 6 e 7 dígitos para realizar o cruzamento (merge)
        referencia_codigos_municipais['cod_municipal_7_digitos'] = referencia_codigos_municipais['cod_municipio'].astype(str).str.zfill(7)
        referencia_codigos_municipais['cod_municipal_6_digitos'] = referencia_codigos_municipais['cod_municipal_7_digitos'].str[:6]

        df = dataframe_original          
        df['Código'] = df['Código'].astype(str).str.zfill(6)
        
        # Mescla os dados para recuperar o dígito verificador ausente
        df = df.merge(referencia_codigos_municipais[['cod_municipal_6_digitos','cod_municipal_7_digitos']], left_on='Código', right_on='cod_municipal_6_digitos', how="left")
        df['Código'] = df['cod_municipal_7_digitos']
        df = df.drop(columns=['cod_municipal_6_digitos','cod_municipal_7_digitos'])
        return df 
    
    def ajustar_tamanho_caracteres_indicador(self, indicador: str):
        """
        Mapeia e corrige nomes de indicadores muito longos para versões mais enxutas e padronizadas.

        :param indicador: Nome original do indicador.
        :return: Nome do indicador corrigido ou o original caso não esteja no mapeamento.
        """
        possiveis_indicadores_e_correcao = {
            'responsaveis_familiares_do_sexo_feminino_beneficiarias_do_auxilio_gas' : 'responsaveis_familiares_feminino_beneficiarias_do_auxilio_gas',
            'quantidade_de_pessoas_sem_informacao_sobre_raca_cor_inscritas_no_cadastro_unico' : 'pessoas_sem_informacao_sobre_raca_cor_cadastro_unico',
            'quantidade_de_familias_do_cadastro_unico_em_situacao_de_trabalho_infantil' : 'familias_do_cadastro_unico_em_situacao_de_trabalho_infantil',
            'quantidade_de_pessoas_do_cadastro_unico_em_situacao_de_trabalho_infantil' : 'pessoas_do_cadastro_unico_em_situacao_de_trabalho_infantil',
            'quantidade_de_pessoas_do_sexo_masculino_inscritas_no_cadastro_unico' : "pessoas_do_sexo_masculino_inscritas_no_cadastro_unico",
            'quantidade_de_pessoas_do_sexo_feminino_inscritas_no_cadastro_unico' : "pessoas_do_sexo_feminino_inscritas_no_cadastro_unico",
            'total_de_familias_em_situacao_de_rua_inscritas_no_cadastro_unico' : "familias_em_situacao_de_rua_cadastro_unico"
        }

        if indicador in possiveis_indicadores_e_correcao.keys():
            indicador = possiveis_indicadores_e_correcao[indicador]

        return indicador