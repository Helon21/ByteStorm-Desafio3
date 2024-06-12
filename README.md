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

```
  GET api/v1/funcionarios/id/{id}
```

#### Buscar funcionário por cpf

```
  GET api/v1/funcionarios/cpf/{cpf}
```

#### Buscar todos os funcionários

```
  GET api/v1/funcionarios/cpf/{cpf}
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

```
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

```
  PATCH api/v1/propostas/iniciar-votacao/15
```
```
{
    "tempoVotacaoMinutos": 10
}
```
- O `tempoVotacaoMinutos` representa a quantidade de tempo que a proposta ficara em aberto para votação.

#### Inabilitar Proposta

```
  PATCH api/v1/propostas/inabilitar-proposta/1
```
- Muda o status da proposta para "inativo", impedindo que se abra uma votação para a mesma.

#### Buscar propostas com paginação e filtros

```
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

```
  GET api/v1/propostas/id/{id}
```

#### Votar em uma proposta

```
  POST /api/v1/votos/votar/{ìd}
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

```
  GET http://localhost:8080/api/v1/resultados/propostaId/{id}
```

#### Buscar todas as propostas com paginação e filtros

```
  GET http://localhost:8080/api/v1/resultados/propostaId/{id}
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