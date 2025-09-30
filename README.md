# Oficina das Meninas

Breve descrição sobre o que o projeto faz e como ele pode beneficiar a ONG. Lembre-se de ser claro e conciso.

## Índice
    1. [Visão Geral] (#visão-geral)
    2. [Tecnologias Utilizadas](#tecnologias-utilizadas)
    3. [Estrutura do Projeto](#pré-requisitos)
    4. [Licença](#licença)
    5. [Contato](#contato)

## Visão Geral

Explique de forma clara o propósito do sistema...

---

## Tecnologias Utilizadas

- Java 21                          \# Linguagem principal
- Maven                            \# Gerenciador de dependências e build
- Spring Boot                      \# Framework para aplicações web e APIs REST
- MinIO                            \# Armazenamento de arquivos (Object Storage)
- JSON Web Token (JWT)             \# Autenticação baseada em token
- PostgreSQL                       \# Banco de dados relacional
- JDBC                             \# Conector para banco de dados

---

## 📁 Estrutura do Projeto 


```
oficinadasmeninas/
├── domain/
│   ├── [domain]/          
│   │       ├── [dto]              # Objeto de Transferência de Dados
│   │       ├── [mapper]           # Mapeadores de objetos
│   │       ├── [repository]       # Interface dos repositórios    
│   │       ├── [service]          # Interface dos serviços
│   │       ├── ObjectStorage/     # Interface do MinIO
├── infra/
│   ├── auth/                      # Configuração de autenticação JWT
│   ├── config/                    # Configurações gerais do Spring Boot
│   ├── ObjectStorage/             # Configuração do MinIO
│   ├── [infra]/        
│   │       ├── [repository]       # Implementação  dos repositórios    
│   │       ├── [service]          # Implementação dos serviços
├── presentation/
│   ├── [controller]   # Controladores REST
└── packages/
```

---

## Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---


## Autores
| [<img src="https://avatars.githubusercontent.com/u/127906505?v=4" width="120" height="120" style="border-radius: 50%"><br>**CauaDeSa**](https://github.com/CauaDeSa) | [<img src="https://avatars.githubusercontent.com/u/99999453?v=4" width="120" height="120" style="border-radius: 50%"><br>**caioslopes**](https://github.com/caioslopes) | [<img src="https://avatars.githubusercontent.com/u/64173743?v=4" width="120" height="120" style="border-radius: 50%"><br>**gutsserrano**](https://github.com/gutsserrano) | [<img src="https://avatars.githubusercontent.com/u/110670578?v=4" width="120" height="120" style="border-radius: 50%"><br>**edenilsonjunior**](https://github.com/edenilsonjunior) |
|:------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------------------------:|
| [<img src="https://avatars.githubusercontent.com/u/94545632?v=4" width="120" height="120" style="border-radius: 50%"><br>**DuhCarvalho05**](https://github.com/DuhCarvalho05) | [<img src="https://avatars.githubusercontent.com/u/64981353?v=4" width="120" height="120" style="border-radius: 50%"><br>**giovanatrevizanbarbosa**](https://github.com/giovanatrevizanbarbosa) | [<img src="https://avatars.githubusercontent.com/u/66184416?v=4" width="120" height="120" style="border-radius: 50%"><br>**igorcardosoy**](https://github.com/igorcardosoy) | [<img src="https://avatars.githubusercontent.com/u/141193412?v=4" width="120" height="120" style="border-radius: 50%"><br>**LizzBricce**](https://github.com/LizzBricce) |
| [<img src="https://avatars.githubusercontent.com/u/56651735?v=4" width="120" height="120" style="border-radius: 50%"><br>**LucasAlt40**](https://github.com/LucasAlt40) | [<img src="https://avatars.githubusercontent.com/u/121053250?v=4" width="120" height="120" style="border-radius: 50%"><br>**natansalvadorligabo**](https://github.com/natansalvadorligabo) | [<img src="https://avatars.githubusercontent.com/u/85528622?v=4" width="120" height="120" style="border-radius: 50%"><br>**RICKBISPO**](https://github.com/RICKBISPO) | [<img src="https://avatars.githubusercontent.com/u/106879291?v=4" width="120" height="120" style="border-radius: 50%"><br>**roberttiss**](https://github.com/roberttiss) |
