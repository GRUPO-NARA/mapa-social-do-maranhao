# MAPA SOCIAL DO MARANHÃO


## Como utilizar 

#### O que você precisa
- __Docker__
- __PostgreSQL__ (vem com o Docker)

## Passo a passo 

#### 1. Baixar o projeto 

```bash
git clone https://github.com/synapseLEA/mapa-social-MA.git
cd mapa-social-MA
```
#### 2. Configurar 
Renomeie o arquivo .env_modelo para .env
```bash
# Editar o arquivo .env
# (Use as informações que foram enviadas para você)
```

__Atenção:__ Não esqueça de adicionar .env no .gitignore depois!

#### 3. Iniciar
```bash
docker-compose up -d
```
Espere alguns segundos para o banco de dados ficar pronto.

#### 4. Conectar ao banco 
Use o PGAdmin 4 para conectar:
- __Host:__ localhost
- __Porta:__ 5430
- __Usuário e senha:__ (os que você colocou no arquivo .env)

#### Comandos úteis
```bash
# Ver se está funcionando
docker-compose ps

# Ver mensagens do sistema
docker-compose logs

# Parar tudo
docker-compose down

# Parar e apagar dados
docker-compose down -v
```


