package test.readingthenewsonandroidtv.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.readingthenewsonandroidtv.model.News;
import test.readingthenewsonandroidtv.util.Database;

public class FavoriteRepository {
    private static final String TAG = "FavoriteRepository";
    private final Database database;

    public FavoriteRepository(Context context){
        database = new Database(context);
    }

    public boolean insert(News news) {
        Log.d(TAG, "Insert favorite");

        ContentValues value = new ContentValues();
        value.put("ID", news.getId());
        value.put("TITLE", news.getTitle());
        value.put("ARTICLE", news.getArticle());
        value.put("BGIMAGEURL", news.getBgImageUrl());
        value.put("CARDIMAGEURL", news.getCardImageUrl());
        value.put("SOURCE", news.getSource());
        value.put("LINK", news.getLink());
        value.put("PHOTOCREDIT", news.getPhotoCredit());
        value.put("PUBLISHEDAT", String.valueOf(news.getPublishedAt()));
        value.put("ORIENTATION", news.getOrientation());

        long result = database.getConnection().insert("FAVORITES", null, value);
        if (result ==-1) {
            database.close();
            return false;
        }
        Log.d(TAG, "Favorite saved successfully");
        return true;
    }

    public Integer delete(int id){
        String[] idReceived = {Integer.toString(id)};
        Integer lines = database.getConnection().delete("FAVORITES","id = ?", idReceived);
        database.close();
        return lines;
    }


    @SuppressLint("Range")
    public List<News> getAll(){
        List<News> newsList = new ArrayList<>();
        try {
            String stringQuery = "SELECT ID, TITLE, ARTICLE, BGIMAGEURL, CARDIMAGEURL, SOURCE, " +
                    "LINK, PHOTOCREDIT, PUBLISHEDAT, ORIENTATION " +
                    "FROM FAVORITES " +
                    "ORDER BY ID ASC";

            Cursor cursor = database.getConnection().rawQuery(stringQuery, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                News news = new News();
                news.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                news.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
                news.setArticle(cursor.getString(cursor.getColumnIndex("ARTICLE")));
                news.setBgImageUrl(cursor.getString(cursor.getColumnIndex("BGIMAGEURL")));
                news.setCardImageUrl(cursor.getString(cursor.getColumnIndex("CARDIMAGEURL")));
                news.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));
                news.setLink(cursor.getString(cursor.getColumnIndex("LINK")));
                news.setPhotoCredit(cursor.getString(cursor.getColumnIndex("PHOTOCREDIT")));
                // DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(cursor.getLong(cursor.getColumnIndex("PUBLISHEDAT")));
                news.setPublishedAt(date);
                news.setOrientation(cursor.getString(cursor.getColumnIndex("ORIENTATION")));
                newsList.add(news);
                cursor.moveToNext();
            }

            cursor.close();
            database.close();

            return newsList;
        }
        catch (Exception e) {
            Log.i("Exception", e.getMessage());
            return null;
        }
    }
}
