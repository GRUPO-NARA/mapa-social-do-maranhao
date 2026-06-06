from configuracoes.config import LoggerParaSalvamento
from apoio.salvamento import FuncoesDeApoioParaSalvamento
from fluxo.tratamento import FluxoDeTratamento
from sqlalchemy import create_engine, Engine
from dotenv import load_dotenv
import os 

load_dotenv()  # Carrega as variáveis de ambiente do arquivo .env

class FluxoDeSalvamento:
    """Classe responsável por realizar o salvamento dos dados tratados no banco de dados PostgreSQL."""
    def __init__(self, tratamento=None):
        self.logger_salvamento = LoggerParaSalvamento()
        self.funcoes_de_apoio = FuncoesDeApoioParaSalvamento()
        if tratamento is None:
            self.tratamento = FluxoDeTratamento()
        else:
            self.tratamento = tratamento

        self.logger_salvamento.info("Instância de SalvamentoEmBancoDeDados criada com sucesso.")
        
    def EstabelecerConexao(self) -> Engine:
        """Cria uma conexão com o banco de dados usando SQLAlchemy."""
        # colocar variável de ambiente para senha e usuário do banco de dados
        URI = os.getenv("SQLALCHEMY_DATABASE_URI")
        try:
            conexao = create_engine(URI)
            return conexao
        except Exception as e:
            self.logger_salvamento.error(f"Erro ao criar conexão com o banco de dados: {e}")
            raise
    
    async def Salvar(self, informacoes_do_indicador: dict, conexao_com_o_banco: Engine, indice: int = None):
        """Salva um DataFrame em uma tabela do banco de dados, organizando-a por categoria."""
        
        # Obtém a tabela tratada do índice específico
        if indice is not None:
            tabela = self.tratamento.ObterTabelaTratada(indice)
        else:
            # Fallback: executa o tratamento completo
            tabelas = await self.tratamento.Tratamento()
            tabela = tabelas.get(0) if tabelas else None
       
        self.funcoes_de_apoio.InserirReferenciasCodigosMunicipais(conexao_com_o_banco)

        if not informacoes_do_indicador:
            self.logger_salvamento.error("Informações do indicador não fornecidas. Salvamento abortado.")
            return False
        
        indicador = informacoes_do_indicador.get("indicador")
        categoria = informacoes_do_indicador.get("categoria")
        
        if tabela is None or tabela.empty:
            self.logger_salvamento.warning(f"A tabela '{indicador}' está vazia ou é None. Salvamento abortado.")
            return False

        self.funcoes_de_apoio.VerificarSchema(conexao_com_o_banco, categoria)

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
            self.funcoes_de_apoio.DefinirChavePrimaria(
                conexao_com_o_banco,
                indicador,
                categoria
            )
            self.funcoes_de_apoio.DefinirChaveEstrangeira(
                conexao_com_o_banco,
                indicador,
                categoria
            )
            
            self.logger_salvamento.info(f"Indicador '{indicador}' salvo com sucesso na schema '{categoria}'")
            return True
        
        except Exception as e:
            self.logger_salvamento.error(f"Erro ao salvar a tabela '{indicador}' na categoria '{categoria}': {e}")
            return False

    