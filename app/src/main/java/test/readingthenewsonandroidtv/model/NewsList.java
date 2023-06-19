package test.readingthenewsonandroidtv.model;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import test.readingthenewsonandroidtv.util.Setup;

/**
 * The class NewsList processes a list of news received from an external source in format JSON
 */
public final class NewsList {
    private static final String TAG = "NewsList";
    private static List<News> list;
    private static int count = 0;

    public static List<News> getNewsList() {
        Log.d(TAG, "method getNewsList");
        if (list == null) {
            setupNewsOnline();
        }
        return list;
    }

    public static List<News> setupNewsOnline() {
        Log.d(TAG, "method setupNewsOnline");
        list = new ArrayList<>();
        try
        {
            URL jsonUrl = new URL(Setup.JSON_URL);
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            try {
                News[] newsListFromJson = mapper.readValue(jsonUrl, News[].class);
                for (News news : newsListFromJson) {
                    Log.d(TAG, news.toString());
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
}