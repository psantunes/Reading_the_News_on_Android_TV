package test.readingthenewsonandroidtv;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;
import androidx.leanback.widget.RowHeaderView;

public class CustomRowHeaderPresenter extends RowHeaderPresenter {


    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        Presenter.ViewHolder viewHolder = super.onCreateViewHolder(parent);
        RowHeaderView rowHeaderView = (RowHeaderView) viewHolder.view;
        // Typeface typeface = parent.getResources().getFont(R.font.font_awesome_6_free_solid_900);
        // rowHeaderView.setTypeface(typeface);
        rowHeaderView.setTextSize(TypedValue.COMPLEX_UNIT_SP,32);
        return viewHolder;
    }
}
