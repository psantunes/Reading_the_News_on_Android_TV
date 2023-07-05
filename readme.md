O Reading the News on Android TV é um framework para a construção de apps para a visualização e leitura de resumos de notícias em Smart TVs.

O framework é uma prova de conceito, desenvolvido ao longo de dois anos no curso de Tecnologia em Sistemas para Internet do IFRS - Campus Porto Alegre.

## Telas da aplicação

!["Tela de abertura](/docs/img_1.png "Tela de abertura").

!["Tela de notícia rolada](/docs/img_2.png "Tela de notícia com foto vertical").

![Tela de notícia rolada](/docs/img_3.png "Tela de notícia com foto horizontal").

!["Tela de favoritos](/docs/img_4.png "Tela de favoritos").

## O que o sistema faz

- Login anônimo de usuários usando Firebase
- Acessa um documento JSON externo com resumos de notícias
- Permite a navegação sequencial entre as notícias
- Grava as notícias favoritas do usuário localmente
- Permite a leitura do texto integral da notícias caso o dispositivo Android tenha um navegador habilitado
- Possui dois layouts para exibição da notícia, com foto vertical e foto horizontal
- Permite a navegação em modo quiosque, com as notícias se sucedendo em loop a cada 12 segundos

## Tecnologia

A aplicação usa as seguintes tecnologias:

- Java 8
- Android Studio Default JDK 11.0.2
- Biblioteca Android versão 7.1.2 (API 25, Nougat)
- Biblioteca Leanback 1.0.0
- Autenticação anônima de usuários com Firebase
- SQLite
- Jackson

## Como instalar (nova versão em breve)

1. Faça o download do aplicativo e abra ele no Android Studio (versão 11 ou superior)
2. Crie uma conta no Firebase ou acesse uma conta já existente
3. Crie um novo projeto no Firebase
4. Habilite a ferramenta Authentication, apenas com login Anônimo
5. (...)

## Ficha Técnica

Software desenvolvido por Antonio Paulo Serpa Antunes ([psantunes](https://github.com/psantunes) ) como trabalho de conclusão do curso de Tecnologia em Sistemas para Internet do Instituto Federal de Educação, Ciência e Tecnologia do Rio Grande do Sul (IFRS) - Campus Porto Alegre. O trabalho teve orientação dos professores Fabio Yoshimitsu Okuyama e Silvia de Castro Bertagnolli.
Para contatar o autor, escreva para pauloserpaantunes@gmail.com.