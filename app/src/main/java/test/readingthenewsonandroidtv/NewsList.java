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
                "Notícia número 1",
                "Google Demo Slam_ 20ft Search",
                "Introducing Google Nose"
        };

        String description = "Texto de notícias número 1. "
                + "Donec tristique, orci sed semper lacinia, quam erat rhoncus massa, non congue tellus est "
                + "facilisis mattis. Ut aliquet luctus lacus. Phasellus nec commodo erat. Praesent tempus id "
                + "lectus ac scelerisque. Maecenas pretium cursus lectus id volutpat.";
        String studio[] = {
                "Jornal do Comércio", "Studio One", "Studio Two", "Studio Three", "Studio Four"
        };
        String videoUrl[] = {
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search.mp4",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose.mp4"
        };
        String bgImageUrl[] = {
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg",
        };
        String cardImageUrl[] = {
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/card.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/card.jpg"
        };

        for (int index = 0; index < title.length; ++index) {
            list.add(
                    buildNews(
                            title[index],
                            description,
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