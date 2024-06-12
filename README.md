# Desafio 03

### Autores
- Grupo 03 - ByteStorm
- [Matheus Laurentino](https://github.com/mathlaurentino)
- [Helon Xavier](https://github.com/Helon21)

## Intalação

1. Clone este repositório:
```
  git clone https://github.com/Helon21/ByteStorm-Desafio3.git
```

2. Navegue até o diretório do projeto:
```
cd ByteStorm-Desafio3
```
3. Abra uma janela do IntelliJ para cada micro serviço (eureka, msfuncionarios, ms-propostas, msresultados, ms-gateway)

4. Em cada projeto, abra o menu "Project Structure", certifique-se de selecionar o
   SDK e o nível de linguagem Java 17.


5. Na raiz da pasta `ByteStorm-Desafio3` existe um arquivo chamado `docker-compose`, ele iniciará o Kafka com o seguinte comando:
```
  docker-compose up
```

6. Após iniciar o Kafka, inicie cada um dos projetos na seguinte ordem: eureka, msfuncionarios, ms-propostas, msresultados, ms-gateway


7. Ao iniciar os tres principais micro serviços, um arquivo que termina com `mv.db` será criado na raiz
   do projeto. Este arquivo é o banco de dados H2 utilizado pela aplicação.


8. Se você possui o Postman instalado, importe o arquivo
   `Desafio_03.postman_collection.json` que contém todas as rotas da API.

9. Acesse a documentação do Swagger através das seguintes URLs:
```
  http://localhost:{porta_do_msresultados}/docs-resultados.html
  http://localhost:{porta_do_msfuncionarios}/docs-funcionarios.html
  http://localhost:{porta_do_mspropostas}/docs-propostas.html
```

10. Caso queira acessar o banco de dados, vá para a seguinte URL e deixe os campos
   "User Name" e "Password" em branco:
```
http://localhost:{porta_do_serviço}/h2-console
```

## Descrição do Projeto

Este projeto é uma arquitetura de micro serviços composta por três serviços principais, além de utilizar o Eureka para o registro e descoberta de serviços, e um Gateway para roteamento. A seguir, uma descrição detalhada de cada serviço e seu papel no sistema:

### 1. **msFuncionarios**

Este serviço é responsável por gerenciar os funcionários da organização. Ele oferece funcionalidades completas de CRUD para os dados dos funcionários. Além disso, mantém informações sobre o status dos funcionários, que podem estar "ativos" ou "inativos". Apenas funcionários ativos têm permissão para criar e votar em propostas.

### 2. **msPropostas**

O msPropostas é encarregado do gerenciamento de propostas. Suas principais funcionalidades incluem:

- **CRUD de Propostas**: Criação, leitura e inativação de propostas.
- **Abertura de Propostas para Votação**: Permite que propostas sejam abertas para votação pelos funcionários. O endpoint de criação de propostas requer, além dos detalhes da proposta, o ID do funcionário que está criando-a. Utilizando o OpenFeign, o msPropostas consulta o msFuncionarios para verificar se o funcionário está ativo antes de permitir a criação da proposta.
- **Votação**: Funciona de maneira semelhante à criação de propostas. O ID do funcionário é verificado no msFuncionarios para garantir que ele esteja ativo antes de permitir a votação.
- **Produção de Mensagens Kafka**: Quando uma votação é concluída, o resultado é enviado para o Kafka, onde o msPropostas atua como um producer.

### 3. **msResultados**

O msResultados tem a responsabilidade de receber e gerenciar os resultados das votações. Suas funcionalidades incluem:

- **Consumo de Mensagens Kafka**: Este serviço é um consumer do Kafka, recebendo os resultados das votações.
- **Persistência dos Resultados**: Armazena os resultados das votações em um banco de dados.
- **Exposição dos Resultados**: Oferece endpoints para a consulta dos resultados das votações.

## Infraestrutura Adicional

### 4. **Eureka**

Utilizamos o Eureka para o registro e descoberta de serviços, facilitando a comunicação entre os micro serviços. Cada micro serviço se registra no Eureka Server, que mantém um catálogo atualizado de todos os serviços disponíveis no sistema.

### 5. **Gateway**

O Gateway é configurado para roteamento de todas as requisições, permitindo que todos os serviços sejam acessados através de uma única porta.

## Resumo do Funcionamento do Sistema

1. **Criação de Propostas**: Um funcionário ativo cria uma proposta através do msPropostas, que verifica o status do funcionário no msFuncionarios usando OpenFeign.
2. **Abertura para Votação**: A proposta pode ser aberta para votação, com um tempo determinado para a duração da votação.
3. **Votação**: Os funcionários ativos podem votar nas propostas. O msPropostas verifica novamente o status do funcionário no msFuncionarios antes de permitir a votação.
4. **Envio dos Resultados**: Após o término da votação, o msPropostas envia os resultados para o Kafka.
5. **Processamento dos Resultados**: O msResultados consome as mensagens do Kafka, persiste os dados no banco de dados e disponibiliza os resultados através de seus endpoints.

## Documentação dos Endpoints

## MS Funcionarios

### Funcionalidades

1. Cadastrar um novo funcionário;
2. Alterar dados de um funcionário;
3. Alterar status de um funcionário;
4. Buscar funcionário por ID;
5. Buscar funcionário por CPF;
6. Buscar todos os funcionários com paginação.

### Endpoints

#### Cadastra um novo funcionário

```http
  POST /api/v1/funcionarios
```
```
{ 
    "nome": "Maria",
    "cpf": "91181247047",
    "dataNascimento": "2009-10-12",
    "sexo": "FEMININO"
}
```
- O cpf precisa ser válido e único. <br>
- A data de nascimento no formato yyyy-mm-dd
- Sexo MASCULINO ou FEMININO

#### Alterar dados de um funcionário

```http
  PUT /api/v1/funcionarios/{id}
```
```
{ 
    "nome": "Maria",
    "cpf": "91181247047",
    "dataNascimento": "2009-10-12",
    "sexo": "FEMININO"
}
```
- O cpf precisa ser válido e único. <br>
- A data de nascimento no formato yyyy-mm-dd
- Sexo MASCULINO ou FEMININO

#### Alterar status de um funcionário

```http
  PATCH /api/v1/funcionarios/{id}
```
```
{
    "status": "ATIVO"
}
```
- Status ATIVO ou INATIVO
- É possivel alterar o status livremente

#### Buscar funcionário por id

```http
  GET api/v1/funcionarios/id/{id}
```

#### Buscar funcionário por cpf

```http
  GET api/v1/funcionarios/cpf/{cpf}
```

#### Buscar todos os funcionários

```http
  GET api/v1/funcionarios
```

- atributos possíveis na query:
- `page` (query): Representa a página retornada.
    - Valor padrão: `0`

- `size` (query): Representa o total de elementos por página.
    - Valor padrão: `8`


## MS Propostas

### Funcionalidades

1. Criar uma nova proposta;
2. Iniciar o processo de votação de uma proposta;
3. Inativar uma proposta
4. Buscar propostas com filtros e paginação.
5. Buscar uma proposta por ID;
6. Receber votos para uma proposta

### Endpoints

#### Criar uma nova proposta

```http
  POST /api/v1/propostas
```
```
{
    "titulo": "Proposta melhoria na saúde",
    "descricao": "Devemos focar na saúde pois ...",
    "funcionarioId": 10
}
```
- Quando uma nova proposta é criada, o `ms-propostas` utiliza OpenFeign para buscar no `ms-funcionarios` os dados do <br>
  funcionário que está criando a proposta. Se o funcionário não for encontrado ou estiver com status inativo, a proposta <br>
  não será criada.

#### Iniciar processo de votação de uma proposta

```http
  PATCH api/v1/propostas/iniciar-votacao/{id}
```
```
{
    "tempoVotacaoMinutos": 10
}
```
- O `tempoVotacaoMinutos` representa a quantidade de tempo que a proposta ficara em aberto para votação.

#### Inabilitar Proposta

```http
  PATCH api/v1/propostas/inabilitar-proposta/{id}
```
- Muda o status da proposta para "inativo", impedindo que se abra uma votação para a mesma.

#### Buscar propostas com paginação e filtros

```http
  GET api/v1/propostas
```

- `titulo` (query): Filtra os resultados com base no título fornecido.

- `funcionarioId` (query): Busca as propostas feitas pelo ID do funcionário.

- `status` (query): Filtra os resultados com base no status fornecido. Valores possíveis:
    - `ATIVO`
    - `INATIVO`
    - `EM_VOTACAO`
    - `VOTACAO_ENCERRADA`

- `page` (query): Representa a página retornada.
    - Valor padrão: `0`

- `size` (query): Representa o total de elementos por página.
    - Valor padrão: `8`

- `direction` (query): Representa a ordenação do resultado.
    - Valor padrão: `asc`

#### Buscar uma proposta por id

```http
  GET api/v1/propostas/id/{id}
```

#### Votar em uma proposta

```http
  POST api/v1/votos/votar/{ìd}
```
```
{
    "funcionarioId": 41,
    "status": "APROVADO"
}
```
- Status: `APROVADO` ou `REJEITADO`
- Quando um voto é realizado, o `ms-propostas` utiliza OpenFeign para buscar no `ms-funcionarios` os dados do funcionário <br>
  que está realizando o voto. Se o funcionário não for encontrado ou estiver com status inativo, o voto não será computado.

## MS Resultados

### Funcionalidades

1. Buscar resultado de uma proposta por o ID da proposta;
2. Buscar todos os resultados de propostas com filtros e paginação.

### Endpoints

#### Buscar por o ID proposta

```http
  GET api/v1/resultados/propostaId/{id}
```

#### Buscar todas as propostas com paginação e filtros

```http
  GET api/v1/resultados/buscarResultados
```

- `titulo` (query): Filtra os resultados com base no título fornecido.

- `funcionarioId` (query): Busca as propostas feitas pelo ID do funcionário.

- `status` (query): Filtra os resultados com base no status fornecido. Valores possíveis:
    - `APROVADO`
    - `REJEITADO`

- `dataVotacao` (query): Busca as propostas que tiveram sua votação a partir da data fornecida.

- `page` (query): Representa a página retornada.
    - Valor padrão: `0`

- `size` (query): Representa o total de elementos por página.
    - Valor padrão: `8`

- `direction` (query): Representa a ordenação do resultado.
    - Valor padrão: `asc`