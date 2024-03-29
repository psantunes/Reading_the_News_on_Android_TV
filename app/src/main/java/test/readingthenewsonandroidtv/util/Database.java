package test.readingthenewsonandroidtv.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE = "READING_THE_NEWS";
    private static final int VERSION = 4;

    public Database(Context context){
        super(context, DATABASE,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = " CREATE TABLE FAVORITES (" +
                "ID INTEGER PRIMARY KEY, " +
                "ARTICLE TEXT NOT NULL," +
                "TITLE TEXT NOT NULL," +
                "BGIMAGEURL TEXT NOT NULL," +
                "CARDIMAGEURL TEXT NOT NULL," +
                "SOURCE TEXT NOT NULL," +
                "LINK TEXT NOT NULL," +
                "PHOTOCREDIT TEXT NOT NULL," +
                "PUBLISHEDAT TEXT NOT NULL," +
                "ORIENTATION TEXT NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS FAVORITES");
        onCreate(db);

    }
    public SQLiteDatabase getConnection(){
        return this.getWritableDatabase();
    }

}
