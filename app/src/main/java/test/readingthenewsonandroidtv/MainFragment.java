package test.readingthenewsonandroidtv;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import android.util.Log;

import java.util.List;

import test.readingthenewsonandroidtv.util.Mode;
import test.readingthenewsonandroidtv.util.Setup;

public class MainFragment extends GuidedStepSupportFragment {
    private static final String TAG = "MainFragment";

    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        String title = Setup.APP_NAME;
        String description = Setup.APP_DESCRIPTION;
        Drawable icon = getActivity().getDrawable(Setup.LOGO);
        return new GuidanceStylist.Guidance(title, description, null, icon);
    }

    @Override
    public void onCreateActions(List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder(getActivity())
                .id(1)
                .title(getString(R.string.news_nav_mode))
                .editable(false)
                .description(R.string.news_nav_mode_desc)
                .build();
        actions.add(action);
        action = new GuidedAction.Builder(getActivity())
                .id(2)
                .title(getString(R.string.news_kyosk_mode))
                .editable(false)
                .description(R.string.news_kyosk_mode_desc)
                .build();
        actions.add(action);
        action = new GuidedAction.Builder(getActivity())
                .id(3)
                .title(getString(R.string.favorites))
                .editable(false)
                .description(R.string.favorites_desc)
                .build();
        actions.add(action);
        action = new GuidedAction.Builder(getActivity())
                .id(4)
                .title(getString(R.string.logout))
                .editable(false)
                .build();
        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        switch ((int) action.getId()){
            case 1:
                Log.i(TAG, "Option selected: " + getString(R.string.news_nav_mode));
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                intent.putExtra(NewsActivity.NEWS, 0);
                intent.putExtra(NewsActivity.SOURCE, 0);
                intent.putExtra(NewsActivity.MODE, Mode.navigation);
                startActivity(intent);
                break;
            case 2:
                Log.i(TAG, "Option selected: " + getString(R.string.news_kyosk_mode));
                Intent intent2 = new Intent(getActivity(), NewsActivity.class);
                intent2.putExtra(NewsActivity.NEWS, 0);
                intent2.putExtra(NewsActivity.SOURCE, 0);
                intent2.putExtra(NewsActivity.MODE, Mode.kyosk);
                startActivity(intent2);
                break;
            case 3:
                Log.i(TAG, "Option selected: " + getString(R.string.favorites));
                Intent intent3 = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Log.w(TAG, "Option selected: " + getString(R.string.logout));
                getActivity().finishAffinity();
                break;
        }
    }
}

