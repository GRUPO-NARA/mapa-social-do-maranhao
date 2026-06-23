import pytest
from fastapi.testclient import TestClient

from api_python.clusterizacao import (
    DadoMunicipio,
    ErroClusterizacao,
    RequisicaoClusterizacao,
    realizar_clusterizacao,
)
from api_python.main import apiPython


DADOS = [
    DadoMunicipio(municipio=f"Município {indice}", valor=valor)
    for indice, valor in enumerate(
        [10, 11, 12, 13, 45, 47, 49, 80, 82, 84, 86],
        start=1,
    )
]


def test_avalia_kmeans_e_hierarquico():
    resposta = realizar_clusterizacao(
        RequisicaoClusterizacao(referencia=2024, dados=DADOS)
    )

    algoritmos = {metrica.algoritmo for metrica in resposta.modelosAvaliados}

    assert algoritmos == {"K-Means", "Hierárquico Aglomerativo"}
    assert resposta.algoritmoSelecionado in algoritmos
    assert 2 <= resposta.quantidadeClusters <= 5
    assert sum(cluster.quantidadeMunicipios for cluster in resposta.clusters) == len(DADOS)
    assert resposta.totalMunicipios == len(DADOS)


def test_clusters_sao_ordenados_pela_media():
    resposta = realizar_clusterizacao(
        RequisicaoClusterizacao(referencia=2024, dados=DADOS)
    )

    medias = [cluster.media for cluster in resposta.clusters]
    assert medias == sorted(medias)


def test_rejeita_valores_sem_variacao():
    dados_iguais = [
        DadoMunicipio(municipio=f"Município {indice}", valor=10)
        for indice in range(4)
    ]

    with pytest.raises(ErroClusterizacao, match="distintos"):
        realizar_clusterizacao(
            RequisicaoClusterizacao(referencia=2024, dados=dados_iguais)
        )


def test_endpoint_clusterizacao():
    cliente = TestClient(apiPython)
    resposta = cliente.post(
        "/clusterizacao",
        json={
            "referencia": 2024,
            "dados": [dado.model_dump() for dado in DADOS],
        },
    )

    assert resposta.status_code == 200
    assert resposta.json()["totalMunicipios"] == len(DADOS)
    assert len(resposta.json()["modelosAvaliados"]) >= 2
