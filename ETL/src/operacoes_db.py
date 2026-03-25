import os 
from dotenv import load_dotenv 
from pandas import DataFrame 
from sqlalchemy import create_engine, URL, Engine 
from funcoes_de_apoio import AuxiliaresTratamento 

# Instanciando a classe que contém as funções auxiliares para uso global na classe de conexão
funcoes_auxiliares = AuxiliaresTratamento()

class ConexaoPostgres:
    """
    Classe responsável por gerenciar a comunicação, criação de estrutura e inserção de dados 
    no banco de dados PostgreSQL.
    """

    def __init__(self):
        """
        Inicializador da classe. Realiza o carregamento das variáveis de ambiente,
        estabelece a conexão inicial e garante a existência do schema de metadados estaduais.
        """
        load_dotenv() 
        self.CONEXAO_DB = self.estabelecer_conexao() 
       

    def estabelecer_conexao(self) -> Engine:
        """
        Cria o objeto de engine do SQLAlchemy utilizando as credenciais configuradas.

        :return: Objeto Engine para comunicação com o banco de dados.
        :rtype: sqlalchemy.engine.Engine
        """
        try:
            # Recupera a string de conexão configurada (geralmente para ambiente Docker)
            URL_DB = os.getenv('DATABASE_URL')
            return create_engine(URL_DB)
        except Exception as e:
            print(f"Erro ao estabelecer conexão: {e}")

    def inserir_dados(self, indicador: str, topico: str, dataframe: DataFrame) -> None:
        """
        Insere um DataFrame no banco de dados, organizando-o em tabelas por indicadores
        dentro de schemas definidos por tópicos.

        Args:
            indicador (str): Nome da tabela a ser criada/atualizada (ex: 'populacao_total').
            topico (str): Nome do schema onde a tabela será inserida (ex: 'demografia').
            dataframe (DataFrame): Conjunto de dados tratado para inserção.
        """
        # Ajusta o nome do indicador conforme regras de tamanho da classe auxiliar
        indicador = funcoes_auxiliares.ajustar_tamanho_caracteres_indicador(indicador)

        try:
            dataframe.to_sql(
                name = indicador,
                schema = topico,
                if_exists = 'replace', # Sobrescreve a tabela se já existir
                chunksize = 10000,     # Otimiza a inserção em lotes de 10k registros
                con = self.CONEXAO_DB,
                index = False,
                method = 'multi'       # Melhora performance de inserção múltipla
            )
            # Estabelece o relacionamento de integridade referencial com a tabela de municípios
            self.anexar_chave_estrangeira(indicador, topico) 
        except Exception as e:
            print(f"Erro ao inserir dados: {e}")
    
    def criar_schema_dados_gerais(self) -> None:
        """
        Garante a existência do schema 'dados_gerais' e da tabela 'informacoes'.
        Popula a tabela com dados básicos de municípios (código IBGE e Nome) 
        para servir de base para as Chaves Estrangeiras.
        """
        try:
            dados_tabela = funcoes_auxiliares.leitura_arquivo_informacoes_municipios()
            with self.CONEXAO_DB.connect() as conexao:
                conexao_raw = conexao.connection
                with conexao_raw.cursor() as cursor:
                    # Criação do schema de metadados
                    cursor.execute("CREATE SCHEMA IF NOT EXISTS dados_gerais")
                    conexao_raw.commit()
                    
                    # Tabela mestre de municípios
                    cursor.execute("""
                        CREATE TABLE IF NOT EXISTS dados_gerais.informacoes (
                            codigo_ibge VARCHAR(7) PRIMARY KEY,
                            nome_municipio VARCHAR(100) NOT NULL 
                        )
                    """)
                    conexao_raw.commit()
                    
                    # Inserção dos municípios da base de referência
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

    def inserir_dados_schema_dados_gerais(self, indicador: str, dataframe: DataFrame) -> None:
        """
        Função simplificada para inserir tabelas especificamente dentro do schema 'dados_gerais'.

        Args:
            indicador (str): Nome da tabela.
            dataframe (DataFrame): Dados a serem salvos.
        """
        try:
            with self.CONEXAO_DB.connect() as conexao:
                conexao_raw = conexao.connection
                with conexao_raw.cursor() as cursor:
                    cursor.execute("CREATE SCHEMA IF NOT EXISTS dados_gerais")
                    conexao_raw.commit()
                    
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
        """
        Altera a estrutura de uma tabela recém-criada para converter o tipo da coluna 
        'cod_municipio' e adicionar uma CONSTRAINT de chave estrangeira (FK).

        Args:
            nome_tabela (str): Tabela que receberá a chave estrangeira.
            topico (str): Schema onde a tabela se encontra.
        """
        nome_constraint = f"fk_{nome_tabela}_municipio"

        with self.CONEXAO_DB.connect() as conexao:
            conexao_raw = conexao.connection
            with conexao_raw.cursor() as cursor:
                # Garante que a coluna de código tenha o tipo correto para o relacionamento
                cursor.execute(f"ALTER TABLE {topico}.{nome_tabela} ALTER COLUMN cod_municipio TYPE VARCHAR(7);")
            
                # Adiciona o relacionamento com a tabela mestre de municípios
                comando = f"""
                    ALTER TABLE {topico}.{nome_tabela}
                    ADD CONSTRAINT {nome_constraint}
                    FOREIGN KEY (cod_municipio)
                    REFERENCES dados_gerais.informacoes (codigo_ibge);
                """
                cursor.execute(comando)
                conexao_raw.commit()

    def verificar_existencia_schema(self, topico: str):
        """
        Verifica se um determinado schema existe no banco de dados e o cria caso não exista.

        Args:
            topico (str): Nome do schema (tópico) a ser verificado.
        """
        try:
            with self.CONEXAO_DB.connect() as conexao:
                conexao_raw = conexao.connection
                with conexao_raw.cursor() as cursor:
                    comando = f"CREATE SCHEMA IF NOT EXISTS {topico}"
                    cursor.execute(comando)
                    conexao_raw.commit()
        except Exception as e:
            print(f"Erro ao verificar schema: {e}")