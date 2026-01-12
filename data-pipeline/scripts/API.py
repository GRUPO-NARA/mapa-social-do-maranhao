from fastapi import FastAPI # type: ignore
import uvicorn # type: ignore
import os
from pathlib import Path
import json
from fastapi.exceptions import HTTPException # type: ignore
from coleta import ColetaESalvamento # type: ignore
from tratamento import ArquivodeTratamento # type: ignore
import asyncio

app = FastAPI()

try:

    # Quando a API for inicializada, é realizado a coleta e tratamento dos dados
    asyncio.run(ColetaESalvamento().realizar_coleta())
    ArquivodeTratamento().executar_processo_de_tratamento()

    diretorio_atual = Path(__file__).resolve().parent
        
    diretorio_dados_tratados = diretorio_atual / '..' / 'dados' / 'dados-tratados'

    pastas_nos_dados_tratados = os.listdir(diretorio_dados_tratados)

except FileNotFoundError:
    raise HTTPException(status_code=500, detail="Diretório de dados não encontrado ou vazio.")

except Exception as e:
    raise HTTPException(status_code=500, detail=f"Erro genérico: {e}")




      
def inicializar_API():
        
    uvicorn.run(
        app,
        port=8000
    )
    
@app.get('/indicadores')
async def buscar_indicadores():
    
    quantidade_de_indicadores = 0
    
    lista_de_indicadores = []
        
    for pasta in pastas_nos_dados_tratados:
            
        diretorio_pasta = diretorio_dados_tratados / pasta
            
        arquivos_na_pasta = os.listdir(diretorio_pasta)
            
        for arquivo in arquivos_na_pasta:
                
            caminho_arquivo = diretorio_pasta / arquivo
                
            with open(caminho_arquivo, 'r') as f:
                    
                leitura_arquivo = json.load(f)
                
            indicador = leitura_arquivo
                
            cobertura_inicio = indicador[0]['referencia']
                
            cobertura_fim = indicador[-1]['referencia']
            
            dados_indicador = {
                    
                'id' : indicador[0]['indicador'],
                
                'fonte' : indicador[0]['fonte'],
                
                'unidade' : indicador[0]['unidade'],
                
                'granularidade' : '',
                
                'cobertura' : {
                    'inicio' : cobertura_inicio,
                    'fim' : cobertura_fim
                    }
            }
            
            if dados_indicador['cobertura'].get('inicio').count('-') > 0:
                dados_indicador['granularidade'] = 'municipio_mes'
            else:
                dados_indicador['granularidade'] = 'municipio_ano'
        
            quantidade_de_indicadores += 1
        
            lista_de_indicadores.append(dados_indicador)
        
    corpo_requisicao = {
        'quantidade_de_indicadores' : quantidade_de_indicadores,
        'indicadores' : lista_de_indicadores
    }
        
    return corpo_requisicao

def definir_formato_da_referencia(referencia):
    formato_da_referencia = '' 
    if referencia.count('-') > 0:
        formato_da_referencia = 'ano-mes'
    else:
        formato_da_referencia = 'ano'
    
    return formato_da_referencia

def verificar_referencia(arquivo: json, referencia: str):
    formato_da_referencia = definir_formato_da_referencia(referencia)
    if formato_da_referencia == 'ano':
        for dados in arquivo:
            if dados['referencia'] != referencia:
                raise HTTPException(status_code=404, detail="Não foram encontrados dados para a referência solicitada!")
    else:
        # Divide a referência (ex: "2023-05") em duas variáveis
        ano_requisitado, mes_requisitado = referencia.split('-')

        ano_encontrado = False
        mes_encontrado = False

        for dados in arquivo:
            texto_ref = dados['referencia']
            ano_ref, mes_ref = texto_ref.split('-')

            if ano_ref == ano_requisitado:
                ano_encontrado = True
                # Se achou o ano, verifica se o mês também bate
                if mes_ref == mes_requisitado:
                    mes_encontrado = True
                    # Aqui você provavelmente quer salvar os 'dados' ou retornar algo
                    break 

        # Verificações de erro fora do loop
        if not ano_encontrado:
            raise HTTPException(status_code=404, detail="Não foram encontrados dados para o ano solicitado!")

        if not mes_encontrado:
            raise HTTPException(status_code=404, detail="Não foram encontrados dados para o mês solicitado!")

        # Se chegou aqui, é porque encontrou ambos!

@app.get('/dados')
def buscar_por_indicador_e_referencia(indicador: str, referencia: str):
    
    formato_da_referencia_de_entrada = definir_formato_da_referencia(referencia)
    
    if referencia[-1] == '-':
        raise HTTPException(status_code=404, detail=f'A referência precisa estar no formato (ano-mes). A referência fornecida foi: {referencia}')
    
        
    for pasta in pastas_nos_dados_tratados:
        
        diretorio_pasta = diretorio_dados_tratados / pasta
            
        arquivos_na_pasta = os.listdir(diretorio_pasta)
        
        for arquivo in arquivos_na_pasta:
            
            fontes = [
                'SAGICAD',
                'SIDRA'    
            ]
            
            texto_separado = arquivo.replace('_', ' ')
            
            for fonte in fontes:
                
                if fonte in texto_separado:
                    
                    localizacao_referencia = texto_separado.find(fonte)
                    
                    break
            
            indicador_do_arquivo = arquivo[:localizacao_referencia - 1]
            
            quantidade_de_dados = 0
            if indicador_do_arquivo == indicador:
                
                caminho_arquivo = diretorio_pasta / arquivo
                
                with open(caminho_arquivo, 'r') as f:
                    
                    leitura_do_arquivo = json.load(f)
                
                dados_iniciais = leitura_do_arquivo[0]
                formato_da_referencia_do_arquivo = definir_formato_da_referencia(dados_iniciais['referencia'])
                
                if formato_da_referencia_de_entrada != formato_da_referencia_do_arquivo:
                    raise HTTPException(status_code=404, detail="Verifique o formato da referência")
                
                fonte_dados = dados_iniciais['fonte']
                unidade = dados_iniciais['unidade']
                referencia_dos_dados = referencia
                lista_de_dados = []
                
                verificar_referencia(leitura_do_arquivo, referencia)
                    
                for dados in leitura_do_arquivo:
                    dicionario_dos_dados = { 'cod_municipio' : dados['cod_municipio'], 'valor' : dados['valor']}
                    lista_de_dados.append(dicionario_dos_dados)
                    quantidade_de_dados += 1
                
                corpo_da_requisicao = {
                    'filtros' : {
                        'indicador' : indicador_do_arquivo,
                        'referencia' : referencia_dos_dados
                    },
                    'fonte' : fonte_dados,
                    'unidade' : unidade,
                    'quantidade_de_dados' : quantidade_de_dados,
                    'dados' : lista_de_dados
                }
                # Mudar o formato dos valores para int64 ou float64
                # Adicionar quando nao for encontrado o indicador
                return corpo_da_requisicao  
            
    raise HTTPException(status_code=404, detail="Indicador não encontrado.")
    
#if __name__ == '__main__':
    #inicializar_API()
    #buscar_por_indicador_e_referencia('quantidade_de_pessoas_do_sexo_masculino_inscritas_no_cadastro_unico', '2025-12')