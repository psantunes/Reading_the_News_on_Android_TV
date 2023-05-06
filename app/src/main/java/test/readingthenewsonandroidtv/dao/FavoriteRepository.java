package test.readingthenewsonandroidtv.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import test.readingthenewsonandroidtv.model.News;
import test.readingthenewsonandroidtv.util.Database;

public class FavoriteRepository {
    private final Database database;

    public FavoriteRepository(Context context){
        database = new Database(context);
    }

    public boolean insert(News news) {
        ContentValues value = new ContentValues();
        value.put("ID", news.getId());
        value.put("TITLE", news.getTitle());
        value.put("BGIMAGEURL", news.getArticle());
        value.put("CARDIMAGEURL", news.getCardImageUrl());
        value.put("SOURCE", news.getSource());
        value.put("LINK", news.getLink());
        value.put("PHOTOCREDIT", news.getPhotoCredit());
        value.put("PUBLISHEDAT", news.getPublishedAt());

        long result = database.getConnection().insert("FAVORITES", null, value);
        if (result ==-1) {
            database.close();
            return false;
        }
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
            String stringQuery = "SELECT ID, TITLE, BGIMAGEURL, CARDIMAGEURL, SOURCE, " +
                    "LINK, PHOTOCREDIT, PUBLISHEDAT" +
                    "FROM FAVORITES " +
                    "ORDER BY PUBLISHEDAT ASC";

            Cursor cursor = database.getConnection().rawQuery(stringQuery, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                News news = new News();
                news.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                news.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
                news.setBgImageUrl(cursor.getString(cursor.getColumnIndex("BGIMAGEURL")));
                news.setCardImageUrl(cursor.getString(cursor.getColumnIndex("CARDIMAGEURL")));
                news.setSource(cursor.getString(cursor.getColumnIndex("SOURCE")));
                news.setLink(cursor.getString(cursor.getColumnIndex("LINK")));
                news.setPhotoCredit(cursor.getString(cursor.getColumnIndex("PHOTOCREDIT")));
                news.setPublishedAt(cursor.getString(cursor.getColumnIndex("PUBLISHEDAT")));
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

    public boolean checkIfIsFavorite(int id){
        Cursor cursor =  database.getConnection().rawQuery("SELECT 1 FROM FAVORITES WHERE ID = " + id, null);
        boolean isFavorite = (cursor.getCount() > 0);
        cursor.close();
        database.close();

        return isFavorite;
    }
}
