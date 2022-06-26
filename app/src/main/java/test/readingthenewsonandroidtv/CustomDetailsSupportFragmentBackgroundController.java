package test.readingthenewsonandroidtv;

import android.animation.PropertyValuesHolder;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.leanback.app.DetailsSupportFragment;
import androidx.leanback.app.DetailsSupportFragmentBackgroundController;
import androidx.leanback.graphics.FitWidthBitmapDrawable;
import androidx.leanback.widget.ParallaxTarget;

public class CustomDetailsSupportFragmentBackgroundController extends DetailsSupportFragmentBackgroundController {

    /**
     * Creates a DetailsSupportFragmentBackgroundController for a DetailsSupportFragment. Note that
     * each DetailsSupportFragment can only associate with one DetailsSupportFragmentBackgroundController.
     *
     * @param fragment The DetailsSupportFragment to control background and embedded video playing.
     * @throws IllegalStateException If fragment was already associated with another controller.
     */
    public CustomDetailsSupportFragmentBackgroundController(DetailsSupportFragment fragment) {
        super(fragment);
    }

    @Override
    public void enableParallax() {
        int offset = 10;
        Drawable coverDrawable = new FitWidthBitmapDrawable();
        ColorDrawable colorDrawable = new ColorDrawable();
        enableParallax(coverDrawable, colorDrawable,
                new ParallaxTarget.PropertyValuesHolderTarget(
                        coverDrawable,
                        PropertyValuesHolder.ofInt(FitWidthBitmapDrawable.PROPERTY_VERTICAL_OFFSET,
                                0, -offset)
                ));
    }
}
