import os # Biblioteca responsável por realizar a leitura das variáveis de ambiente
from dotenv import load_dotenv # Carregamento do arquivo de ambiente .env
from pandas import DataFrame # Importando o tipo DataFrame para complementação de documentação das funções
from sqlalchemy import create_engine, URL, Engine # Utilizando a biblioteca sqlalchemy para comunicação com o banco de dados
from funcoes_de_apoio import AuxiliaresTratamento # Utilização das funções auxiliares

# Instanciando a classe que contém as funções auxiliares
funcoes_auxiliares = AuxiliaresTratamento()

class ConexaoPostgres:
    """
    Classe responsável por reunir as funções de comunicação com o banco de dados PostgreSQL.
    """

    def __init__(self):
        """
        Funções que serão inicializadas em primeira instância.
        """
        # Carregamento do ambiente .env
        load_dotenv() 
        # Inicialização da conexão com o banco de dados
        self.CONEXAO_DB = self.estabelecer_conexao() 
        # Criação do schema dos dados gerais (informações estaduais)
        self.criar_schema_dados_gerais() 

    def estabelecer_conexao(self) -> Engine:
        """
        Função responsável por realizar a conexão com o banco de dados.

        É realizado o carregamento das variáveis de ambiente para realizar a validação e estabelecer a conexão.
        
        :return: Objeto de engine do SQLAlchemy para conexão com o banco.
        :rtype: Engine
        """
        # Tentativa de conexão com o banco de dados.
        try:
            # URL de conexão do banco de dados, utilizado na conexão com o docker.
            URL_DB = os.getenv('DATABASE_URL')

            # Conexão local, utilizada para testes de salvamento.
            #USUARIO_DB = os.getenv('DB_USER') 
            #HOST_DB = os.getenv('DB_HOST')
            #SENHA_DB = os.getenv('DB_PASSWORD')
            #NOME_DB = os.getenv('DB_NAME')
            #url = URL.create(
            #    "postgresql+psycopg2", username=USUARIO_DB, password=SENHA_DB, host=HOST_DB, database=NOME_DB
            #)
            # Retorna-se o objeto de conexão do banco de dados.
            return create_engine(URL_DB)
        # Implementar tratamento para caso o database não exista
        except Exception as e:
            print(f"Erro ao estabelecer conexão: {e}")
       

    def inserir_dados(self, indicador:str, topico:str, dataframe: DataFrame) -> None:
        """
        Função responsável por inserir os dados tratados, separando por schemas.
        
        É realizado o salvamento dos dados diretamente no banco, separando em tabelas por schemas (tópicos),
        o nome da tabela é determinado pelo indicador dos dados. Exemplo: taxa_de_mortalidade.

        Args:
            indicador (str): indicador dos dados, exemplo: quantidade_de_homens_por_municipio.
            topico (str): tipo dos dados, exemplo: dados de saúde, dados demográficos...
            dataframe (DataFrame): tabela (dataframe) que contém os dados.
        """
        # Correção do nome do indicador, para que tenha a quantidade de caracteres correta para salvamento.
        indicador = funcoes_auxiliares.ajustar_tamanho_caracteres_indicador(indicador)

        # Tenta realizar o salvamento dos dados no banco.
        try:
            dataframe.to_sql(
                name = indicador, # Nome da tabela
                schema = topico, # Qual schema vai ficar essa tabela
                if_exists = 'replace', # Caso já exista essa tabela, é realizada a sobreposição dos dados
                chunksize=10000, # Tamanho máximo de dados
                con = self.CONEXAO_DB, # Conexão com o banco de dados
                index=False, # Retirada do index
                method='multi' # Salvamento em grandes quantidades de dados 
            )
            # Função responsável por anexar a chave estrangeira na coluna do código municipal
            # Essa implementação facilita a consulta dos dados no banco.
            self.anexar_chave_estrangeira(indicador, topico) 
        # Caso ocorra algum erro, é exibido na tela.
        except Exception as e:
            print(f"Erro ao inserir dados: {e}")
    
    def criar_schema_dados_gerais(self) -> None:
        """
        Função responsável por criar o schema dos dados gerais (informações estaduais)
        """

        try:
            dados_tabela = funcoes_auxiliares.leitura_arquivo_informacoes_municipios()
            with self.CONEXAO_DB.connect() as conexao:
                conexao_raw = conexao.connection
                with conexao_raw.cursor() as cursor:
                    cursor.execute("""
                        CREATE SCHEMA IF NOT EXISTS dados_gerais
                    """)
                    conexao_raw.commit()
                    cursor.execute("""
                        CREATE TABLE IF NOT EXISTS dados_gerais.informacoes (
                                   codigo_ibge VARCHAR(7) PRIMARY KEY,
                                   nome_municipio VARCHAR(100) NOT NULL 
                                   )
                    """)
                    conexao_raw.commit()
                    for item_atual in range(0, len(dados_tabela['codigos_municipais'])): 
                        cursor.execute("""
                            INSERT INTO dados_gerais.informacoes (
                            codigo_ibge, nome_municipio
                            ) VALUES (%s, %s) ON CONFLICT (codigo_ibge) DO NOTHING
                        """, (
                        dados_tabela['codigos_municipais'][item_atual],
                        dados_tabela['nomes_municipios'][item_atual]
                        ))
                    conexao_raw.commit()

        except Exception as e:
            print(f"Erro ao criar schema de dados gerais: {e}")

    def inserir_dados_schema_dados_gerais(self, indicador:str, dataframe: DataFrame) -> None:
        try:
            dataframe.to_sql(
                name = indicador,
                schema = 'dados_gerais',
                if_exists = 'replace',
                chunksize = 10000,
                con = self.CONEXAO_DB,
                index = False,
                method = 'multi'
            )
        except Exception as e:
            print(f"Erro ao inserir dados no schema geral: {e}")

    def anexar_chave_estrangeira(self, nome_tabela: str, topico: str):
    
        nome_constraint = f"fk_{nome_tabela}_municipio"

        with self.CONEXAO_DB.connect() as conexao:
            conexao_raw = conexao.connection
            with conexao_raw.cursor() as cursor:
            
                cursor.execute(f"ALTER TABLE {topico}.{nome_tabela} ALTER COLUMN cod_municipio TYPE VARCHAR(7);")
            
            
                comando = f"""
            ALTER TABLE {topico}.{nome_tabela}
            ADD CONSTRAINT {nome_constraint}
            FOREIGN KEY (cod_municipio)
            REFERENCES dados_gerais.informacoes (codigo_ibge);
            """
                cursor.execute(comando)
                conexao_raw.commit()

    def verificar_existencia_schema(self, topico:str):
        
        try:
            with self.CONEXAO_DB.connect() as conexao:
                conexao_raw = conexao.connection
                with conexao_raw.cursor() as cursor:
                    comando = f"""
                    CREATE SCHEMA IF NOT EXISTS {topico}
                    """
                    cursor.execute(comando)
                    conexao_raw.commit()
        except Exception as e:
            print(f"Erro ao verificar schema: {e}")