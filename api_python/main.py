from fastapi import FastAPI, HTTPException

from api_python.clusterizacao import (
    ErroClusterizacao,
    RequisicaoClusterizacao,
    RespostaClusterizacao,
    realizar_clusterizacao,
)
from api_python.predicoes import (
    ErroPredicao,
    RequisicaoPredicao,
    RespostaPredicao,
    realizar_predicao,
)

apiPython = FastAPI(
    title="Mapa Social MA — Modelos Preditivos",
    description="Serviço interno de previsão e análise de dados do Mapa Social do Maranhão.",
    version="1.0.0",
)


@apiPython.get("/health", tags=["Infraestrutura"])
def health() -> dict[str, str]:
    return {"status": "ok"}


@apiPython.post("/predicao", response_model=RespostaPredicao, tags=["Predição"])
def predicao(requisicao: RequisicaoPredicao) -> RespostaPredicao:
    try:
        return realizar_predicao(requisicao)
    except ErroPredicao as erro:
        raise HTTPException(status_code=422, detail=str(erro)) from erro


@apiPython.post(
    "/clusterizacao",
    response_model=RespostaClusterizacao,
    tags=["Clusterização"],
)
def clusterizacao(requisicao: RequisicaoClusterizacao) -> RespostaClusterizacao:
    try:
        return realizar_clusterizacao(requisicao)
    except ErroClusterizacao as erro:
        raise HTTPException(status_code=422, detail=str(erro)) from erro
