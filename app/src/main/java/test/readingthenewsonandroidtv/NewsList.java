package test.readingthenewsonandroidtv;

import java.util.ArrayList;
import java.util.List;

public final class NewsList {
    private static List<News> list;
    private static long count = 0;

    public static List<News> getList() {
        if (list == null) {
            list = setupNews();
        }
        return list;
    }

    public static List<News> setupNews() {
        list = new ArrayList<>();
        String title[] = {
                "Síndromes gripais pressionam hospitais de Porto Alegre",
                "Notícia número 1",
        };

        String description[] = {
                "Mais de 430 pacientes aguardavam internações em Porto Alegre nesta terça-feira, em um cenário de emergências e hospitais da Capital superlotados. A situação, que compromete também as consultas eletivas ou de menor gravidade, é causada em especial por síndromes gripais, segundo o diretor Atenção Hospitalar e Urgências da SMS, Francisco Isaías. \n" +
                "\n" +
                "O quadro se repete na pediatria, porém outro fator que contribui para a superlotação é a falta de vacinação, segundo a chefe do serviço de emergência do Hospital de Clínicas, Patrícia Lago. “É algo que nos preocupa muito”, afirmou ela à RBSTV citando que os casos de pneumonia pneumocócica, que voltaram a afetar crianças da Capital. “Existe um atraso vacinal importante.” \n" +
                "\n" +
                "Segundo ela, a rede de atendimento às crianças ainda sente o reflexo do fechamento do setor materno-infantil do Hospital São Lucas, da PUCRS, dois anos atrás. Neste mês, a Prefeitura abriu 111 leitos pediátricos e informou à reportagem da RBS que agora o foco está concentrado em vagas para adultos.",
                "Texto de notícias número 1. "
                + "Donec tristique, orci sed semper lacinia, quam erat rhoncus massa, non congue tellus est "
                + "facilisis mattis. Ut aliquet luctus lacus. Phasellus nec commodo erat. Praesent tempus id "
                + "lectus ac scelerisque. Maecenas pretium cursus lectus id volutpat."
        };
        String studio[] = {
                "Matinal Jornalismo", "Studio One", "Studio Two", "Studio Three", "Studio Four"
        };
        String videoUrl[] = {
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose.mp4"
        };
        String bgImageUrl[] = {
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg",
        };
        String cardImageUrl[] = {
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/card.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/card.jpg"
        };

        for (int index = 0; index < title.length; ++index) {
            list.add(
                    buildNews(
                            title[index],
                            description[index],
                            studio[index],
                            videoUrl[index],
                            cardImageUrl[index],
                            bgImageUrl[index]));
        }

        return list;
    }

    private static News buildNews(
            String title,
            String description,
            String studio,
            String videoUrl,
            String cardImageUrl,
            String backgroundImageUrl) {
        News news = new News();
        news.setId(count++);
        news.setTitle(title);
        news.setDescription(description);
        news.setStudio(studio);
        news.setCardImageUrl(cardImageUrl);
        news.setBackgroundImageUrl(backgroundImageUrl);
        news.setVideoUrl(videoUrl);
        return news;
    }
}