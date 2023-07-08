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

## Como instalar

1. Faça o download do aplicativo e abra ele no Android Studio (versão 11 ou superior)
2. Acesse o arquivo *app/src/main/java/util/Setup.java* e altere ali as configurações básicas da ferramenta

* APP_NAME - nome do aplicativo
* APP_DESCRIPTION - uma breve descrição que aparecerá na primeira tela
* LOGO - logomarca que identifica a aplicação, que deverá ser armazenada na pasta app/src/main/res/drawable
* JSON_URL - URL onde a aplicação deve puxar o arquivo Json com notícias ()

3. As principais cores da aplicação podem ser alteradas em *app/src/main/res/values/colors.xml*
4. Caso a intenção seja publicar o aplicativo na Google Play será necessário também providenciar banners e ícones para a aplicação e alterado no arquivo *AndroidManifest.xml* os campos abaixo listados:

   * android:banner
   * android:icon
   * android:logo
   * android:label

5. Se desejar, altere ainda os textos da aplicação em *app/src/main/res/values/strings.xml*
6. Acesse o arquivo *app/build.gradle* e altere o parâmetro applicationId de "test.readingthenewsonandroidtv2" para o nome de identificação do app da sua preferência. Anote este nome

7. Crie uma conta no Firebase ou acesse uma conta já existente
8. Crie um novo projeto no Firebase
9. Habilite a ferramenta Authentication, apenas com login Anônimo
10. Na página inicial do projeto, clique na opção para adicionar um projeto "Android"
11. Na tela seguinte, no campo "Nome do pacote Android" digite o mesmo nome de identificação do app que você inseriu no passos 6. Se desejar dê um apelido ao app e clique em "Registrar App"
12. Ao registrar o app, um arquivo chamado google-services.json será criado e baixado no seu computador.
13. Copie o arquivo para a pasta /app da aplicação
14. Sincronize o gradle e dê um build na aplicação. Se tudo der certo ela irá funcionar automaticamente

**Dica:** Se preferir, faça os passos 7 a 14 seguindo o [tutorial de configuração do Firebase]<https://firebase.google.com/docs/android/setup?hl=pt-br#console>. O tutorial do Google inclui ainda passos para a configuração do plugin no app, mas estas referêncuas já foram inseridos no projeto e não precisam ser feitas.

## Configuração do arquivo JSON

Para funcionamento do sistema é necessário providenciar um arquivo JSON contendo uma lista do objeto News. Um modelo do arquivo está disponível na pasta *docs/json_file_example.json*. Os campos abaixo são esperados:

    * id - inteiro, identificador único da notícia 
    * title - string, o título da notícia 
    * article - string, o resumo da notícia 
    * bgImageUrl - string, URL da imagem em tamanho grande que ilustra a notícia (o sistema foi testado com imagens horizontais com 1280x360 pixels e imagens horizontais de 640x720 pixels.
    * cardImageUrl - string, URL da imagem para o card que ilustra a página que exibe as notícias favoritas. Prefira imagens com proporção 16:9 com 400x225 pixels ou mais.
    * source - string, a fonte da notícia
    * link - string, a URL com o texto integral da notícia
    * photoCredit - string, o nome do autor da imagem
    * publishedAt - data, data de publicação do texto, em formato YYYY-MM-DD. Esta versão do texto não trata hora da imagem
    * orientation - string, define o template do post. São esperados o valor “horizontal” se a imagem for horizontal e “vertical” se a imagem for vertical. 

Recomenda-se que o JSON liste as notícias em ordem crescente de id e que a cada atualização na lista de notícias, as notícias antigas sejam removidas

## Ficha Técnica

Software desenvolvido por Antonio Paulo Serpa Antunes ([psantunes](https://github.com/psantunes)) como trabalho de conclusão do curso de Tecnologia em Sistemas para Internet do Instituto Federal de Educação, Ciência e Tecnologia do Rio Grande do Sul (IFRS) - Campus Porto Alegre. O trabalho teve orientação dos professores Fabio Yoshimitsu Okuyama e Silvia de Castro Bertagnolli.

Para contatar o autor, escreva para pauloserpaantunes@gmail.com.
