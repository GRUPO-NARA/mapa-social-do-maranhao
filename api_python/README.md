# API Python

Serviço interno responsável por executar os modelos preditivos e de clusterização do Mapa Social do Maranhão.

O Spring Boot busca a série histórica no banco e envia os pontos para `POST /predicao`. A API Python não acessa o banco nem chama o Spring, o que mantém o serviço desacoplado da origem dos dados.

Para a clusterização, o Spring Boot envia os valores municipais da referência mais recente para `POST /clusterizacao`. A API avalia K-Means e Clusterização Hierárquica Aglomerativa com duas a cinco classes e seleciona a combinação com maior índice silhouette.

## Execução local

```bash
python -m venv .venv
.venv/Scripts/pip install -r api_python/requirements-dev.txt
.venv/Scripts/uvicorn api_python.main:apiPython --reload --port 8000
```

Documentação interativa: `http://localhost:8000/docs`.

## Exemplo

```json
{
  "anoPrevisao": 2030,
  "dados": [
    {"referencia": 2020, "valor": 84.1},
    {"referencia": 2021, "valor": 86.0},
    {"referencia": 2022, "valor": 87.2},
    {"referencia": 2023, "valor": 89.1},
    {"referencia": 2024, "valor": 90.0}
  ],
  "limiteInferior": 0,
  "limiteSuperior": 100
}
```

## Testes

```bash
pytest api_python/tests
```
