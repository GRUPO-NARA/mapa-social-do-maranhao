from __future__ import annotations

import numpy as np
from pydantic import BaseModel, Field, model_validator
from sklearn.cluster import AgglomerativeClustering, KMeans
from sklearn.metrics import silhouette_score
from sklearn.preprocessing import RobustScaler


class ErroClusterizacao(ValueError):
    """Erro de domínio que pode ser apresentado ao consumidor da API."""


class DadoMunicipio(BaseModel):
    municipio: str = Field(min_length=1, max_length=100)
    valor: float


class RequisicaoClusterizacao(BaseModel):
    referencia: int
    dados: list[DadoMunicipio] = Field(min_length=4)
    minimoClusters: int = Field(default=2, ge=2, le=5)
    maximoClusters: int = Field(default=5, ge=2, le=5)

    @model_validator(mode="after")
    def validar_requisicao(self) -> "RequisicaoClusterizacao":
        municipios = [dado.municipio.strip().casefold() for dado in self.dados]

        if len(municipios) != len(set(municipios)):
            raise ValueError("Os municípios não podem ser duplicados")

        if self.minimoClusters > self.maximoClusters:
            raise ValueError("O mínimo de clusters deve ser menor ou igual ao máximo")

        return self


class MetricaModelo(BaseModel):
    algoritmo: str
    quantidadeClusters: int
    silhouette: float


class ClusterMunicipal(BaseModel):
    nome: str
    quantidadeMunicipios: int
    faixa: str
    media: float
    valorMinimo: float
    valorMaximo: float
    municipios: list[str]


class RespostaClusterizacao(BaseModel):
    algoritmoSelecionado: str
    quantidadeClusters: int
    silhouette: float
    referencia: int
    totalMunicipios: int
    modelosAvaliados: list[MetricaModelo]
    clusters: list[ClusterMunicipal]


def _formatar_numero(valor: float) -> str:
    return f"{valor:.3f}".rstrip("0").rstrip(".")


def _nome_cluster(indice: int, quantidade: int) -> str:
    if indice == 0:
        return "Resultado mais baixo"
    if indice == quantidade - 1:
        return "Resultado mais alto"
    if quantidade == 3:
        return "Resultado intermediário"
    return f"Resultado intermediário {indice}"


def _criar_modelos(quantidade_clusters: int) -> list[tuple[str, object]]:
    return [
        (
            "K-Means",
            KMeans(
                n_clusters=quantidade_clusters,
                n_init=20,
                random_state=42,
            ),
        ),
        (
            "Hierárquico Aglomerativo",
            AgglomerativeClustering(
                n_clusters=quantidade_clusters,
                linkage="ward",
            ),
        ),
    ]


def realizar_clusterizacao(
    requisicao: RequisicaoClusterizacao,
) -> RespostaClusterizacao:
    valores = np.asarray(
        [[dado.valor] for dado in requisicao.dados],
        dtype=float,
    )

    if not np.isfinite(valores).all():
        raise ErroClusterizacao("Os dados municipais contêm valores inválidos")

    quantidade_valores_distintos = len(np.unique(valores))
    maximo_clusters = min(
        requisicao.maximoClusters,
        len(requisicao.dados) - 1,
        quantidade_valores_distintos,
    )

    if maximo_clusters < requisicao.minimoClusters:
        raise ErroClusterizacao(
            "Não há valores distintos suficientes para formar os clusters"
        )

    valores_normalizados = RobustScaler().fit_transform(valores)
    avaliacoes: list[tuple[MetricaModelo, np.ndarray]] = []

    for quantidade_clusters in range(requisicao.minimoClusters, maximo_clusters + 1):
        for nome, modelo in _criar_modelos(quantidade_clusters):
            rotulos = modelo.fit_predict(valores_normalizados)
            quantidade_rotulos = len(np.unique(rotulos))

            if quantidade_rotulos < 2 or quantidade_rotulos >= len(valores):
                continue

            silhouette = float(silhouette_score(valores_normalizados, rotulos))
            avaliacoes.append(
                (
                    MetricaModelo(
                        algoritmo=nome,
                        quantidadeClusters=quantidade_clusters,
                        silhouette=round(silhouette, 6),
                    ),
                    rotulos,
                )
            )

    if not avaliacoes:
        raise ErroClusterizacao("Não foi possível encontrar uma clusterização válida")

    melhor_metrica, melhores_rotulos = max(
        avaliacoes,
        key=lambda avaliacao: avaliacao[0].silhouette,
    )

    rotulos_ordenados = sorted(
        np.unique(melhores_rotulos),
        key=lambda rotulo: float(valores[melhores_rotulos == rotulo].mean()),
    )

    clusters: list[ClusterMunicipal] = []
    for indice, rotulo in enumerate(rotulos_ordenados):
        posicoes = np.flatnonzero(melhores_rotulos == rotulo)
        valores_cluster = valores[posicoes, 0]
        municipios = sorted(
            (requisicao.dados[posicao].municipio for posicao in posicoes),
            key=str.casefold,
        )
        minimo = float(valores_cluster.min())
        maximo = float(valores_cluster.max())

        clusters.append(
            ClusterMunicipal(
                nome=_nome_cluster(indice, len(rotulos_ordenados)),
                quantidadeMunicipios=len(posicoes),
                faixa=f"De {_formatar_numero(minimo)} a {_formatar_numero(maximo)}",
                media=round(float(valores_cluster.mean()), 3),
                valorMinimo=round(minimo, 3),
                valorMaximo=round(maximo, 3),
                municipios=municipios,
            )
        )

    return RespostaClusterizacao(
        algoritmoSelecionado=melhor_metrica.algoritmo,
        quantidadeClusters=melhor_metrica.quantidadeClusters,
        silhouette=melhor_metrica.silhouette,
        referencia=requisicao.referencia,
        totalMunicipios=len(requisicao.dados),
        modelosAvaliados=[metrica for metrica, _ in avaliacoes],
        clusters=clusters,
    )
