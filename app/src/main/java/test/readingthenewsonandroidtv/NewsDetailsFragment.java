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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import test.readingthenewsonandroidtv.model.Favorite;
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

    private News mSelectedNews;

    private ArrayObjectAdapter mAdapter;
    private ClassPresenterSelector mPresenterSelector;

    private DetailsSupportFragmentBackgroundController mDetailsBackground;

    private final List<News> newsList = NewsList.getNewsList();
    private Boolean isFavorite;
    private String keepFirebaseFavoriteKey;
    private final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);


        mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);

        mSelectedNews =
                (News) getActivity().getIntent().getSerializableExtra(DetailsActivity.NEWS);
        if (mSelectedNews != null) {
            mPresenterSelector = new ClassPresenterSelector();
            mAdapter = new ArrayObjectAdapter(mPresenterSelector);

            //verifyFavorites();
            // function setupDetailsOverviewRow() is called inside verify favorites
            setupDetailsOverviewRow(false);

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
                } else if (action.getId() == REMOVE_FAVORITE) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("favorites");
                    reference.child(keepFirebaseFavoriteKey)
                                            .removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //removed with success
                                                    Toast.makeText(getActivity(), "Favorito removido com sucesso", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            //removed failed
                                                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

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

    public void verifyFavorites() {
        Log.d(TAG, "entrei nos favoritos");

        isFavorite = false;
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference fav_reference = FirebaseDatabase.getInstance().getReference();
        Query query = fav_reference.child("favorites")
                                   .orderByChild("user")
                                   .equalTo(uid);
        Log.d(TAG, query.toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "entrei na função");

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Favorite favorite = postSnapshot.getValue(Favorite.class);

                        Log.d(TAG, "Id da notícia no Firebase:" + favorite.getId());
                        Log.d(TAG, "Id da notícia da Lista:" + mSelectedNews.getId());

                        if (favorite.getId() == mSelectedNews.getId()) {
                            keepFirebaseFavoriteKey = postSnapshot.getKey();
                            Log.d(TAG, "firebase key for this news and user is: " + keepFirebaseFavoriteKey);
                            isFavorite = true;
                            break;
                        }
                    }
                }
                // delay method loadRows until we have favorites
                setupDetailsOverviewRow(isFavorite);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {  }
        });
    }
}
