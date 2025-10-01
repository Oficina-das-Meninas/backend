# Oficina das Meninas🌸

Sistema desenvolvido para modernizar a gestão da **Oficina das Meninas**, oferecendo novas funcionalidades para ampliar o apoio à ONG e aumentar seu impacto social.

## Sobre a ONG

Fundada em 2002 por **Adélia Bellodi Privato**, a Oficina das Meninas acolhe e oferece oportunidades a meninas de 6 a 17 anos em situação de vulnerabilidade social.

#### Impacto Social:

- 65+ meninas assistidas até o momento

- 3 parceiros que apoiam a ONG

- 20+ anos de atuação transformando vidas

## Índice
1. [Visão Geral](#visão-geral)  
2. [Tecnologias Utilizadas](#tecnologias-utilizadas)  
3. [Estrutura do Projeto](#estrutura-do-projeto)  
4. [Licença](#licença)  
5. [Autores](#autores)  

## Visão Geral

O projeto moderniza o sistema da ONG com foco em transparência, engajamento e eficiência.  
Entre os principais recursos estão:  

- **Apadrinhamento**: contribuições recorrentes para apoiar a ONG.  
- **Portal da Transparência**: acesso público a documentos e relatórios.  
- **Divulgação de Eventos**: corridas, ações sociais e campanhas.  
- **Guia do Voluntário e Doações**: tutoriais sobre como ajudar via voluntariado, imposto de renda ou nota fiscal.  

Essas funcionalidades fortalecem a missão da ONG de acolher e transformar vidas.  

---

## Tecnologias Utilizadas

- **Java 21** – Linguagem principal  
- **Maven** – Gerenciador de dependências e build  
- **Spring Boot** – Framework para aplicações web e APIs REST  
- **MinIO** – Armazenamento de arquivos (Object Storage)  
- **JWT (JSON Web Token)** – Autenticação baseada em token  
- **PostgreSQL** – Banco de dados relacional  
- **JDBC** – Conector de banco de dados  

---

## Estrutura do Projeto📁

```
oficinadasmeninas/
├── domain/
│   ├── [domain]/
│   │   ├── dto/             # Objetos de Transferência de Dados
│   │   ├── mapper/          # Mapeadores de objetos
│   │   ├── repository/      # Interfaces de repositórios
│   │   ├── service/         # Interfaces de serviços
│   │   ├── .java            # Classe(s) do domínio
│   ├── ObjectStorage/       # Interface do MinIO
├── infra/
│   ├── auth/                # Autenticação JWT
│   ├── config/              # Configurações do Spring Boot
│   ├── ObjectStorage/       # Configuração do MinIO
│   ├── [infra]/
│   │   ├── repository/      # Implementação dos repositórios
│   │   ├── service/         # Implementação dos serviços
├── presentation/
│   ├── controller/          # Controladores REST
└── resources/               # Recursos estáticos e arquivos de configuração de ambiente
```

---

## Licença

-

---


## Autores

<div style="display: flex; flex-wrap: wrap; gap: 32px; justify-content: center;">
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/64173743?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/gutsserrano"><b>Augusto</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/99999453?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/caioslopes"><b>Caio</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/127906505?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/CauaDeSa"><b>Cauã</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/110670578?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/edenilsonjunior"><b>Edenilson</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/94545632?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/DuhCarvalho05"><b>Eduardo</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/106879291?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/roberttiss"><b>Gabriel</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/64981353?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/giovanatrevizanbarbosa"><b>Giovana</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/85528622?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/RICKBISPO"><b>Henrique</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/66184416?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/igorcardosoy"><b>Igor</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/56651735?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/LucasAlt40"><b>Lucas</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/141193412?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/LizzBricce"><b>Maria</b></a>
  </div>
  <div style="text-align: center; width: 120px;">
    <img src="https://avatars.githubusercontent.com/u/121053250?v=4" width="120" height="120" style="border-radius: 50%"><br>
    <a href="https://github.com/natansalvadorligabo"><b>Natan</b></a>
  </div>
</div>
