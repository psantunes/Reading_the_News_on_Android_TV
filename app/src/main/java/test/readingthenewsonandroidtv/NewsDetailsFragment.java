package test.readingthenewsonandroidtv;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.OnActionClickedListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import test.readingthenewsonandroidtv.dao.FavoriteRepository;
import test.readingthenewsonandroidtv.model.News;
import test.readingthenewsonandroidtv.model.NewsList;

/*
 * LeanbackDetailsFragment extends DetailsFragment, a Wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its meta plus related videos.
 */
public class NewsDetailsFragment extends DetailsSupportFragment {
    private static final String TAG = "NewsDetailsFragment";

    private static final int HOME = 1;
    private static final int ADD_FAVORITE = 2;
    private static final int EXTERNAL_LINK = 3;
    private static final int NEXT = 4;
    private static final int REMOVE_FAVORITE = 5;

    private static final int DETAIL_THUMB_WIDTH = 600;
    private static final int DETAIL_THUMB_HEIGHT = 600;

    private List<News> list;
    private News mSelectedNews;
    private int newsNumber;
    private int source;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private DetailsSupportFragmentBackgroundController mDetailsBackground;

    private Boolean isFavorite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        source = (int) getActivity().getIntent().getSerializableExtra(DetailsActivity.SOURCE);
        if (source == 0) {
            Log.w(TAG, "Take news from JSON");
            loadNewsFromServer();
            newsNumber = (int) getActivity().getIntent().getSerializableExtra(DetailsActivity.NEWS);
        } else {
            Log.w(TAG, "Take news from SQLite");
            loadNewsFromDatabase();

            News takeNews = (News) getActivity().getIntent().getSerializableExtra(DetailsActivity.NEWS);

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId() == takeNews.getId()) {
                    newsNumber = i;
                }
            }
        }

        mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);

        mSelectedNews = list.get(newsNumber);
        //  (News) getActivity().getIntent().getSerializableExtra(DetailsActivity.NEWS);
        if (mSelectedNews != null) {
            mPresenterSelector = new ClassPresenterSelector();
            mAdapter = new ArrayObjectAdapter(mPresenterSelector);

            verifyFavorites();
            // function setupDetailsOverviewRow() is called inside verify favorites
            //setupDetailsOverviewRow(false);

            setupDetailsOverviewRowPresenter();
            setAdapter(mAdapter);
            initializeBackground(mSelectedNews);
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
}

    private void initializeBackground(News data) {
        mDetailsBackground.setParallaxDrawableMaxOffset(10);
        mDetailsBackground.enableParallax();
        Glide.with(getActivity())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.default_background)
                .load(data.getBgImageUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap,
                                                @Nullable Transition<? super Bitmap> transition) {
                        mDetailsBackground.setCoverBitmap(bitmap);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });
    }

    private void setupDetailsOverviewRow(Boolean isFavorite) {
        Log.d(TAG, "doInBackground: " + mSelectedNews.toString());
        final DetailsOverviewRow row = new DetailsOverviewRow(mSelectedNews);
        row.setImageDrawable(
                ContextCompat.getDrawable(getContext(), R.drawable.default_background));
        int width = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_WIDTH);
        int height = convertDpToPixel(getActivity().getApplicationContext(), DETAIL_THUMB_HEIGHT);
        Glide.with(getActivity())
                .load(mSelectedNews.getCardImageUrl())
                .centerCrop()
                .error(R.drawable.default_background)
                .into(new SimpleTarget<Drawable>(width, height) {
                    @Override
                    public void onResourceReady(@NonNull Drawable drawable,
                                                @Nullable Transition<? super Drawable> transition) {
                        Log.d(TAG, "details overview card image url ready: " + drawable);
                        row.setImageDrawable(drawable);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });

        ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

        actionAdapter.add(
                new Action(
                        HOME,
                        getResources().getString(R.string.back_1)));

        if (isFavorite) {
            actionAdapter.add(
                    new Action(
                            REMOVE_FAVORITE,
                            getResources().getString(R.string.remove_favorite)));
        } else {
            actionAdapter.add(
                    new Action(
                            ADD_FAVORITE,
                            getResources().getString(R.string.add_favorite)));
        }

        actionAdapter.add(
                new Action(
                        EXTERNAL_LINK,
                        getResources().getString(R.string.external_link)));

        // dont show if is the last news
        if (!checkIfItLast()) {
            actionAdapter.add(
                    new Action(
                            NEXT,
                            getResources().getString(R.string.next_1)));
        }

        row.setActionsAdapter(actionAdapter);
        mAdapter.add(row);
    }

    private void setupDetailsOverviewRowPresenter() {
        // Set detail background.
        FullWidthDetailsOverviewRowPresenter detailsPresenter =
                new FullWidthDetailsOverviewRowPresenter(new NewsDetailsDescriptionPresenter());
        detailsPresenter.setBackgroundColor(
                ContextCompat.getColor(getContext(), R.color.selected_background));

        // Hook up transition element.
        FullWidthDetailsOverviewSharedElementHelper sharedElementHelper =
                new FullWidthDetailsOverviewSharedElementHelper();
        sharedElementHelper.setSharedElementEnterTransition(
                getActivity(), DetailsActivity.SHARED_ELEMENT_NAME);
        detailsPresenter.setListener(sharedElementHelper);
        detailsPresenter.setParticipatingEntranceTransition(true);
        detailsPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            @Override
            public void onActionClicked(Action action) {
                if (action.getId() == EXTERNAL_LINK) {
                    try {
                    Uri webpage = Uri.parse(mSelectedNews.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getActivity(), "Para ler a notícia, você precisa ter um navegador instalado", Toast.LENGTH_SHORT).show();
                    }
                } else if (action.getId() == HOME) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                } else if (action.getId() == ADD_FAVORITE) {
                    FavoriteRepository favoriteRepository = new FavoriteRepository(getActivity());
                    boolean saveNews = favoriteRepository.insert(mSelectedNews);
                    if (saveNews) {
                        Toast.makeText(getActivity(),
                                "Notícia gravada com sucesso",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(),
                                "Erro ao gravar a notícia",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (action.getId() == REMOVE_FAVORITE) {
                    FavoriteRepository favoriteRepository = new FavoriteRepository(getActivity());
                    int deleteNews = favoriteRepository.delete(mSelectedNews.getId());
                    if (deleteNews > 0) {
                        Toast.makeText(getActivity(),
                                "Favorito removido com sucesso",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(),
                                "Erro ao remover o favorito",
                                Toast.LENGTH_SHORT).show();
                    }
                } else if (action.getId() == NEXT) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    newsNumber++;
                    if (source == 0) {
                        intent.putExtra(DetailsActivity.NEWS, newsNumber);
                        intent.putExtra(DetailsActivity.SOURCE, 0);
                    } else {
                        intent.putExtra(DetailsActivity.NEWS, list.get(newsNumber));
                        intent.putExtra(DetailsActivity.SOURCE, 1);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), action.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mPresenterSelector.addClassPresenter(DetailsOverviewRow.class, detailsPresenter);
    }

    private int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private boolean checkIfItLast() {
        int pos = 0;
        for (News news : list) {
            if (news.getId() == mSelectedNews.getId()) {
                pos = list.indexOf(news);
                break;
            }
        }
        return (pos == (list.size() - 1));
    }

    // call NewsList. thread is used to delay application until list is load
    public void loadNewsFromServer() {
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
        try {
            FavoriteRepository favoriteRepository = new FavoriteRepository(getActivity());
            Thread t2 = new Thread(() -> list = favoriteRepository.getAll());
            t2.start();
            t2.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void verifyFavorites() {
        isFavorite = false;
        FavoriteRepository favoriteRepository = new FavoriteRepository(getActivity());
        isFavorite = favoriteRepository.checkIfIsFavorite(mSelectedNews.getId());

        setupDetailsOverviewRow(isFavorite);
    }
}
