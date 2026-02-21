import pandas as pd
from pathlib import Path
import os
from funcoes_de_apoio import AuxiliaresTratamento
from operacoes_db import ConexaoPostgres
import time
from pysus.ftp.databases import SIM, CNES, SINAN



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

        self.COLUNAS_SIM_MAPA = ["CODMUNRES", "DTOBITO", "DTNASC", "CAUSABAS", "SEXO", "RACACOR", "ESC2010"]
        self.COLUNAS_CNES = {
            "ST": ["CNES", "CODUFMUN", "TP_UNID", "HOSP_SUS", "AMB_SUS", "ATIVIDAD"],
            "SR": ["CNES", "CODUFMUN", "SERV_ESP", "CLASS_SR", "TP_UNID", "AMB_SUS", "HOSP_SUS"],
            "LT": ["CNES", "CODUFMUN","TP_LEITO", "QT_EXIST", "QT_SUS"],
            "EQ": ["CNES", "CODUFMUN", "TIPEQUIP", "CODEQUIP", "QT_EXIST", "QT_USO", "IND_SUS", "IND_NSUS", "COMPETEN"],
            "PF": ["CNES", "CODUFMUN", "CBO", "PROF_SUS", "HORA_AMB", "HORAHOSP", "COMPETEN"]
        }
        self.COLUNAS_SINAN = {
            "ACGR": ["DT_ACGRAV", "ID_MN_RESI", "ID_MUNICIP", "NU_IDADE_N", "CS_SEXO", "CS_RACA", "CS_ESCOL_N",
                "EVOLUCAO", "CLASSI_FIN", "ACIDTRAB", "OCUPACAO", "ATIVIDADE"],
            "VIOL": ["ID_MN_RESI", "ID_MUNICIP", "NU_IDADE_N", "CS_SEXO", "CS_RACA", "CS_ESCOL_N",
                "TP_VIOL", "MEIO_AGRAV", "AUTOR_VIOL", "LOCAL_OCOR", "EVOLUCAO", "CLASSI_FIN"],
            "DENG": ["DT_SIN_PRI", "ID_MN_RESI", "ID_MUNICIP", "NU_IDADE_N", "CS_SEXO", "CS_RACA", "CS_ESCOL_N",
                "CLASSI_FIN", "EVOLUCAO", "HOSPITAL"]
        }

        
    def executar_processo_de_tratamento(self):
        """
        Executa sequencialmente o tratamento para todos os tipos de arquivos suportados.
        """
        self.arquivos_SIDRA()
        self.arquivos_SAGICAD()
        self.arquivos_ideb_dados_gerais_QEDU()
        self.arquivos_aprendizado_QEDU()
        #self.arquivos_SIM(colunas=self.COLUNAS_SIM_MAPA)
        #self.arquivos_CNES(colunas_por_grupo=self.COLUNAS_CNES)
        #self.arquivos_SINAN(grupos=("ACGR","VIOL","DENG"), ano_inicio=2024, ano_fim=2024)

    
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
    
    def arquivos_aprendizado_QEDU(self) -> None:
        """
        Processa arquivos de proficiência escolar (aprendizado) da plataforma QEDU (.xlsx).
        
        A função consolida dados de desempenho em Matemática e Português, traduz siglas 
        técnicas para nomes amigáveis, padroniza as colunas de ciclo e dependência 
        administrativa e separa os registros entre o nível estadual (MA) e municipal.
        """
        dados_separados = []
        
        # Varredura de diretórios para localização dos arquivos específicos de aprendizado
        for pasta in self.topicos:
            caminho_arquivos = self.pastas_informacoes_municipais / pasta
            arquivos_na_pasta = os.listdir(caminho_arquivos)
            
            for arquivo in arquivos_na_pasta:   
                if arquivo.startswith("aprendizado") and arquivo.endswith('QEDU.xlsx'):
                    caminho_arquivo = caminho_arquivos / arquivo
                    # Coleta dados das abas de municípios e do estado
                    dados_separados.append(pd.read_excel(caminho_arquivo, sheet_name='municipios'))
                    dados_separados.append(pd.read_excel(caminho_arquivo, sheet_name='estado'))
                    
        if dados_separados:
            # Consolida todos os arquivos encontrados em um único DataFrame
            df = pd.concat(dados_separados)
            
            # Dicionários de mapeamento para tradução de IDs e siglas
            map_dependencia = {0: 'total', 1: 'federal', 2: 'estadual', 3: 'municipal', 4: 'privada', 5: 'publica'}
            map_ciclo = {'AI': 'anos_iniciais', 'AF': 'anos_finais', 'EM': 'ensino_medio'}
                        
            # Aplicação do mapeamento e limpeza de colunas de ID
            df['ciclo'] = df['ciclo_id'].map(map_ciclo)
            df['dependencia_adm'] = df['dependencia_id'].map(map_dependencia)
            df.drop(columns=['dependencia_id','ciclo_id'], inplace = True)
            df.rename(columns={'ibge_id' : 'cod_municipio'}, inplace=True)
            df['unidade'] = 'percentual'
            
            # Tradução dinâmica dos nomes das colunas de proficiência
            # Converte prefixos técnicos (mt_ -> matematica_ | lp_ -> portugues_)
            for coluna in df.columns:
                if str(coluna).startswith('mt'):
                    df.rename(columns={coluna:str(coluna).replace('mt','matematica')}, inplace=True)
                elif str(coluna).startswith('lp'):
                    df.rename(columns={coluna:str(coluna).replace('lp','portugues')}, inplace=True) 
            
            # Segmentação e Inserção 1: Dados de nível Estadual (Código IBGE 21 para o Maranhão)
            df_agrupamento_estadual = df[df['cod_municipio'] == 21]
            database.inserir_dados_schema_dados_gerais('aprendizado_geral', df_agrupamento_estadual)
            
            # Segmentação e Inserção 2: Dados de nível Municipal
            df_agrupamento_municipal = df[df['cod_municipio'] != 21]
            # Trata valores ausentes com valor sentinela -1
            df_agrupamento_municipal.fillna(-1, inplace=True)
            database.verificar_existencia_schema('dados_educacao')
            database.inserir_dados('aprendizado_municipais','dados_educacao',df_agrupamento_municipal)
            
        else:
            print("Nenhum dado encontrado")

    def arquivos_SIM(
        self,
        grupo="CID10",
        uf="MA",
        ano_inicio=2015,
        ano_fim=2024,
        schema_destino="dados_saude",
        indicador_destino="sim_obitos",
        colunas=None,
        chunksize=None,
    ):
        print("Iniciando tratamento dos arquivos SIM...")

        ref_csv_path = (
            self.diretorio_dados_coletados
            / "informacoes_estaduais"
            / "informacoes_municipios.csv"
        )

        ref = pd.read_csv(ref_csv_path)
        ref2 = ref.copy()
        ref2["CODMUN7"] = ref2["cod_municipio"].astype(str).str.zfill(7)
        ref2["CODMUN6"] = ref2["CODMUN7"].str[:6]
        ref2 = ref2.drop_duplicates("CODMUN6")[["CODMUN6", "CODMUN7"]]
        
        sim = SIM().load()

        database.verificar_existencia_schema(schema_destino)

        for ano in range(ano_inicio, ano_fim + 1):
            try:
                print(f"Baixando SIM {grupo} | {uf} | {ano}")

                arquivos = sim.get_files(grupo, uf=uf, year=ano)
                parquet = sim.download(arquivos)
                df = parquet.to_dataframe()

                if colunas is not None:
                    cols_ok = [c for c in colunas if c in df.columns]
                    df = df[cols_ok].copy()

                if "CODMUNRES" in df.columns:
                    cod6 = (
                        df["CODMUNRES"]
                        .astype(str)
                        .str.strip()
                        .str.replace(r"\.0$", "", regex=True)
                        .str.zfill(6)
                    )

                    df = df.assign(CODMUN6=cod6).merge(ref2, left_on="CODMUN6", right_on="CODMUN6", how="left")

                    df["cod_municipio"] = df["CODMUN7"].astype("string")

                    df.drop(columns=["CODMUNRES", "CODMUN6", "CODMUN7"], inplace=True, errors="ignore")

                    df = df[df["cod_municipio"].notna()]

                    # datas
                    if "DTOBITO" in df.columns:
                        df["DTOBITO"] = pd.to_datetime(
                            df["DTOBITO"].astype(str),
                            format="%d%m%Y",
                            errors="coerce"
                        )

                    if "DTNASC" in df.columns:
                        df["DTNASC"] = pd.to_datetime(
                            df["DTNASC"].astype(str),
                            format="%d%m%Y",
                            errors="coerce"
                        )

                if {"DTOBITO", "DTNASC"}.issubset(df.columns):
                    anos_calc = df["DTOBITO"].dt.year - df["DTNASC"].dt.year
                    fez_aniversario = (
                        (df["DTOBITO"].dt.month > df["DTNASC"].dt.month)
                        | (
                            (df["DTOBITO"].dt.month == df["DTNASC"].dt.month)
                            & (df["DTOBITO"].dt.day >= df["DTNASC"].dt.day)
                        )
                    )
                    df["IDADE"] = anos_calc - (~fez_aniversario).astype(int)
                    df.loc[df["IDADE"] < 0, "IDADE"] = pd.NA
                    df.loc[df["IDADE"] > 130, "IDADE"] = pd.NA
                    df["IDADE"] = df["IDADE"].astype("Int64")

                if "ESC2010" in df.columns:
                    df["ESC2010"] = pd.to_numeric(df["ESC2010"], errors="coerce")
                    df.loc[~df["ESC2010"].isin([1, 2, 3, 4, 5]), "ESC2010"] = 9
                    df["ESC2010"] = df["ESC2010"].astype("Int64")

                if "RACACOR" in df.columns:
                    s = df["RACACOR"].astype(str).str.strip().replace("", pd.NA)
                    df["RACACOR"] = pd.to_numeric(s, errors="coerce")
                    df.loc[~df["RACACOR"].isin([1, 2, 3, 4, 5]), "RACACOR"] = 9
                    df["RACACOR"] = df["RACACOR"].fillna(9).astype("Int64")

                df["ANO"] = ano

                if "CODMUNRES" in df.columns:
                    df = df.rename(columns={"CODMUNRES": "cod_municipio"})

                database.inserir_dados(indicador_destino, schema_destino, df)

                print(f"SIM {ano} inserido ({len(df):,} linhas)")

            except Exception as e:
                print(f"Erro ao tratar/inserir SIM {ano}: {e}")
 
    def arquivos_CNES(
        self,
        grupos=("ST", "SR", "PF", "LT", "EQ"),
        uf="MA",
        ano_inicio=2015,
        ano_fim=2024,
        schema_destino="dados_saude",
        colunas_por_grupo=None
    ):
        print("Iniciando tratamento dos arquivos do CNES...")

        cnes_db = CNES().load(list(grupos))
        
        ref_csv_path = (
            self.diretorio_dados_coletados
            / "informacoes_estaduais"
            / "informacoes_municipios.csv"
        )
        ref = pd.read_csv(ref_csv_path)
        ref["CODMUN7"] = ref["cod_municipio"].astype(str).str.zfill(7)
        ref["CODMUN6"] = ref["CODMUN7"].str[:6]
        ref = ref.drop_duplicates("CODMUN6")[["CODMUN6", "CODMUN7"]]
        
        database.verificar_existencia_schema(schema_destino)

        inseriu_algum = False

        for ano in range(ano_inicio, ano_fim + 1):
            for g in grupos:
                try:
                    print(f"Baixando CNES | grupo={g} | uf={uf} | ano={ano}")

                    arquivos = cnes_db.get_files(g, uf=uf, year=ano)
                    if not arquivos:
                        print(f"Sem arquivos: grupo={g} uf={uf} ano={ano}")
                        continue

                    parquets = cnes_db.download(arquivos)

                    colunas_grupo = None
                    if hasattr(self, "COLUNAS_CNES") and isinstance(self.COLUNAS_CNES, dict):
                        colunas_grupo = self.COLUNAS_CNES.get(g)

                    tabela_destino = f"cnes_{g.lower()}"

                    for pq in parquets:
                        df = pq.to_dataframe()

                        df["ANO"] = ano
                        df["GRUPO"] = g

                        if "CODUFMUN" in df.columns:
                            cod6 = (
                                df["CODUFMUN"]
                                .astype(str)
                                .str.strip()
                                .str.replace(r"\.0$", "", regex=True)
                                .str.zfill(6)
                            )

                            df = (
                                df.assign(CODMUN6=cod6)
                                .merge(ref, on="CODMUN6", how="left")
                            )

                            df["cod_municipio"] = df["CODMUN7"]
                            df.drop(columns=["CODUFMUN", "CODMUN6", "CODMUN7"], inplace=True, errors="ignore")

                        if colunas_grupo is not None:
                            base = ["ANO", "GRUPO", "cod_municipio"]
                            cols_ok = [c for c in colunas_grupo if c in df.columns]
                            df = df[list(dict.fromkeys(cols_ok + base))].copy()

                        database.inserir_dados(tabela_destino, schema_destino, df)
                        inseriu_algum = True

                    print(f"Inserido: ano={ano} grupo={g} -> tabela {tabela_destino}")

                except Exception as e:
                    print(f"Erro CNES: ano={ano} grupo={g} -> {e}")

        if not inseriu_algum:
            raise ValueError("Nenhum dado CNES foi inserido (todos os filtros retornaram 0 arquivos).")

        print("Finalizado: CNES inserido no banco em tabelas separadas por grupo.")

    def arquivos_SINAN(
        self,
        grupos=("ACGR", "VIOL", "DENG"),
        ano_inicio=2015,
        ano_fim=2024,
        schema_destino="dados_saude",
        uf_ibge_prefixo="21",
        usar_municipio_residencia=True,
    ):
        print("Iniciando tratamento dos arquivos SINAN...")

        ref_csv_path = (
            self.diretorio_dados_coletados
            / "informacoes_estaduais"
            / "informacoes_municipios.csv"
        )
        ref = pd.read_csv(ref_csv_path)
        ref["CODMUN7"] = ref["cod_municipio"].astype(str).str.zfill(7)
        ref["CODMUN6"] = ref["CODMUN7"].str[:6]
        ref = ref.drop_duplicates("CODMUN6")[["CODMUN6", "CODMUN7"]]

        col_mun = "ID_MN_RESI" if usar_municipio_residencia else "ID_MUNICIP"

        sinan = SINAN().load()
        database.verificar_existencia_schema(schema_destino)

        inseriu_algum = False

        for ano in range(ano_inicio, ano_fim + 1):
            for g in grupos:
                try:
                    print(f"Baixando arquivos do SINAN | grupo={g} | ano={ano}")

                    arquivos = sinan.get_files(g, year=ano)  # sem uf
                    if not arquivos:
                        print(f"Não foi achado arquivos: grupo={g} ano={ano}")
                        continue

                    parquets = sinan.download(arquivos)

                    if hasattr(parquets, "__iter__") and not isinstance(parquets, (str, bytes)):
                        lista_pq = list(parquets)
                    else:
                        lista_pq = [parquets]

                    colunas_g = self.COLUNAS_SINAN.get(g)
                    if not colunas_g:
                        raise ValueError(f"Não há colunas definidas em self.COLUNAS_SINAN para o grupo '{g}'.")

                    tabela_destino = f"sinan_{g.lower()}"

                    for pq in lista_pq:
                        df = pq.to_dataframe()

                        cols_ok = [c for c in colunas_g if c in df.columns]
                        manter = list(dict.fromkeys(cols_ok + [col_mun]))
                        manter = [c for c in manter if c in df.columns]
                        df = df[manter].copy()

                        if col_mun not in df.columns:
                            print(f"  Aviso: {col_mun} não existe em grupo={g} ano={ano}; pulando arquivo.")
                            continue

                        mun6 = (
                            df[col_mun]
                            .astype(str)
                            .str.strip()
                            .str.replace(r"\.0$", "", regex=True)
                            .str.zfill(6)
                        )
                        df = df[mun6.str.startswith(uf_ibge_prefixo, na=False)].copy()
                        mun6 = mun6.loc[df.index]

                        if df.empty:
                            continue

                        df = df.assign(CODMUN6=mun6).merge(ref, on="CODMUN6", how="left")
                        df["cod_municipio"] = df["CODMUN7"].astype("string")

                        df = df[df["cod_municipio"].notna()].copy()
                        if df.empty:
                            continue

                        df.drop(columns=["CODMUN6", "CODMUN7"], inplace=True, errors="ignore")

                        for c in ("DT_NOTIFIC", "DT_ACGRAV", "DT_SIN_PRI"):
                            if c in df.columns:
                                df[c] = pd.to_datetime(df[c].astype(str), format="%d%m%Y", errors="coerce")

                        df["ANO"] = ano
                        df["GRUPO"] = g

                        df.drop(columns=["ID_MN_RESI", "ID_MUNICIP"], inplace=True, errors="ignore")

                        database.inserir_dados(tabela_destino, schema_destino, df)
                        inseriu_algum = True

                    print(f"Inserido: grupo={g} ano={ano} -> tabela {tabela_destino}")

                except Exception as e:
                    print(f"Erro SINAN: grupo={g} ano={ano} -> {e}")

        if not inseriu_algum:
            raise ValueError("Nenhum dado SINAN foi inserido (sem arquivos ou filtro UF removeu tudo).")

        print("Finalizado: SINAN inserido no banco (tabelas separadas por grupo).")