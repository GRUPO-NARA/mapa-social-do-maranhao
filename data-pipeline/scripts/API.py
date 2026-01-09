from fastapi import FastAPI
import uvicorn
import os
from pathlib import Path
import json

app = FastAPI()

diretorio_atual = Path(__file__).resolve().parent
        
diretorio_dados_tratados = diretorio_atual / '..' / 'dados' / 'dados-tratados'
        
pastas_nos_dados_tratados = os.listdir(diretorio_dados_tratados)
        
def inicializar_API():
        
    uvicorn.run(
        app,
        port=8000
    )
    
@app.get('/teste')
def teste():
    return {'mensagem' : 'ola'}

@app.get('/indicadores')
def buscar_indicadores():
    
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
     
if __name__ == '__main__':
    inicializar_API()
