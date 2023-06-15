package test.readingthenewsonandroidtv;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import test.readingthenewsonandroidtv.dao.FavoriteRepository;
import test.readingthenewsonandroidtv.model.News;
import test.readingthenewsonandroidtv.util.Mode;

public class FavoriteFragment extends BrowseSupportFragment {
    private static final String TAG = "FavoriteFragment";

    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;

    private final Handler mHandler = new Handler();
    private DisplayMetrics mMetrics;
    private BackgroundManager mBackgroundManager;
    private List<News> list;

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setHeadersState(HEADERS_DISABLED);

        prepareBackgroundManager();

        setupUIElements();

        loadRows();

        setOnItemViewClickedListener(new ItemViewClickedListener());

    }

    private void loadRows() {
        FavoriteRepository favoriteRepository = new FavoriteRepository(getActivity());
        list = favoriteRepository.getAll();
        Log.i(TAG, "Favorites tem " + list.size() + " elementos");

        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);

        for (int i = 0; i < list.size(); i++) {
            listRowAdapter.add(list.get(i));
        }

        HeaderItem header = new HeaderItem(0, getString(R.string.favorites));
        rowsAdapter.add(new ListRow(header, listRowAdapter));
        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        Drawable mDefaultBackground = ContextCompat.getDrawable(getContext(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getContext(), R.color.fastlane_background));

    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            News news = (News) item;
            Log.d(TAG, "Clicked on " + news.getTitle());
            Intent intent = new Intent(getActivity(), NewsActivity.class);
            intent.putExtra(NewsActivity.NEWS, news.getId());
            intent.putExtra(NewsActivity.SOURCE, 1);
            intent.putExtra(NewsActivity.MODE, Mode.navigation);
            startActivity(intent);
        }
    }


    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getContext(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }
}