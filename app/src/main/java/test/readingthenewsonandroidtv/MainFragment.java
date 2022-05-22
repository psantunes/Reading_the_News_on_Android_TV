package test.readingthenewsonandroidtv;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import android.util.Log;

import java.util.List;

public class MainFragment extends GuidedStepSupportFragment {
    private static final String TAG = "MainFragment";

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
                .title(getString(R.string.about_us))
                .editable(false)
                .build();
        actions.add(action);

    }
}