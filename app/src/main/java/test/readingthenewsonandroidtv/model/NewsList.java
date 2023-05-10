package test.readingthenewsonandroidtv.model;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NewsList {
    private static final String TAG = "NewsList";
    private static final String JSON_URL = "https://raw.githubusercontent.com/psantunes/Reading_the_News_on_Android_TV/main/docs/json_file_example.json";
    private static List<News> list;
    private static int count = 0;

    public static List<News> getNewsList() {
        if (list == null) {
            setupNewsOnline();
        }
        return list;
    }

    public static List<News> setupNewsOnline() {
        list = new ArrayList<>();
        try
        {
            URL jsonUrl = new URL("https://raw.githubusercontent.com/psantunes/Reading_the_News_on_Android_TV/main/docs/json_file_example.json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            try {
                News[] newsListFromJson = mapper.readValue(jsonUrl, News[].class);
                for (News news : newsListFromJson) {
                    System.out.println(news);
                    list.add(news);
                    System.out.println("Item adicionado na lista");
                }
            } catch (IOException io) {
                Log.i("Exception", io.getMessage());
            }
            return list;
        }
        catch (Exception e)
        {
            Log.i("Exception", e.getMessage());
            return null;
        }
    }

    public static List<News> setupNewsManual(){
        list = new ArrayList<>();
        String json = "[" +
                "{" +
                "\"id\": \"1\",\n" +
                "\"title\": \"Manchete 1\",\n" +
                "\"article\": \"Mais de 430 pacientes aguardavam internações em Porto Alegre nesta terça-feira, em um cenário de emergências e hospitais da Capital superlotados. A situação, que compromete também as consultas eletivas ou de menor gravidade, é causada em especial por síndromes gripais, segundo o diretor Atenção Hospitalar e Urgências da SMS, Francisco Isaías.\",\n" +
                "\"source\": \"Matinal Jornalismo\",\n" +
                "\"link\": \"https://www.matinaljornalismo.com.br/matinal/newsletter/sindromes-gripais-pressionam-hospitais-de-porto-alegre-deixando-mais-de-400-a-espera-por-internacao/\",\n" +
                "\"publishedAt\": \"2022-05-25\",\n" +
                "\"bgImageUrl\": \"https://teleseries.com.br/wp-content/uploads/2023/03/pucrs_g.jpg\",\n" +
                "\"cardImageUrl\": \"https://teleseries.com.br/wp-content/uploads/2023/03/pucrs_p.jpg\",\n" +
                "\"photoCredit\": \"Bruno Todeschini/Divulgação PUCRS\"\n" +
                "}," +
                "{" +
                "\"id\": \"2\",\n" +
                "\"title\": \"Manchete 2\",\n" +
                "\"article\": \"Mais de 430 pacientes aguardavam internações em Porto Alegre nesta terça-feira, em um cenário de emergências e hospitais da Capital superlotados. A situação, que compromete também as consultas eletivas ou de menor gravidade, é causada em especial por síndromes gripais, segundo o diretor Atenção Hospitalar e Urgências da SMS, Francisco Isaías.\",\n" +
                "\"source\": \"Outra fonte\",\n" +
                "\"link\": \"https://www.matinaljornalismo.com.br/matinal/newsletter/sindromes-gripais-pressionam-hospitais-de-porto-alegre-deixando-mais-de-400-a-espera-por-internacao/\",\n" +
                "\"publishedAt\": \"2022-05-25\",\n" +
                "\"bgImageUrl\": \"https://teleseries.com.br/wp-content/uploads/2023/03/pucrs_g.jpg\",\n" +
                "\"cardImageUrl\": \"https://teleseries.com.br/wp-content/uploads/2023/03/pucrs_p.jpg\",\n" +
                "\"photoCredit\": \"Bruno Todeschini/Divulgação PUCRS\"\n" +
                "}" +
                "]";

        try {
            ObjectMapper mapper = new ObjectMapper();
            //list = mapper.readValue(json, new TypeReference<List<News>>(){});
            list = Arrays.asList(mapper.readValue(json, News[].class));
            return list;
        }
        catch (Exception e)
        {
            Log.i("Exception", e.getMessage());
            return null;
        }
    }
}