O objetivo deste projeto é estudar as possibilidades de usos para Smart TVs, imaginando uma aplicação que exiba notícias na tela de um dispositivo que rode Android TV.

O projeto foi desenvolvimento no âmbito da disciplina de Programação para Web III, do curso de Tecnologia em Sistemas para Internet do IFRS, e está sendo aprimorada como trabalho de conclusão de curso.

## Telas da aplicação

!["Tela de abertura](/docs/tela1.png "Tela de abertura").

!["Tela de notícia rolada](/docs/tela2.png "Tela de notícia").

![Tela de notícia rolada](/docs/tela3.png "Tela de notícia rolada").

!["Tela de favoritos](/docs/tela4.png "Tela de favoritos").

## O que o sistema faz

- Login anônimo de usuários
- Acessa um banco de dados externo com resumos de notícias
- Permite a navegação sequêncial entre notícias
- Grava as notícias favoritas do usuário num dispositivo externo
- Permite a leitura da notícia (texto integral) - caso o dispositivo Android tenha um navegador habilitado

## Tecnologia

A aplicação usa as seguintes tecnologias:

- Android Studio Default JDK 11.0.2
- Biblioteca Android versão 7.1.2 (API 25, Nougat)
- Biblioteca Leanback
- Autenticação anônima de usuários com Firebase
- Banco de dados não relacional Firebase Realtime Database (para a próxima versão será trocado para um banco de dados relacional

## Como instalar

Para instalação será necessário criar uma conta no Firebase e habilitar com o recursos de autenticação de usuário anônimo.
Em breve será disponibilizado um passos a passo para instalação

## Próximos passos

- Refatorar a aplicação, de forma a usar banco de dados relacional
- Refazer totalmente o layout da tela notícias, criando uma melhor experiência de leitura
- Estudar a possibilidade de alternar layouts
- Criar um modo quiosque, permitindo que as notícias sejam lidas automaticamente, sem usar o controle remoto
- Permitir conteúdo adicional multimídia (áudio e vídeo)