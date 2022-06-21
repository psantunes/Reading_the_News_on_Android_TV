package test.readingthenewsonandroidtv;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.DetailsOverviewRow;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.OnActionClickedListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import test.readingthenewsonandroidtv.model.Favorite;

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

    private static final int DETAIL_THUMB_WIDTH = 600;
    private static final int DETAIL_THUMB_HEIGHT = 600;

    private News mSelectedNews;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private DetailsSupportFragmentBackgroundController mDetailsBackground;

    private final List<News> newsList = MainFragment.getNews();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate DetailsFragment");
        super.onCreate(savedInstanceState);

        mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);

        mSelectedNews =
                (News) getActivity().getIntent().getSerializableExtra(DetailsActivity.NEWS);
        if (mSelectedNews != null) {
            mPresenterSelector = new ClassPresenterSelector();
            mAdapter = new ArrayObjectAdapter(mPresenterSelector);
            setupDetailsOverviewRow();
            setupDetailsOverviewRowPresenter();
            setAdapter(mAdapter);
            initializeBackground(mSelectedNews);
            setOnItemViewClickedListener(new ItemViewClickedListener());
        } else {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    private void initializeBackground(News data) {
        mDetailsBackground.enableParallax();
        Glide.with(getActivity())
                .asBitmap()
                .centerCrop()
                .error(R.drawable.default_background)
                .load(data.getBackgroundImageUrl())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap,
                                                @Nullable Transition<? super Bitmap> transition) {
                        mDetailsBackground.setCoverBitmap(bitmap);
                        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
                    }
                });
    }

    private void setupDetailsOverviewRow() {
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
        actionAdapter.add(
                new Action(
                        ADD_FAVORITE,
                        getResources().getString(R.string.favorite_1)));
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
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Favorite favorite = new Favorite(mSelectedNews.getId(), uid);
                    DatabaseReference favorites = FirebaseDatabase.getInstance().getReference().child("favorites");
                    favorites.push().setValue(favorite).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(),
                                    "Notícia gravada com sucesso",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Erro ao gravar a notícia", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (action.getId() == NEXT) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(DetailsActivity.NEWS, newsList.get(getNextNews()));
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

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {

            if (item instanceof Movie) {
                Log.d(TAG, "Item: " + item.toString());
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(getResources().getString(R.string.movie), mSelectedNews);

                Bundle bundle =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(),
                                ((ImageCardView) itemViewHolder.view).getMainImageView(),
                                DetailsActivity.SHARED_ELEMENT_NAME)
                                .toBundle();
                getActivity().startActivity(intent, bundle);
            }
        }
    }

    private int getNextNews() {
        int count = 0;
        for (News news : newsList) {
            if (news.getId() == mSelectedNews.getId()) {
                count = newsList.indexOf(news);
                count++;
            }
        }
        return count;
    }

    private boolean checkIfItLast() {
        int pos = 0;
        for (News news : newsList) {
            if (news.getId() == mSelectedNews.getId()) {
                pos = newsList.indexOf(news);
                break;
            }
        }
        return (pos == (newsList.size() - 1));
    }
}
