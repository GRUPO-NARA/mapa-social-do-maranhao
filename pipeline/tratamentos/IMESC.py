import json
from typing import BinaryIO

import pandas as pd

from configuracoes.config import LoggerParaTratamento


logger_tratamento = LoggerParaTratamento()


def TratamentoIMESC(arquivo: BinaryIO, informacoes_do_arquivo: dict):
    """Realiza o tratamento dos dados coletados da fonte IMESC."""

    logger_tratamento.info(f"Iniciando tratamento do indicador: {informacoes_do_arquivo.get('indicador')} da fonte IMESC.")

    if not arquivo:
        logger_tratamento.error("Arquivo para tratamento não encontrado.")
        return None

    nome_do_indicador = informacoes_do_arquivo.get("indicador")
    fonte = informacoes_do_arquivo.get("fonte")

    try:
        dados = json.load(arquivo)
        valores = dados["values"]
    except (UnicodeDecodeError, json.JSONDecodeError, KeyError, TypeError) as erro:
        logger_tratamento.error(f"Resposta JSON do IMESC inválida: {erro}")
        return None

    tabela = (
        pd.DataFrame.from_dict(valores, orient="index")
        .rename_axis("cod_municipio")
        .reset_index()
        .melt(
            id_vars=["cod_municipio"],
            var_name="referencia",
            value_name="valor",
        )
    )

    tabela["cod_municipio"] = tabela["cod_municipio"].astype("str")
    tabela["referencia"] = tabela["referencia"].astype(int)
    tabela["valor"] = pd.to_numeric(tabela["valor"], errors="coerce")
    tabela["fonte"] = fonte
    tabela["indicador"] = nome_do_indicador

    tabela = tabela[
        [
            "cod_municipio",
            "referencia",
            "fonte",
            "indicador",
            "valor",
        ]
    ]
    tabela.insert(0, "id", range(1, len(tabela) + 1))

    return tabela