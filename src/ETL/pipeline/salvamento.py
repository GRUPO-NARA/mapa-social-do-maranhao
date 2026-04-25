
from arquivo_de_configuracao import ConfiguracoesProcessoDeETL
from funcoes_de_apoio.para_salvamento import FuncoesDeApoioSalvamento
from sqlalchemy import create_engine, Engine
from pandas import DataFrame


class FluxoDeSalvamento:
    """Classe responsável por realizar o salvamento dos dados tratados no banco de dados PostgreSQL."""
    def __init__(self):
        config = ConfiguracoesProcessoDeETL()
        self.apoio = FuncoesDeApoioSalvamento()
        self.logger_salvamento = config.configuracoes_logging_para_salvamento()
        self.logger_salvamento.info("Instância de SalvamentoEmBancoDeDados criada com sucesso.")
        
    def criar_conexao_com_banco_de_dados(self, url: str):
        """Cria uma conexão com o banco de dados usando SQLAlchemy."""
        try:
            engine = create_engine(url)
            return engine
        except Exception as e:
            self.logger_salvamento.error(f"Erro ao criar conexão com o banco de dados: {e}")
            raise
    
    def salvar_tabela_no_banco_de_dados(self, tabela: DataFrame, informacoes_do_indicador: dict, conexao_com_o_banco: Engine):
        """Salva um DataFrame em uma tabela do banco de dados, organizando-a por categoria."""
        
        self.apoio.inserir_referencias_codigos_municipais(conexao_com_o_banco)
        
        if not informacoes_do_indicador:
            self.logger_salvamento.error("Informações do indicador não fornecidas. Salvamento abortado.")
            return False
        
        indicador = informacoes_do_indicador.get("indicador")
        categoria = informacoes_do_indicador.get("categoria")
        
        if tabela is None or tabela.empty:
            self.logger_salvamento.warning(f"A tabela '{indicador}' está vazia ou é None. Salvamento abortado.")
            return False

        self.apoio.verificao_de_schema(conexao_com_o_banco, categoria)

        try:
            tabela.to_sql(
                name = indicador,
                schema = categoria,
                if_exists = 'replace',  
                chunksize = 10000,      
                con = conexao_com_o_banco,
                index = False,
                method = 'multi'        
            )
            self.apoio.definir_chave_primaria(
                engine= conexao_com_o_banco,
                tabela= indicador,
                schema= categoria)
            
            
            self.apoio.definir_chave_estrangeira(
                engine= conexao_com_o_banco,
                tabela= indicador,
                schema= categoria)
            
            return True
        
        except Exception as e:
            self.logger_salvamento.error(f"Erro ao salvar a tabela '{indicador}' na categoria '{categoria}': {e}")
            return False

    