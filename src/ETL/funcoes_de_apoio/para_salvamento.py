from sqlalchemy import Engine, inspect
from funcoes_de_apoio.para_tratamento import FuncoesDeApoioParaTratamento
import pandas as pd

class FuncoesDeApoioSalvamento:
    """Classe de apoio para funções relacionadas ao banco de dados."""
    def __init__(self):
        pass

    def verificao_de_schema(self, engine: Engine , categoria: str):
        try:
            with engine.connect() as conexao_com_o_banco_de_dados:
                with conexao_com_o_banco_de_dados.connection.cursor() as realizador_de_comandos_sql:
                    script_sql = f"CREATE SCHEMA IF NOT EXISTS {categoria}"
                    realizador_de_comandos_sql.execute(script_sql)
                    conexao_com_o_banco_de_dados.connection.commit()
        except Exception as e:
            print(f"Erro ao verificar schema: {e}")
    
    def definir_chave_primaria(self, engine: Engine, tabela: str, schema: str):
        
        restricao_chave_primaria = f"pk_{tabela}_id"

        with engine.connect().connection.cursor() as executor_de_comandos_sql:
            comando_sql = f"""
                ALTER TABLE {schema}.{tabela}
                ADD CONSTRAINT {restricao_chave_primaria}
                PRIMARY KEY (id);
            """
            executor_de_comandos_sql.execute(comando_sql)
            executor_de_comandos_sql.connection.commit()
    
    def definir_chave_estrangeira(self, engine: Engine, tabela: str, schema: str):

        restricao_chave_estrangeira = f"fk_{tabela}_municipio"

        with engine.connect().connection.cursor() as executor_de_comandos_sql:
            comando_sql = f"""
                ALTER TABLE {schema}.{tabela}
                ADD CONSTRAINT {restricao_chave_estrangeira}
                FOREIGN KEY (cod_municipio)
                REFERENCES dados_estadual.referencias_codigos_municipais (codigo_ibge);
            """
            executor_de_comandos_sql.execute(comando_sql)
            executor_de_comandos_sql.connection.commit()
    
    def criar_schema_estadual(self, engine: Engine) -> bool:
        try:
            with engine.connect().connection.cursor() as executor_de_comandos_sql:
                comando_sql = "CREATE SCHEMA IF NOT EXISTS dados_estadual"
                executor_de_comandos_sql.execute(comando_sql)
                executor_de_comandos_sql.connection.commit()
        except Exception as e:
            print(f"Erro ao criar schema estadual: {e}")
            return False
        return True
    
    def inserir_referencias_codigos_municipais(self, engine: Engine) -> None:
        "Insere os códigos municipais de referência na tabela de informações gerais do banco de dados."
        "Esses dados são necessários para fazer um relacionamento entre o código municipal e o nome do município, garantindo a integridade dos dados e facilitando consultas futuras."
        
        if not self.criar_schema_estadual(engine):
            self.criar_schema_estadual(engine)
        
        inspector = inspect(engine)
        tabelas_estadual = inspector.get_table_names(schema="dados_estadual")
        
        if "referencias_codigos_municipais" in tabelas_estadual:
            return  
        
        tabela_de_referencia = FuncoesDeApoioParaTratamento().referencia_dos_codigos_municipais
        tabela_de_referencia = pd.read_csv(tabela_de_referencia, sep=',')
        tabela_de_referencia.rename(columns={
            "cod_municipio" : "codigo_ibge",
            "nome_municipio" : "municipio"
        }, inplace=True)
        tabela_de_referencia['codigo_ibge'] = tabela_de_referencia['codigo_ibge'].astype(str)
        tabela_de_referencia['municipio'] = tabela_de_referencia['municipio'].str.strip()

        tabela_de_referencia.to_sql(
            name="referencias_codigos_municipais",
            schema="dados_estadual",
            if_exists="replace",
            chunksize=10000,
            con=engine,
            index=False,
            method='multi'
        )
        
        with engine.connect().connection.cursor() as executor_de_comandos_sql:
            comando_sql = """
                ALTER TABLE dados_estadual.referencias_codigos_municipais
                ADD CONSTRAINT pk_referencias_codigo_ibge PRIMARY KEY (codigo_ibge);
            """
            try:
                executor_de_comandos_sql.execute(comando_sql)
                executor_de_comandos_sql.connection.commit()
            except Exception as e:
      
                executor_de_comandos_sql.connection.rollback()
                pass


        