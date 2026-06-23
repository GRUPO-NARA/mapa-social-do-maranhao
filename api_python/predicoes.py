from __future__ import annotations

from math import ceil

import numpy as np
from pydantic import BaseModel, Field, model_validator
from sklearn.ensemble import RandomForestRegressor
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_absolute_error, r2_score


class ErroPredicao(ValueError):
    """Erro de domínio que pode ser apresentado ao consumidor da API."""


class PontoHistorico(BaseModel):
    referencia: int
    valor: float


class RequisicaoPredicao(BaseModel):
    anoPrevisao: int = Field(ge=1900, le=2200)
    dados: list[PontoHistorico] = Field(min_length=5)
    limiteInferior: float | None = None
    limiteSuperior: float | None = None

    @model_validator(mode="after")
    def validar_requisicao(self) -> "RequisicaoPredicao":
        referencias = [ponto.referencia for ponto in self.dados]

        if len(referencias) != len(set(referencias)):
            raise ValueError("As referências históricas não podem ser duplicadas")

        if self.anoPrevisao <= max(referencias):
            raise ValueError("O ano da previsão deve ser posterior ao último ano histórico")

        if (
            self.limiteInferior is not None
            and self.limiteSuperior is not None
            and self.limiteInferior >= self.limiteSuperior
        ):
            raise ValueError("O limite inferior deve ser menor que o limite superior")

        return self


class MetricasModelo(BaseModel):
    modelo: str
    mae: float
    r2: float | None


class RespostaPredicao(BaseModel):
    modelo: str
    ano: int
    predicao: float
    mae: float
    r2: float | None
    pontosTreinamento: int
    referenciaInicial: int
    referenciaFinal: int
    valorLimitado: bool
    modelosAvaliados: list[MetricasModelo]


def _criar_modelos() -> list[tuple[str, object]]:
    return [
        ("Regressão Linear", LinearRegression()),
        (
            "Random Forest",
            RandomForestRegressor(
                n_estimators=300,
                min_samples_leaf=2,
                random_state=42,
                n_jobs=-1,
            ),
        ),
    ]


def _avaliar_modelos(
    referencias: np.ndarray,
    valores: np.ndarray,
) -> tuple[str, list[MetricasModelo]]:
    quantidade_teste = max(2, ceil(len(referencias) * 0.2))
    ponto_corte = len(referencias) - quantidade_teste

    x_treino = referencias[:ponto_corte]
    x_teste = referencias[ponto_corte:]
    y_treino = valores[:ponto_corte]
    y_teste = valores[ponto_corte:]

    metricas: list[MetricasModelo] = []

    for nome, modelo in _criar_modelos():
        modelo.fit(x_treino, y_treino)
        estimativas = modelo.predict(x_teste)
        r2 = r2_score(y_teste, estimativas) if len(y_teste) >= 2 else None

        metricas.append(
            MetricasModelo(
                modelo=nome,
                mae=round(float(mean_absolute_error(y_teste, estimativas)), 6),
                r2=round(float(r2), 6) if r2 is not None else None,
            )
        )

    melhor = min(metricas, key=lambda metrica: metrica.mae)
    return melhor.modelo, metricas


def _novo_modelo(nome: str) -> object:
    modelos = dict(_criar_modelos())
    return modelos[nome]


def realizar_predicao(requisicao: RequisicaoPredicao) -> RespostaPredicao:
    pontos = sorted(requisicao.dados, key=lambda ponto: ponto.referencia)

    referencias = np.asarray(
        [[ponto.referencia] for ponto in pontos],
        dtype=float,
    )
    valores = np.asarray([ponto.valor for ponto in pontos], dtype=float)

    if not np.isfinite(referencias).all() or not np.isfinite(valores).all():
        raise ErroPredicao("A série histórica contém valores inválidos")

    nome_modelo, metricas = _avaliar_modelos(referencias, valores)

    modelo_final = _novo_modelo(nome_modelo)
    modelo_final.fit(referencias, valores)
    valor_original = float(modelo_final.predict([[requisicao.anoPrevisao]])[0])
    valor_previsto = valor_original

    if requisicao.limiteInferior is not None:
        valor_previsto = max(valor_previsto, requisicao.limiteInferior)

    if requisicao.limiteSuperior is not None:
        valor_previsto = min(valor_previsto, requisicao.limiteSuperior)

    metrica_vencedora = next(
        metrica for metrica in metricas if metrica.modelo == nome_modelo
    )

    return RespostaPredicao(
        modelo=nome_modelo,
        ano=requisicao.anoPrevisao,
        predicao=round(valor_previsto, 3),
        mae=metrica_vencedora.mae,
        r2=metrica_vencedora.r2,
        pontosTreinamento=len(pontos),
        referenciaInicial=pontos[0].referencia,
        referenciaFinal=pontos[-1].referencia,
        valorLimitado=not np.isclose(valor_original, valor_previsto),
        modelosAvaliados=metricas,
    )
