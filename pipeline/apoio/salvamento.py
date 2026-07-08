from sqlalchemy import Engine, inspect, text
from configuracoes.config import MapeamentoDiretoriosEArquivos
import pandas as pd
from sqlalchemy.sql.elements import quoted_name

class FuncoesDeApoioParaSalvamento:
    """Classe de apoio para funções relacionadas ao banco de dados."""
    def __init__(self):
        pass

    def VerificarSchema(self, conexao_com_o_banco: Engine , categoria: str):
        try:
            # Escapa o nome da schema para evitar SQL injection
            categoria_segura = categoria.lower().replace('-', '_').replace(' ', '_')
            comando_SQL = text(f"CREATE SCHEMA IF NOT EXISTS {categoria_segura}")

            with conexao_com_o_banco.connect() as conexao:
                conexao.execute(comando_SQL)
                conexao.commit()

        except Exception as e:
            print(f"Erro ao verificar schema: {e}")
    
    def DefinirChavePrimaria(self, conexao_com_o_banco: Engine, tabela: str, schema: str):
        
        schema_seguro = quoted_name(schema, quote=True)
        tabela_segura = quoted_name(tabela, quote=True)
        restricao_segura = quoted_name(f"pk_{tabela}_id", quote=True)
       
        consulta = (
            f"""
            ALTER TABLE {schema_seguro}.{tabela_segura}
            ADD CONSTRAINT {restricao_segura}
            PRIMARY KEY (id);
            """
        )

        comando_SQL = text(consulta)

        with conexao_com_o_banco.connect() as conexao:
            try:
                conexao.execute(comando_SQL)
                conexao.commit()
            except Exception as e:
                print(f"Erro ao definir chave primária: {e}")
    
    def DefinirChaveEstrangeira(self, conexao_com_o_banco: Engine, tabela: str, schema: str):

        schema_seguro = quoted_name(schema, quote=True)
        tabela_segura = quoted_name(tabela, quote=True)
        restricao_chave_estrangeira_segura = quoted_name(f"fk_{tabela}_municipio", quote=True)

        consulta_SQL = text(
            f"""
            ALTER TABLE {schema_seguro}.{tabela_segura}
            ADD CONSTRAINT {restricao_chave_estrangeira_segura}
            FOREIGN KEY (cod_municipio)
            REFERENCES dados_estadual.referencias_codigos_municipais (codigo_ibge);
            """
        )

        with conexao_com_o_banco.connect() as conexao:
            try:
                conexao.execute(consulta_SQL)
                conexao.commit()
            except Exception as e:
                print(f"Erro ao definir chave estrangeira: {e}")

    
    def CriarSchemaEstadual(self, conexao_com_o_banco: Engine) -> bool:
        comando_SQL = text("CREATE SCHEMA IF NOT EXISTS dados_estadual")
        try:
            with conexao_com_o_banco.connect() as conexao:
                conexao.execute(comando_SQL)
                conexao.commit()
        except Exception as e:
            print(f"Erro ao criar schema estadual: {e}")
            return False
        return True
    
    def InserirReferenciasCodigosMunicipais(self, conexao_com_o_banco: Engine) -> None:
        "Insere os códigos municipais de referência na tabela de informações gerais do banco de dados."
        "Esses dados são necessários para fazer um relacionamento entre o código municipal e o nome do município, garantindo a integridade dos dados e facilitando consultas futuras."
        
        if not self.CriarSchemaEstadual(conexao_com_o_banco):
            self.CriarSchemaEstadual(conexao_com_o_banco)

        inspector = inspect(conexao_com_o_banco)
        tabelas_estadual = inspector.get_table_names(schema="dados_estadual")

        if "referencias_codigos_municipais" in tabelas_estadual:
            return  
        
        tabela_de_referencia = MapeamentoDiretoriosEArquivos().arquivo_de_referencia_dos_codigos_municipais
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
            con=conexao_com_o_banco,
            index=False,
            method='multi'
        )
        print("Tabela de referência de códigos municipais salva com sucesso")  # Debug: Verificar se a tabela de referência foi salva com sucesso
        consulta_SQL = text(
            """
            ALTER TABLE dados_estadual.referencias_codigos_municipais
            ADD CONSTRAINT pk_referencias_codigo_ibge PRIMARY KEY (codigo_ibge);"""
        )

        with conexao_com_o_banco.connect() as conexao:
            conexao.execute(consulta_SQL)
            conexao.commit()


        
