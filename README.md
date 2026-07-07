# Mapa Social do Maranhão

O **Mapa Social do Maranhão** reúne indicadores públicos dos 217 municípios
maranhenses em uma interface única, facilitando a consulta e a comparação de
informações demográficas, geográficas, econômicas, de saúde e de assistência
social.

O projeto é desenvolvido pelo **NARA - Núcleo de Análise e Recursos
Analíticos**, em colaboração com o **Ministério Público do Maranhão (MPMA)**.

> Esta documentação descreve a execução do projeto em ambiente local para
> desenvolvimento e testes.

## Visão geral

A aplicação é formada por cinco serviços principais:

| Serviço | Tecnologia | Responsabilidade |
| --- | --- | --- |
| Frontend | Next.js e TypeScript | Interface para visualização dos indicadores |
| Backend | Spring Boot e Java | API REST que consulta os dados |
| API Python | FastAPI, scikit-learn e NumPy | Predição e clusterização dos indicadores com algoritmos de aprendizado de máquina |
| Pipeline | Python e Pandas | Coleta, tratamento e carga dos dados |
| Banco de dados | PostgreSQL | Armazenamento dos indicadores processados |

```mermaid
flowchart LR
    fontes["SIDRA / SAGICAD / IPEADATA"]
    pipeline["Pipeline Python"]
    banco["PostgreSQL"]
    backend["API Spring Boot"]
    apiPython["API Python"]
    frontend["Frontend Next.js"]
    usuario["Usuário"]

    fontes -->|"coleta"| pipeline
    pipeline -->|"tratamento e carga"| banco
    banco -->|"consulta"| backend
    backend -->|"predição e clusterização"| apiPython
    backend -->|"API REST"| frontend
    frontend --> usuario
```

## Recursos da aplicação

A interface permite selecionar um município, escolher uma área temática e
analisar indicadores por meio de cards, gráficos e agrupamentos municipais.

Principais recursos:

- resumo do indicador selecionado, com valor mais recente, melhor resultado,
  ponto de atenção e variação no período;
- gráfico de evolução histórica quando há série temporal suficiente;
- comparação territorial por barras entre municípios selecionados;
- projeção de valores futuros por meio da API Python;
- clusterização dos municípios com resultados semelhantes;
- tratamento visual para dados indisponíveis, valores inválidos ou indicadores
  com apenas uma referência.

### Serviço Python de análise

O serviço `api-python` concentra os algoritmos de aprendizado de máquina usados
pela aplicação. O backend Spring Boot consulta esse serviço internamente sempre
que o usuário solicita previsão ou clusterização.

Na predição, a API Python avalia:

- Regressão Linear;
- Random Forest Regressor.

Os modelos são comparados pelo erro médio absoluto (`MAE`) em uma separação
temporal simples da série histórica. O modelo com menor erro é usado para gerar
a projeção final. Quando o indicador é percentual, o backend envia limites para
evitar previsões abaixo de 0 ou acima de 100.

Na clusterização, a API Python avalia:

- K-Means;
- Agrupamento Hierárquico Aglomerativo.

As combinações de algoritmo e quantidade de grupos são comparadas pelo índice
`silhouette`. A resposta retorna o algoritmo selecionado, os municípios de cada
grupo, valores mínimo e máximo, média do grupo e as métricas avaliadas.

Quando não há dados históricos suficientes, valores distintos ou municípios em
quantidade mínima, a interface mostra uma mensagem informativa em vez de exibir
erro técnico no console.

Alguns indicadores públicos possuem apenas uma fotografia de referência, como
`idade_mediana`, `percentual_de_envelhecimento` e
`pessoas_de_2_anos_ou_mais_com_deficiencia`. Nesses casos, a aplicação prioriza
a comparação territorial e não tenta gerar evolução histórica ou previsão.

Referências mensais, como `2025-3`, também são aceitas nas análises. O backend
ordena essas referências cronologicamente para encontrar o dado mais recente.

## Início rápido com Docker

Esta é a maneira recomendada de executar o projeto completo. É necessário ter
o [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e
em execução.

### 1. Clone o repositório

```bash
git clone https://github.com/GRUPO-NARA/mapa-social-do-maranhao.git
cd mapa-social-do-maranhao
```

### 2. Crie o arquivo de ambiente

No PowerShell:

```powershell
Copy-Item .env.exemplo .env
```

No Linux ou macOS:

```bash
cp .env.exemplo .env
```

Abra o arquivo `.env` e substitua `sua_senha_aqui` por uma senha local. A mesma
senha deve ser usada em `POSTGRES_PASSWORD`, `SQLALCHEMY_DATABASE_URI` e
`SPRING_DATASOURCE_PASSWORD`.

Exemplo:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=minha_senha
POSTGRES_DB=postgres

SQLALCHEMY_DATABASE_URI=postgresql+psycopg2://postgres:minha_senha@db:5432/postgres
SQLALCHEMY_URI=postgresql+psycopg2://postgres:minha_senha@db:5432/postgres
PIPELINE_HOSTS_INSEGUROS=dataimesc.imesc.ma.gov.br
PIPELINE_AGENDADOR_INTERVALO=60

SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=minha_senha
SPRING_JPA_HIBERNATE_DDL_AUTO=validate

BACKEND_PORT=8080
BACKEND_HOST=0.0.0.0
NEXT_PUBLIC_API_URL=http://localhost:8080
ENDERECOS_PERMITIDOS=http://localhost:3000
HABILITAR_DOCUMENTACAO_SWAGGER=false
PYTHON_API_URL=http://localhost:8000
```

> Se a senha contiver caracteres especiais, eles podem precisar ser
> codificados na URL de `SQLALCHEMY_DATABASE_URI`. Para evitar esse problema no
> ambiente de desenvolvimento, prefira uma senha alfanumérica.
>
> `PIPELINE_HOSTS_INSEGUROS` deve conter apenas hosts conhecidos cujo
> certificado TLS não valida corretamente. Atualmente o IMESC exige essa
> exceção; os demais hosts continuam com validação TLS normal.

### 3. Suba a aplicação

```bash
docker compose up --build
```

Na primeira execução, o Docker precisa baixar as imagens e instalar as
dependências. Além disso, o pipeline coleta e processa os indicadores antes de
liberar o backend. Por isso, a inicialização pode levar alguns minutos.

Depois da carga inicial, o serviço `pipeline-scheduler` permanece em execução
e atualiza os dados periodicamente. O intervalo é definido por
`PIPELINE_AGENDADOR_INTERVALO`.

Quando a aplicação estiver pronta:

| Recurso | Endereço |
| --- | --- |
| Frontend | http://localhost:3000 |
| API | http://localhost:8080 |
| API Python | http://localhost:8000 |
| Saúde da API | http://localhost:8080/actuator/health |
| Saúde da API Python | http://localhost:8000/health |

Por segurança, a documentação interativa da API fica desativada por padrão.
Para habilitar o Swagger UI somente em ambiente local, use `HABILITAR_DOCUMENTACAO_SWAGGER=true`
e acesse `http://localhost:8080/swagger-ui/index.html`.

O PostgreSQL fica disponível apenas para os serviços da rede Docker e não é
publicado diretamente em uma porta do computador.

### Comandos úteis

Executar em segundo plano:

```bash
docker compose up --build -d
```

Ver o estado dos serviços:

```bash
docker compose ps -a
```

Acompanhar os logs:

```bash
docker compose logs -f
```

Ver apenas os logs do frontend:

```bash
docker compose logs -f frontend
```

Parar os serviços:

```bash
docker compose down
```

Apagar também o banco local e reprocessar todos os dados na próxima execução:

```bash
docker compose down -v
docker compose up --build
```

> `docker compose down -v` remove permanentemente o volume do PostgreSQL.

### Comportamento esperado dos containers

O serviço `pipeline` é executado uma vez durante a inicialização. Depois que a
carga termina com sucesso, seu estado aparece como `Exited (0)`. Isso é
esperado: o backend só inicia depois que o pipeline termina, e o frontend só
inicia quando a API está saudável.

O serviço `pipeline-scheduler` usa a mesma imagem, mas permanece em execução
para atualizar os dados periodicamente. Ele começa depois da carga inicial e
aguarda o primeiro intervalo antes de rodar novamente.

## Execução local para desenvolvimento

Neste modo, cada parte da aplicação roda diretamente no computador. É útil
para desenvolver com recarregamento automático e acompanhar os logs de cada
serviço separadamente.

### Pré-requisitos

| Tecnologia | Versão recomendada |
| --- | --- |
| Node.js | 22 |
| Python | 3.11 |
| Java JDK | 17 ou superior |
| PostgreSQL | 14 ou superior |

Também é necessário ter uma instância local do PostgreSQL em execução. Os
exemplos abaixo consideram:

```text
host: localhost
porta: 5432
banco: postgres
usuário: postgres
senha: minha_senha
```

Altere os valores conforme a configuração da sua máquina.

### Ordem de inicialização

No modo local, execute os componentes nesta ordem:

1. PostgreSQL
2. Pipeline
3. API Python
4. Backend
5. Frontend

O pipeline cria e alimenta as tabelas consumidas pelo backend.

### 1. Execute o pipeline

Abra um terminal na raiz do projeto.

No PowerShell:

```powershell
cd pipeline
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
pip install -r requirements.txt

$env:SQLALCHEMY_DATABASE_URI="postgresql+psycopg2://postgres:minha_senha@localhost:5432/postgres"
python main.py
```

No Linux ou macOS:

```bash
cd pipeline
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
pip install -r requirements.txt

export SQLALCHEMY_DATABASE_URI="postgresql+psycopg2://postgres:minha_senha@localhost:5432/postgres"
python main.py
```

Aguarde a mensagem `ETL concluído com sucesso!` antes de iniciar os demais
serviços.

### 2. Execute a API Python

Abra outro terminal na raiz do projeto.

No PowerShell:

```powershell
cd api_python
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install --upgrade pip
pip install -r requirements.txt
uvicorn api_python.main:apiPython --app-dir .. --host 0.0.0.0 --port 8000
```

No Linux ou macOS:

```bash
cd api_python
python3 -m venv .venv
source .venv/bin/activate
python -m pip install --upgrade pip
pip install -r requirements.txt
uvicorn api_python.main:apiPython --app-dir .. --host 0.0.0.0 --port 8000
```

Confirme que o serviço está disponível:

```bash
curl http://localhost:8000/health
```

### 3. Execute o backend

Abra outro terminal na raiz do projeto.

No PowerShell:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/postgres"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="minha_senha"
$env:BACKEND_PORT="8080"
$env:BACKEND_HOST="0.0.0.0"
$env:ENDERECOS_PERMITIDOS="http://localhost:3000"
$env:PYTHON_API_URL="http://localhost:8000"

cd backend
.\mvnw.cmd spring-boot:run
```

No Linux ou macOS:

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/postgres"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="minha_senha"
export BACKEND_PORT="8080"
export BACKEND_HOST="0.0.0.0"
export ENDERECOS_PERMITIDOS="http://localhost:3000"
export PYTHON_API_URL="http://localhost:8000"

cd backend
./mvnw spring-boot:run
```

Confirme que a API está disponível:

```bash
curl http://localhost:8080/actuator/health
```

A resposta esperada é:

```json
{"status":"UP"}
```

### 4. Execute o frontend

Abra um terceiro terminal na raiz do projeto.

No PowerShell:

```powershell
cd frontend
Copy-Item .env.local.exemplo .env.local
npm ci
npm run dev
```

No Linux ou macOS:

```bash
cd frontend
cp .env.local .env.local
npm ci
npm run dev
```

O arquivo `frontend/.env.local` deve conter:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

Acesse http://localhost:3000.

## Testando a API

A documentação interativa fica desativada por padrão. Em ambiente local,
defina `HABILITAR_DOCUMENTACAO_SWAGGER=true` para usar o
[Swagger UI](http://localhost:8080/swagger-ui/index.html).

Alguns exemplos:

```bash
# Lista os municípios
curl "http://localhost:8080/estadual/municipios"

# Consulta a população de São Luís
curl "http://localhost:8080/demograficos/populacao?municipio=S%C3%A3o%20Lu%C3%ADs"

# Consulta o PIB municipal
curl "http://localhost:8080/economicos/produtoInternoBruto?municipio=S%C3%A3o%20Lu%C3%ADs"

# Consulta os principais dados municipais
curl "http://localhost:8080/informacoes/dadosPrincipaisMunicipal?municipio=S%C3%A3o%20Lu%C3%ADs"
```

### Principais endpoints

| Método | Endpoint | Parâmetro |
| --- | --- | --- |
| GET | `/estadual/municipios` | Nenhum |
| GET | `/demograficos/populacao` | `municipio` |
| GET | `/demograficos/populacaoEstadualRecente` | Nenhum |
| GET | `/demograficos/quantidadeDeHomens` | `municipio` |
| GET | `/demograficos/quantidadeDeMulheres` | `municipio` |
| GET | `/demograficos/quantidadeDeResidentesRurais` | `municipio` |
| GET | `/demograficos/idh` | `municipio` |
| GET | `/demograficos/evolucaoIDH` | `municipio` |
| GET | `/geograficos/areaTotal` | `municipio` |
| GET | `/geograficos/densidadeDemografica` | `municipio` |
| GET | `/economicos/produtoInternoBruto` | `municipio` |
| GET | `/economicos/produtoInternoBrutoAgregadoEstadual` | Nenhum |
| GET | `/saude/idadeMediana` | `municipio` |
| GET | `/informacoes/dadosPrincipaisMunicipal` | `municipio` |
| GET | `/informacoes/evolucaoDoIndicador` | `schema`, `indicador`, `municipio` |
| GET | `/informacoes/comparacaoTerritorial` | `schema`, `indicador`, `municipios` |
| GET | `/predicoes` | `schema`, `indicador`, `municipio`, `ano` |
| GET | `/clusterizacoes` | `schema`, `indicador` |

## Fontes e indicadores

Os dados são obtidos de fontes públicas:

- [SIDRA/IBGE](https://sidra.ibge.gov.br/)
- [SAGICAD](https://aplicacoes.cidadania.gov.br/vis/data3/data-explorer.php)
- [IPEADATA](https://www.ipeadata.gov.br/)
- [IBGE Cidades](https://cidades.ibge.gov.br/)

Atualmente, o projeto trabalha com indicadores como:

- população residente, homens, mulheres e residentes rurais;
- Índice de Desenvolvimento Humano;
- área territorial e densidade demográfica;
- Produto Interno Bruto municipal e estadual;
- idade mediana e indicadores relacionados à saúde;
- famílias e pessoas inscritas no Cadastro Único;
- acesso domiciliar à água encanada e energia elétrica.
- indicadores de educação, como aprovação, analfabetismo, docentes e evasão.

## Estrutura do projeto

```text
mapa-social-do-maranhao/
|-- api_python/              API FastAPI para predição e clusterização
|   |-- main.py
|   |-- predicoes.py
|   |-- clusterizacao.py
|   |-- tests/
|   |-- requirements.txt
|   `-- Dockerfile
|-- backend/                 API Spring Boot
|   |-- src/main/java/       controllers, services, repositories e entidades
|   |-- src/main/resources/  configuração da aplicação
|   |-- pom.xml
|   `-- Dockerfile
|-- frontend/                aplicação Next.js
|   |-- app/                 páginas e layout
|   |-- components/          cards, gráficos, indicadores e componentes principais
|   |-- public/              imagens e ícones
|   |-- tratamento/          funções auxiliares de formatação
|   |-- package.json
|   `-- Dockerfile
|-- pipeline/                processo de ETL em Python
|   |-- fluxo/               coleta, tratamento e salvamento
|   |-- tratamentos/         regras específicas por fonte
|   |-- informacoes/         URLs e referências municipais
|   |-- main.py
|   |-- requirements.txt
|   `-- Dockerfile
|-- docker-compose.yml
|-- .env.exemplo
`-- README.md
```

## Solução de problemas

### O frontend falha durante a instalação no Docker

Reconstrua apenas essa imagem sem cache:

```bash
docker compose build --no-cache frontend
docker compose up
```

O Dockerfile usa `npm ci`, portanto `frontend/package.json` e
`frontend/package-lock.json` precisam permanecer sincronizados.

### O frontend abre, mas não carrega os dados

Verifique primeiro a saúde do backend:

```bash
curl http://localhost:8080/actuator/health
```

Depois confirme que `NEXT_PUBLIC_API_URL` aponta para
`http://localhost:8080`. Como variáveis `NEXT_PUBLIC_*` são incorporadas
durante o build do Next.js, reconstrua o frontend após alterar esse valor.

### A previsão ou clusterização aparece como indisponível

Isso pode acontecer mesmo com a aplicação funcionando corretamente. A previsão
precisa de uma série histórica mínima, e a clusterização precisa de dados
municipais suficientes e valores distintos para formar grupos.

Indicadores com apenas uma referência, como idade mediana, percentual de
envelhecimento e pessoas de 2 anos ou mais com deficiência, devem ser lidos
principalmente por comparação territorial. A interface exibe uma mensagem
amigável quando não há dados suficientes para gerar previsão ou grupos.

Se a mensagem aparecer para muitos indicadores ao mesmo tempo, confirme a saúde
da API Python:

```bash
curl http://localhost:8000/health
```

### O backend não conecta ao banco

Confira usuário, senha, host e nome do banco. No Docker, o host deve ser `db`.
Na execução local, deve ser `localhost`.

```text
Docker: jdbc:postgresql://db:5432/postgres
Local:  jdbc:postgresql://localhost:5432/postgres
```

### A porta 3000 ou 8080 já está em uso

Encerre o processo que ocupa a porta ou altere o mapeamento correspondente em
`docker-compose.yml`.

### O pipeline aparece como encerrado

Use `docker compose ps -a`. O estado `Exited (0)` indica conclusão normal. Para
investigar uma saída com erro:

```bash
docker compose logs pipeline
```

Para acompanhar as atualizações recorrentes:

```bash
docker compose logs -f pipeline-scheduler
```

## Desenvolvimento e contato

Os desenvolvedores fazem parte do **NARA**, grupo de discentes de Engenharia da
Computação da **Universidade Estadual do Maranhão (UEMA)**.

| Membro | Atuação | GitHub |
| --- | --- | --- |
| Júlio César | Full stack e gestão | [@JulioCesra](https://github.com/JulioCesra) |
| Carlos Vinícius | Frontend | [@amorimcarlos](https://github.com/amorimcarlos) |
| Bruno Raphael | Backend | [@BrunoAndrade-dev](https://github.com/BrunoAndrade-dev) |
| Ana Elise | Backend | [@elisasilva06](https://github.com/elisasilva06) |

O projeto está em desenvolvimento e deverá ser integrado futuramente à
plataforma oficial do Ministério Público do Maranhão.
