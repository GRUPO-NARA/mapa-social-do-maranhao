
from io import BytesIO
from fluxo.coleta import FluxoDeColeta
from tratamentos.IMESC import TratamentoIMESC
from tratamentos.SIDRA import TratamentoSIDRA
from tratamentos.SAGICAD import TratamentoSAGICAD
from tratamentos.IPEADATA import TratamentoIPEADATA
from configuracoes.config import LoggerParaTratamento

logger_tratamento = LoggerParaTratamento()

class FluxoDeTratamento:
    """Orquestrador de tratamento específico para cada fonte de dados"""
    def __init__(self, coleta=None):
        logger_tratamento.info("Inicializando Fluxo de Tratamento.")
        if coleta is None:
            self.coleta = FluxoDeColeta()
        else:
            self.coleta = coleta
        self.tabelas_tratadas = {}  # Armazena cada tabela tratada por índice

    async def Tratamento(self):
        """Coleta dados em bytes, identifica a fonte e aplica tratamento."""
        # Coleta retorna uma lista de bytes (um por URL coletada)
        lista_de_bytes = await self.coleta.RespostaDaRequisicao()
        
        if not lista_de_bytes:
            raise RuntimeError("Nenhum dado foi coletado.")
        
        # Processa CADA URL coletada com suas respectivas informações
        for indice, bytes_do_arquivo in enumerate(lista_de_bytes):

            if not bytes_do_arquivo:  # Pula se não houver dados
                continue
                
            # Pega as informações correspondentes ao índice da coleta
            informacoes_do_indicador = self.coleta.InformacoesDoIndicador(indice)
            
            if not informacoes_do_indicador:
                continue
            
            fonte_dos_dados = informacoes_do_indicador.get("fonte")
            indicador = informacoes_do_indicador.get("indicador")
            
            # Converte os bytes do arquivo para um objeto de arquivo em memória
            arquivo = BytesIO(bytes_do_arquivo)
            try:
                if fonte_dos_dados == "SIDRA":
                    tabela = TratamentoSIDRA(arquivo, informacoes_do_indicador)
                    self.tabelas_tratadas[indice] = tabela
                    logger_tratamento.info(f"Indicador '{indicador}' (índice {indice}) tratado com sucesso")
                    print("-"*150)

                   
                elif fonte_dos_dados == "SAGICAD":
                    tabela = TratamentoSAGICAD(arquivo, informacoes_do_indicador)
                    self.tabelas_tratadas[indice] = tabela
                    logger_tratamento.info(f"Indicador '{indicador}' (índice {indice}) tratado com sucesso")
                    print("-"*150)

                elif fonte_dos_dados == "IPEADATA":
                    tabela = TratamentoIPEADATA(arquivo, informacoes_do_indicador)
                    self.tabelas_tratadas[indice] = tabela
                    logger_tratamento.info(f"Indicador '{indicador}' (índice {indice}) tratado com sucesso")
                    print("-"*150)

                elif fonte_dos_dados == "IMESC":
                    tabela = TratamentoIMESC(arquivo, informacoes_do_indicador)
                    self.tabelas_tratadas[indice] = tabela
                    logger_tratamento.info(f"Indicador '{indicador}' (índice {indice}) tratado com sucesso")
                    print("-"*150)
                else:
                    logger_tratamento.warning(f"Fonte '{fonte_dos_dados}' não reconhecida para {indicador}")
                    self.tabelas_tratadas[indice] = None

            except Exception as e:
                logger_tratamento.error(f"Erro ao tratar indicador '{indicador}': {e}")
                self.tabelas_tratadas[indice] = None
        
        return self.tabelas_tratadas

    def ObterTabelaTratada(self, indice: int):
        """Retorna a tabela tratada para um índice específico."""
        return self.tabelas_tratadas.get(indice)

