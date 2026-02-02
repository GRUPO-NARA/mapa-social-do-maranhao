import pandas as pd
from pathlib import Path
import os
from funcoes_de_apoio import AuxiliaresTratamento
from operacoes_db import ConexaoPostgres
import time

# Instanciação das dependências para uso na classe principal
funcoes_auxiliares = AuxiliaresTratamento()
database = ConexaoPostgres()

class TratamentoDadosMunicipais:
    """
    Classe orquestradora responsável por ler, tratar e carregar dados de múltiplas fontes
    (SIDRA, SAGICAD e QEDU) para o banco de dados PostgreSQL.
    """
    
    def __init__(self):
        """
        Inicializa a classe mapeando os diretórios de dados coletados e identificando
        os tópicos (pastas) disponíveis para tratamento.
        
        Raises:
            Exception: Se o diretório de dados coletados não for encontrado.
        """
        self.diretorio_atual = Path(__file__).resolve().parent
        self.diretorio_dados_coletados = self.diretorio_atual / '..' / 'dados_coletados'
        
        if not os.path.exists(self.diretorio_dados_coletados):
            raise Exception("Não foi encontrado o diretório dos dados coletados. É preciso realizar o processo de coleta das informações!")

        self.pastas_informacoes_municipais = self.diretorio_dados_coletados / 'informacoes_municipais'
        # Lista as subpastas que representam os temas/tópicos dos dados
        self.topicos = os.listdir(self.pastas_informacoes_municipais)
        
    def executar_processo_de_tratamento(self):
        """
        Executa sequencialmente o tratamento para todos os tipos de arquivos suportados.
        """
        self.arquivos_SIDRA()
        self.arquivos_SAGICAD()
        self.arquivos_ideb_dados_gerais_QEDU()
    
    def arquivos_SIDRA(self) -> None: 
        """
        Processa arquivos CSV originados do SIDRA (IBGE).
        
        Realiza a leitura de metadados no cabeçalho, extrai a unidade de medida entre parênteses,
        filtra exclusivamente municípios do Maranhão e padroniza a estrutura para o banco de dados.
        """
        for pasta in self.topicos:
            caminho_arquivos = self.pastas_informacoes_municipais / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:
                if str(arquivo).endswith('SIDRA.csv'):
                    try:
                        caminho_arquivo = caminho_arquivos / arquivo
                        
                        # Extração da unidade de medida nas linhas iniciais
                        dataframe_header = pd.read_csv(caminho_arquivo, sep=';', header=None, nrows=2)
                        unidade = dataframe_header.iloc[1,0]
                        inicio_chave = unidade.find('(')
                        unidade = unidade[inicio_chave + 1: -1] 
                        
                        # Leitura dos dados pulando o cabeçalho descritivo
                        dataframe = pd.read_csv(caminho_arquivo, sep=';', header=3)
                        
                        # Identificação dinâmica da coluna de ano
                        anos = [str(ano) for ano in range(2012, 2027)]
                        referencia = [ano for ano in dataframe.columns if ano in anos][0]
                        
                        # Limpeza da string de unidade
                        unidade = unidade.replace(" ",'_')
                        unidade = funcoes_auxiliares.remover_acentos(unidade)
                        
                        # Filtro geográfico: Municípios do Maranhão (Cód. 2100055 a 2114007)
                        linha_inicio_codigo = dataframe['Cód.'].loc[dataframe['Cód.'] == '2100055'].index[0]
                        linha_ultimo_codigo_municipal = dataframe['Cód.'].loc[dataframe['Cód.'] == '2114007'].index[0]
                        dataframe = dataframe.iloc[linha_inicio_codigo:linha_ultimo_codigo_municipal + 1,:]
                        
                        # Parsing do nome do arquivo para extrair fonte e indicador
                        caminho_normal = arquivo
                        palavra_csv = caminho_normal.find('.csv')
                        caminho_normal = caminho_normal[:palavra_csv]
                        fonte = caminho_normal[caminho_normal.rfind('_') + 1:]
                        indicador = arquivo[:arquivo.rfind('_')]
                        
                        # Padronização de colunas
                        dataframe.rename(columns={'Cód.':'cod_municipio'}, inplace = True)
                        dataframe['referencia'] = referencia
                        dataframe['fonte'] = fonte
                        dataframe['indicador'] = indicador
                        
                        if unidade.lower() == 'habitante_por_quilometro_quadrado':
                            unidade = 'densidade_demografica'
                        dataframe['unidade'] = unidade.lower()
                        
                        # Ajuste de valores: Remove nomes e renomeia coluna de valor
                        dataframe.drop(columns={dataframe.iloc[:,1].name}, inplace = True)
                        dataframe.rename(columns={str(referencia):'valor'}, inplace = True)
                        
                        # Tipagem numérica
                        unidades_inteiras = ['pessoas','anos']
                        formato_dataframe = ['cod_municipio', 'referencia', 'fonte', 'indicador', 'valor', 'unidade']
                        dataframe = dataframe[formato_dataframe]

                        if unidade.lower() in unidades_inteiras:
                            dataframe['valor'] = dataframe['valor'].astype(int)
                        else:
                            dataframe['valor'] = dataframe['valor'].astype(str).str.replace(',','.').astype(float).round(3)
                        
                        # Persistência no banco
                        nome_topico = pasta
                        database.verificar_existencia_schema(nome_topico)
                        database.inserir_dados(indicador, nome_topico, dataframe)
                       
                    except Exception:
                        pass
    
    def arquivos_SAGICAD(self) -> None:
        """
        Processa arquivos originados do SAGICAD.
        
        Realiza a correção de códigos IBGE para 7 dígitos, normaliza nomes de colunas
        e lida com arquivos que possuem múltiplos indicadores em colunas separadas,
        desmembrando-os para o formato 'long' (uma linha por indicador).
        """
        for pasta in self.topicos:
            caminho_arquivos = self.pastas_informacoes_municipais / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:   
                if arquivo.endswith('SAGICAD.csv'):
                    caminho_arquivo = caminho_arquivos / arquivo
                    dataframe = pd.read_csv(caminho_arquivo, sep=',', encoding='latin-1')
                    
                    fonte = arquivo[arquivo.rfind('_') + 1:arquivo.find('.')]
                    dataframe = funcoes_auxiliares.corrigir_codigo_ibge(dataframe)
                    
                    # Normalização de cabeçalhos
                    colunas_formatadas = [funcoes_auxiliares.remover_acentos(col.replace(' ','_').replace('/','_').lower()) for col in dataframe.columns]
                    dataframe.columns = colunas_formatadas
                    
                    # Identificação de colunas de indicadores (após a coluna de referência)
                    localizacao_ref = colunas_formatadas.index('referencia')
                    colunas_indicadores = dataframe.iloc[:, localizacao_ref + 1:].columns
                    
                    topico = pasta
                    # Lógica para arquivos Multi-Indicador
                    if len(colunas_indicadores) > 1:
                        for coluna in colunas_indicadores:
                            dataframe_atualizado = dataframe.drop(columns=colunas_indicadores, axis = 1)
                            dataframe_atualizado[coluna] = dataframe[coluna]
                            
                            indicador = coluna
                            dataframe_atualizado.rename(columns={indicador: 'valor', 'codigo': 'cod_municipio'}, inplace=True)
                            
                            # Tratamento e salvamento via auxiliares
                            dataframe_atualizado = funcoes_auxiliares.organizar_dataframe_SAGICAD(fonte, indicador, dataframe_atualizado)
                            dataframe_atualizado = funcoes_auxiliares.tratamento_valores_nulos_SAGICAD(dataframe_atualizado)
                            database.verificar_existencia_schema(topico)
                            database.inserir_dados(indicador, topico, dataframe_atualizado)   

                    else:
                        # Lógica para arquivos de indicador único
                        indicador = colunas_indicadores[0]
                        dataframe.rename(columns={'codigo': 'cod_municipio', indicador: 'valor'}, inplace=True)
                        
                        dataframe = funcoes_auxiliares.organizar_dataframe_SAGICAD(fonte, indicador, dataframe)
                        dataframe = funcoes_auxiliares.tratamento_valores_nulos_SAGICAD(dataframe)
                        database.verificar_existencia_schema(topico)
                        database.inserir_dados(indicador, topico, dataframe)   
                    
    def arquivos_ideb_dados_gerais_QEDU(self) -> None: 
        """
        Processa arquivos de educação (IDEB) provenientes da plataforma QEDU (.xlsx).
        
        Consolida dados de abas diferentes (estado e municípios), mapeia IDs de dependência 
        administrativa e ciclos de ensino para nomes amigáveis, e separa dados a nível 
        estadual (ID 21) de dados municipais.
        """
        dataframe_completo = []
        for pasta in self.topicos:
            caminho_arquivos = self.pastas_informacoes_municipais / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:   
                if arquivo.startswith("IDEB") and arquivo.endswith('QEDU.xlsx'):
                    caminho_arquivo = caminho_arquivos / arquivo
                    leitura_dataframe_tabela_estado = pd.read_excel(caminho_arquivo, sheet_name='estado')
                    leitura_dataframe_tabela_municipios = pd.read_excel(caminho_arquivo, sheet_name='municipios')
                    dataframe_completo.append(leitura_dataframe_tabela_estado)
                    dataframe_completo.append(leitura_dataframe_tabela_municipios)
        
        if dataframe_completo:
            df = pd.concat(dataframe_completo, ignore_index=True)
            # Limpeza de colunas auxiliares do IDEB
            df.drop(columns=['fluxo','aprendizado','nota_mt','nota_lp'], inplace = True)
            
            # Mapeamento de categorias administrativas e ciclos escolares
            map_dependencia = {0: 'total', 1: 'federal', 2: 'estadual', 3: 'municipal', 4: 'privada', 5: 'publica'}
            map_ciclo = {'AI': 'anos_iniciais', 'AF': 'anos_finais', 'EM': 'ensino_medio'}
            
            df['ciclo'] = df['ciclo_id'].map(map_ciclo)
            df['dependencia_adm'] = df['dependencia_id'].map(map_dependencia)
            df.drop(columns=['dependencia_id','ciclo_id'], inplace = True)
            df['unidade'] = 'decimal'
            df.rename(columns={'ibge_id' : 'cod_municipio'}, inplace=True)
            
            # Separação e inserção: Dados Estaduais (Maranhão)
            dataframe_dados_estadual = df[df['cod_municipio'] == 21]
            indicador = 'ideb_geral'
            database.inserir_dados_schema_dados_gerais(indicador, dataframe_dados_estadual)

            # Separação e inserção: Dados Municipais
            dataframe_dados_municipais = df[df['cod_municipio'] != 21]
            dataframe_dados_municipais.fillna(-1, inplace=True)
            database.verificar_existencia_schema('dados_educacao')
            database.inserir_dados('ideb_municipais','dados_educacao',dataframe_dados_municipais)
        else:
            print("Nenhum dado encontrado")