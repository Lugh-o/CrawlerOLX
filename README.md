# Web Crawler OLX

Webcrawler que coleta dados da [OLX](https://www.olx.com.br/). O programa por padrão faz uma busca por Iphones 11 na região de Goiás (DDD 62), mas os critérios da busca são configuráveis. O resultado da busca é salvo localmente em um arquivo CSV e enviados por email (foi utilizado o [Brevo](https://app-smtp.brevo.com) via SMTP) para a lista de transmissão armazenada em banco de dados. Essa lista de emails por sua vez, pode ser alterada por meio de uma aplicação auxiliar.

## Requisitos
- JVM 8+
- Groovy 4.0.6
- Gradle 6.9
- PostgreSQL

## Como executar

Após o clone, crie um arquivo .env baseado no .env.example e insira as credenciais do seu banco de dados, e as credenciais do STMP escolhido.

```
cp .env.example .env
```

Para executar a migração inicial do banco de dados:

```
./gradlew migrate
```

Para compilar o projeto:

```
./gradlew build
```

Para rodar:

```
./gradlew run
```

Para rodar a aplicação CLI para gestão da lista de emails:

```
./gradlew emailList
```

Para rodar os testes unitários:

```
./gradlew test
```

## Licença

Este projeto é livre para uso pessoal e acadêmico. Sinta-se à vontade para clonar, modificar e melhorar.