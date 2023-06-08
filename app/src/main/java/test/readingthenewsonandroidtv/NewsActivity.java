package test.readingthenewsonandroidtv;

import static java.lang.String.valueOf;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import test.readingthenewsonandroidtv.dao.FavoriteRepository;
import test.readingthenewsonandroidtv.databinding.ActivityNewsBinding;
import test.readingthenewsonandroidtv.model.News;
import test.readingthenewsonandroidtv.model.NewsList;

public class NewsActivity extends FragmentActivity {
    private static final String TAG = "NewsActivity";
    public static final String NEWS = "news";
    public static final String SOURCE = "source";

    private List<News> list;
    private News selectedNews;
    private Boolean isFavorite;
    private int newsNumber;
    private int nextNews;
    private int source;
    private View view;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;

    private ActivityNewsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "method onCreate");
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news);

        source = (int) getIntent().getSerializableExtra(NewsActivity.SOURCE);

        if (source == 0) {
            Log.w(TAG, "Loading data from JSON file");
            loadNewsFromServer();
            newsNumber = (int) getIntent().getSerializableExtra(NewsActivity.NEWS);
        } else {
            Log.w(TAG, "Loading data from SQLite");
            loadNewsFromDatabase();
            int newsId = (int) getIntent().getSerializableExtra(NewsActivity.NEWS);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == newsId) {
                    newsNumber = i;
                }
            }
        }
        selectedNews = list.get(newsNumber);
        binding.setNews(selectedNews);
        binding.setImageUrl(selectedNews.getBgImageUrl());
        binding.setCredits("Fonte: " + selectedNews.getSource() + "\nFoto: " + selectedNews.getPhotoCredit());

        buttons();
        if (source == 1) {
            button2.setText(getString(R.string.remove_favorite));
        }

        view = binding.getRoot();
        setContentView(view);
    }

    // call NewsList. thread is used to delay application until list is load
    public void loadNewsFromServer() {
        Log.d(TAG, "method loadNewsFromServer");
        try {
            Thread t1 = new Thread(() -> list = NewsList.getNewsList());
            t1.start();
            t1.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadNewsFromDatabase() {
        Log.d(TAG, "method loadNewsFromDatabase");
        try {
            FavoriteRepository favoriteRepository = new FavoriteRepository(this);
            Thread t2 = new Thread(() -> list = favoriteRepository.getAll());
            t2.start();
            t2.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initializeBackground(News news) {
        Glide.with(this)
                .asBitmap()
                .centerCrop()
                .load(news.getBgImageUrl())
                .error(R.drawable.default_background)
                .into((ImageView) view);
    }

    private boolean checkIfItLast() {
        Log.d(TAG, "method checkIfItLast");
        int pos = 0;
        for (News news : list) {
            if (news.getId() == selectedNews.getId()) {
                pos = list.indexOf(news);
                break;
            }
        }
        if (pos == (list.size() - 1)) {
            Log.d(TAG, "last news from list");
            return true;
        } else {
            // get next news (used only on list of favorites)
            nextNews = list.get(pos+1).getId();
            Log.d(TAG, "nextNews =" + nextNews);
            return false;
        }
    }

    public void buttons() {
        Log.d(TAG, "render buttons");

        // Button 1 - previous
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        // Button 2 - add or remove favorites
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Clicked on button 2 - Add favorites");
                FavoriteRepository favoriteRepository = new FavoriteRepository(getApplicationContext());

                if (button2.getText() == getString(R.string.add_favorite)) {
                    boolean saveNews = favoriteRepository.insert(selectedNews);
                    if (saveNews) {
                        Toast.makeText(getApplicationContext(),
                                "Notícia gravada com sucesso",
                                Toast.LENGTH_SHORT).show();
                        button2.setText(getString(R.string.remove_favorite));
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Erro ao gravar a notícia",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    int deleteNews = favoriteRepository.delete(selectedNews.getId());
                    if (deleteNews > 0) {
                        Toast.makeText(getApplicationContext(), "Favorito removido com sucesso",
                                Toast.LENGTH_SHORT).show();
                        button2.setText(getString(R.string.add_favorite));
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro ao remover o favorito",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Button 3 - external_link
        button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Clicked on button 3 - read the news");
                try {
                    Uri webpage = Uri.parse(selectedNews.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Para ler a notícia, você precisa ter um navegador instalado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Button 4 - next
        button4 = (Button) findViewById(R.id.button4);
        if (checkIfItLast()) {
            button4.setVisibility(View.GONE);
        }
        else {
            button4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(TAG, "Clicked on button 4 - Next");
                    Intent nextIntent = new Intent(v.getContext(), NewsActivity.class);
                    nextIntent.putExtra(NewsActivity.SOURCE, source);
                    if (source == 0) {
                        nextIntent.putExtra(NewsActivity.NEWS, newsNumber+1);
                    } else {
                        nextIntent.putExtra(NewsActivity.NEWS, nextNews);
                        Log.d(TAG, "next news from source 1 is " + nextNews);
                    }
                    startActivity(nextIntent);
                }
            });
        }
    }
}