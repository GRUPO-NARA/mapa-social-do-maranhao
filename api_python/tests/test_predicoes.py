import pytest
from fastapi.testclient import TestClient

from api_python.main import apiPython
from api_python.predicoes import PontoHistorico, RequisicaoPredicao, realizar_predicao


SERIE = [
    PontoHistorico(referencia=2018, valor=82.0),
    PontoHistorico(referencia=2019, valor=83.4),
    PontoHistorico(referencia=2020, valor=84.1),
    PontoHistorico(referencia=2021, valor=86.0),
    PontoHistorico(referencia=2022, valor=87.2),
    PontoHistorico(referencia=2023, valor=89.1),
    PontoHistorico(referencia=2024, valor=90.0),
]


def test_realiza_predicao_com_metricas_e_todos_os_pontos():
    resposta = realizar_predicao(
        RequisicaoPredicao(anoPrevisao=2030, dados=SERIE)
    )

    assert resposta.ano == 2030
    assert resposta.pontosTreinamento == len(SERIE)
    assert resposta.referenciaInicial == 2018
    assert resposta.referenciaFinal == 2024
    assert len(resposta.modelosAvaliados) == 2
    assert isinstance(resposta.predicao, float)


def test_aplica_limites_a_taxas():
    resposta = realizar_predicao(
        RequisicaoPredicao(
            anoPrevisao=2100,
            dados=SERIE,
            limiteInferior=0,
            limiteSuperior=100,
        )
    )

    assert resposta.predicao <= 100
    assert resposta.valorLimitado is True


def test_rejeita_ano_que_nao_e_futuro():
    with pytest.raises(ValueError, match="posterior"):
        RequisicaoPredicao(anoPrevisao=2024, dados=SERIE)


def test_endpoint_predicao():
    cliente = TestClient(apiPython)
    resposta = cliente.post(
        "/predicao",
        json={
            "anoPrevisao": 2030,
            "dados": [ponto.model_dump() for ponto in SERIE],
            "limiteInferior": 0,
            "limiteSuperior": 100,
        },
    )

    assert resposta.status_code == 200
    assert resposta.json()["ano"] == 2030


def test_health():
    cliente = TestClient(apiPython)
    resposta = cliente.get("/health")

    assert resposta.status_code == 200
    assert resposta.json() == {"status": "ok"}
