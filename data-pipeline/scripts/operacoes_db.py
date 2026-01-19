import os
from typing import List
from dotenv import load_dotenv
from pandas import DataFrame 
from sqlalchemy import create_engine, URL, Engine 
from funcoes_auxiliares import AuxiliaresTratamento

funcoes_auxiliares = AuxiliaresTratamento()

class ConexaoPostgres:
    def __init__(self):
        load_dotenv()
        self.CONEXAO_DB = self.estabelecer_conexao()

    def estabelecer_conexao(self) -> Engine:
        try:
            USUARIO_DB = os.getenv('DB_USER') 
            HOST_DB = os.getenv('DB_HOST')
            SENHA_DB = os.getenv('DB_PASS')
            NOME_DB = os.getenv('DB_NAME')
            url = URL.create(
                "postgresql+psycopg2", username=USUARIO_DB, password=SENHA_DB, host=HOST_DB, database=NOME_DB
            )
            return create_engine(url)
        except Exception as e:
            print(e)

    def inserir_dados(self, indicador:str, topico:str, dataframe: DataFrame) -> None:
        try:

            if indicador == 'responsaveis_familiares_do_sexo_feminino_beneficiarias_do_auxilio_gas':
                indicador = 'responsaveis_familiares_feminino_beneficiarias_do_auxilio_gas'

            dataframe.to_sql(
                name = indicador,
                schema = topico,
                if_exists = 'replace',
                chunksize=10000,
                con = self.CONEXAO_DB,
                index=False
            )
            self.anexar_chave_estrangeira(indicador, topico)
        except Exception as e:
            print(e)

    def inserir_tabela_municipios(self, codigos_municipais:List[str] , nomes_municipios:List[str] ) -> None:

        try:
            dados_tabela = funcoes_auxiliares.leitura_arquivo_informacoes_municipios()
            with self.CONEXAO_DB.connect() as conexao:
                conexao_raw = conexao.connection
                with conexao_raw.cursor() as cursor:
                    cursor.execute("""
                        CREATE SCHEMA IF NOT EXISTS dados_municipios
                    """)
                    conexao_raw.commit()
                    cursor.execute("""
                        CREATE TABLE IF NOT EXISTS dados_municipios.informacoes (
                                   codigo_ibge VARCHAR(7) PRIMARY KEY,
                                   nome_municipio VARCHAR(100) NOT NULL 
                                   )
                    """)
                    conexao_raw.commit()
                    for item_atual in range(0, len(dados_tabela['codigos_municipais'])): 
                        cursor.execute("""
                            INSERT INTO dados_municipios.informacoes (
                            codigo_ibge, nome_municipio
                            ) VALUES (%s, %s) ON CONFLICT (codigo_ibge) DO NOTHING
                        """, (
                        dados_tabela['codigos_municipais'][item_atual],
                        dados_tabela['nomes_municipios'][item_atual]
                        ))
                    conexao_raw.commit()

        except Exception as e:
            print(e)
        

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
            REFERENCES dados_municipios.informacoes (codigo_ibge);
            """
                cursor.execute(comando)
                conexao_raw.commit()