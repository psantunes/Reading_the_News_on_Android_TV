package test.readingthenewsonandroidtv;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import test.readingthenewsonandroidtv.dao.FavoriteRepository;
import test.readingthenewsonandroidtv.databinding.ActivityNewsHBinding;
import test.readingthenewsonandroidtv.databinding.ActivityNewsVBinding;
import test.readingthenewsonandroidtv.model.News;
import test.readingthenewsonandroidtv.model.NewsList;
import test.readingthenewsonandroidtv.util.Mode;

public class NewsActivity extends FragmentActivity {
    private static final String TAG = "NewsActivity";
    public static final String NEWS = "news";
    public static final String SOURCE = "source";
    public static final String MODE = "mode";

    private List<News> list;
    private News selectedNews;
    private boolean isFavorite;
    private int newsNumber;
    private int nextNews;
    private int source;
    private Mode mode;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "method onCreate");
        super.onCreate(savedInstanceState);

        View view;
        source = (int) getIntent().getSerializableExtra(NewsActivity.SOURCE);
        mode = (Mode) getIntent().getSerializableExtra(NewsActivity.MODE);
        Log.d(TAG, "Mode = " + mode.toString());

        if (source == 0) {
            Log.d(TAG, "Loading data from JSON file");
            loadNewsFromServer();
            newsNumber = (int) getIntent().getSerializableExtra(NewsActivity.NEWS);
            selectedNews = list.get(newsNumber);
            FavoriteRepository favoriteRepository = new FavoriteRepository(this);
            isFavorite = favoriteRepository.checkIfIsFavorite(selectedNews.getId());
        } else {
            Log.d(TAG, "Loading data from SQLite");
            loadNewsFromDatabase();
            int newsId = (int) getIntent().getSerializableExtra(NewsActivity.NEWS);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == newsId) {
                    newsNumber = i;
                }
            selectedNews = list.get(newsNumber);
            }
        }

        if (mode == Mode.kyosk) {
            counter();
            changeBackTvButton();
        }

        if (selectedNews.getOrientation().equals("vertical")) {
            Log.d(TAG, "Layout with vertical picture");
            ActivityNewsVBinding bindingV = DataBindingUtil.setContentView(this, R.layout.activity_news_v);
            bindingV.setNews(selectedNews);
            bindingV.setImageUrl(selectedNews.getBgImageUrl());
            bindingV.setCredits("Fonte: " + selectedNews.getSource() + "\nFoto: " + selectedNews.getPhotoCredit());
            view = bindingV.getRoot();
        } else {
            Log.d(TAG, "Layout with horizontal picture");
            ActivityNewsHBinding bindingH = DataBindingUtil.setContentView(this, R.layout.activity_news_h);
            bindingH.setNews(selectedNews);
            bindingH.setImageUrl(selectedNews.getBgImageUrl());
            bindingH.setCredits("Fonte: " + selectedNews.getSource() + "\nFoto: " + selectedNews.getPhotoCredit());
            view = bindingH.getRoot();
        }

        verifyModeAndLoadNavigationButtons();
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
            Log.d(TAG, "Id of nextNews is " + nextNews);
            return false;
        }
    }

    public void verifyModeAndLoadNavigationButtons() {
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        View menu = findViewById(R.id.menu);

        if (mode == Mode.navigation) {
            if (source == 1 || isFavorite) {
                button2.setText(getString(R.string.remove_favorite));
            }
            navButtons();
        } else {
            button1.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button3.setVisibility(View.GONE);
            button4.setVisibility(View.GONE);
            menu.setVisibility(View.GONE);
        }
    }

    public void navButtons() {
        Log.d(TAG, "method buttonFunctions");

        // Button 1 - previous
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        // Button 2 - add or remove favorites
        button2.setOnClickListener(v -> {
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
        });

        // Button 3 - external_link
        button3.setOnClickListener(v -> {
            Log.d(TAG, "Clicked on button 3 - read the news");
            try {
                Uri webpage = Uri.parse(selectedNews.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Para ler a notícia, você precisa ter um navegador instalado", Toast.LENGTH_SHORT).show();
            }
        });

        // Button 4 - next
        if (checkIfItLast()) {
            button4.setVisibility(View.GONE);
        }
        else {
            button4.setOnClickListener(v -> {
                Log.d(TAG, "Clicked on button 4 - Next");
                Intent nextIntent = new Intent(v.getContext(), NewsActivity.class);
                nextIntent.putExtra(NewsActivity.SOURCE, source);
                nextIntent.putExtra(NewsActivity.MODE, Mode.navigation);
                if (source == 0) {
                    nextIntent.putExtra(NewsActivity.NEWS, newsNumber+1);
                } else {
                    nextIntent.putExtra(NewsActivity.NEWS, nextNews);
                    Log.d(TAG, "next news from source 1 is " + nextNews);
                }
                startActivity(nextIntent);
            });
        }
    }

    public void counter() {
        Log.d(TAG, "start the 12 seconds counter");
        int delay = 12000;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Intent nextIntent = new Intent(getApplicationContext(), NewsActivity.class);
                nextIntent.putExtra(NewsActivity.MODE, Mode.kyosk);
                nextIntent.putExtra(NewsActivity.SOURCE, source);
                if (checkIfItLast()) {
                    nextIntent.putExtra(NewsActivity.NEWS, 0);
                } else {
                    nextIntent.putExtra(NewsActivity.NEWS, newsNumber + 1);
                }
                startActivity(nextIntent);
            }
        }, delay);
    }

    // Change the behavior of OnBackPressedCallback when in kyosk mode
    public void changeBackTvButton() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                timer.cancel();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }
}