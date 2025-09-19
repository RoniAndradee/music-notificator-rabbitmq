# Projeto Music Notificator RabbitMQ - Producer & Consumer

## Integrantes
- Pedro Ueda - RA: 01241090
- Ronielli Andrade - RA: 01241040

---

## Visão geral do projeto
O tema do projeto é alertar quando uma nova música é lançada.

Este projeto demonstra o fluxo **Producer → RabbitMQ → Consumer**.
- A **API Java (Producer)** recebe requisições HTTP POST e envia mensagens para a fila RabbitMQ.
- O **RabbitMQ** gerencia a fila de mensagens.
- O **Consumer Node.js** consome mensagens da fila, salva em memória e em um arquivo `messages.txt`, e disponibiliza via HTTP GET.

---

## Conteúdo deste README
- Como configurar e rodar o Producer (API Java Spring Boot)
- Como iniciar um RabbitMQ local para testes (Docker Compose)
- Como iniciar o Consumer (NodeJS)
- Exemplos de requests

---

## Producer (API Java)

O Producer é uma aplicação Spring Boot localizada em `music-notificator-rabbitmq-producer` que expõe o endpoint HTTP:

- POST http://localhost:8080/musics

Exemplo de payload:
```json
{
 "name": "So Far Away",
  "artist": "Nightmare",
  "album": "Avenged Sevenfold"
}
```

Arquivos e propriedades importantes:
- `music-notificator-rabbitmq-producer/src/main/resources/application.properties` contém propriedades do app. Atualmente inclui:
  - `broker.exchange.name` (ex.: `example.fanout.exchange`)
  - `broker.queue.name` (ex.: `example.queue`)

Pré-requisitos
- Java 21 (conforme `pom.xml`)
- Docker + Docker Compose
- Rede com porta 5672 disponível para AMQP e 15672 para a console do RabbitMQ

Passo a passo: iniciar RabbitMQ (opcional, usando Docker Compose)
1. Entre na pasta do producer:

```cmd
cd music-notificator-rabbitmq-producer
```

2. Suba o serviço RabbitMQ definido em `compose.yaml` (isso traz a imagem com a interface de gerenciamento):

```cmd
docker compose -f compose.yaml up -d
```

- A compose define usuário e senha conforme o arquivo `compose.yaml`:
  - usuário: `myuser`
  - senha: `secret`
- Console do RabbitMQ disponível em: http://localhost:15672 (user: `myuser`, pass: `secret`)

Rodando o Producer localmente (usando o wrapper Maven incluído)

1) Executando diretamente com o wrapper (modo desenvolvimento):

```cmd
cd music-notificator-rabbitmq-producer
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.rabbitmq.host=localhost --spring.rabbitmq.port=5672 --spring.rabbitmq.username=myuser --spring.rabbitmq.password=secret"
```

2) Buildar e executar o JAR:

```cmd
cd music-notificator-rabbitmq-producer
.\mvnw.cmd clean package -DskipTests
java -jar target\music-notificator-0.0.1-SNAPSHOT.jar --spring.rabbitmq.host=localhost --spring.rabbitmq.username=myuser --spring.rabbitmq.password=secret
```

Testando o endpoint (curl)
- Enviar uma música para o Producer:

```cmd
curl -X POST http://localhost:8080/musics -H "Content-Type: application/json" -d "{\"name\":\"So far away\",\"artist\":\"Avenged Sevenfold\",\"album\":\"Nightmare\"}"
```

- Se tudo estiver funcionando, o Producer publica uma mensagem na exchange/queue configurada e o Consumer (Node.js) poderá consumi-la.

### Exemplo de retorno esperado (Producer)
- Resposta HTTP do endpoint POST /musics (corpo retornado pela API):

```http
HTTP/1.1 200 OK
Content-Type: text/plain;charset=UTF-8
Content-Length: 17

Mensagem enviada!
```

> Observação: o controller (`MusicController`) retorna uma string simples `"Mensagem enviada!"` quando a mensagem é enviada com sucesso.

### Como verificar a mensagem no Consumer (Endpoint GET /messages)
1. Após enviar a música ao Producer, verifique o Consumer (supondo que ele esteja rodando em http://localhost:3000):

```cmd
curl http://localhost:3000/messages
```

2. Resposta esperada do Consumer:

- O `consumer.js` armazena cada mensagem recebida como uma string (que contém o JSON serializado). Assim, a resposta será um array de strings. Exemplo:

```json
[
  "{\"id\":\"<uuid>\",\"name\":\"Shape of You\",\"artist\":\"Ed Sheeran\",\"album\":\"Divide\"}"
]
```

3. Verificando o arquivo salvo pelo Consumer:
- O `consumer.js` também anexa cada mensagem em `received_messages.txt`. Verifique esse arquivo no diretório do Consumer para confirmar que a mensagem foi gravada:


4. Troubleshooting rápido:
- Se o endpoint GET `/messages` retornar `[]` (array vazio):
  - Confirme que o Consumer está rodando (procure a mensagem no console "Consumer rodando em http://localhost:3000" e "Aguardando mensagens...").
  - Verifique se o RabbitMQ está acessível e as credenciais/host/queue coincidem com as usadas pelo Producer e pelo Consumer.
  - Verifique a fila e mensagens pela interface do RabbitMQ (http://localhost:15672) para ver se as mensagens saíram do Producer e foram encaminhadas para a fila.

## Consumer (Node.js)

O Consumer é uma aplicação Node.js que se conecta ao RabbitMQ, consome mensagens da fila e as expõe via HTTP.

### Executando o Consumer

1. No diretório do consumer:

```cmd
cd music-notificator-rabbitmq-consumer
```

2. Instale as dependências:

```cmd
npm install express amqplib
```

3. Execute o consumer:

```cmd
node consumer.js
```

O consumer ficará escutando a fila example.queue e salvará todas as mensagens recebidas em memória (messages[]) e no arquivo messages.txt.

### Testando o Consumer

- Endpoint do consumer para ver mensagens: http://localhost:3000/messages

Exemplo de teste usando curl:

```cmd
curl http://localhost:3000/messages
```

---
