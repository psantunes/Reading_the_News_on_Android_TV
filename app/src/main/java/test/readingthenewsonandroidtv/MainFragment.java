package test.readingthenewsonandroidtv;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import android.util.Log;

import java.util.List;

import test.readingthenewsonandroidtv.model.News;
import test.readingthenewsonandroidtv.model.NewsList;

public class MainFragment extends GuidedStepSupportFragment {
    private static final String TAG = "MainFragment";
    public static List<News> newsList = NewsList.getNewsList();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = getString(R.string.app_name);
        String description = getString(R.string.app_name_desc);
        Drawable icon = getActivity().getDrawable(R.drawable.newspaper1
        );
        return new GuidanceStylist.Guidance(title, description, null, icon);
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder(getActivity())
                .id(1)
                .title(getString(R.string.news_slide_mode))
                .editable(false)
                .description(R.string.news_slide_mode_desc)
                .build();
        actions.add(action);
        action = new GuidedAction.Builder(getActivity())
                .id(2)
                .title(getString(R.string.favorites))
                .editable(false)
                .description(R.string.favorites_desc)
                .build();
        actions.add(action);
        action = new GuidedAction.Builder(getActivity())
                .id(3)
                .title(getString(R.string.logout))
                .editable(false)
                .build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        switch ((int) action.getId()){
            case 1:
                Log.i(TAG, "Option selected: " + getString(R.string.news_slide_mode));

                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.NEWS, newsList.get(0));
                startActivity(intent);
                break;
            case 2:
                Log.i(TAG, "Option selected: " + getString(R.string.favorites));
                Intent intent2 = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Log.w(TAG, "Option selected: " + getString(R.string.logout));
                getActivity().finishAndRemoveTask();
                break;
        }
    }
}