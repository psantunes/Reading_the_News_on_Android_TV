package test.readingthenewsonandroidtv;

public class NewsDetailsDescriptionPresenter extends CustomAbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        News news = (News) item;

        if (news != null) {
            viewHolder.getTitle().setText(news.getTitle());
            viewHolder.getSubtitle().setText(news.getStudio());
            viewHolder.getBody().setText(news.getDescription());
        }
    }


}