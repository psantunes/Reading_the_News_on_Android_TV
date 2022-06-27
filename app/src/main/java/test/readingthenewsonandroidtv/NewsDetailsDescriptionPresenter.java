package test.readingthenewsonandroidtv;

import test.readingthenewsonandroidtv.model.News;

public class NewsDetailsDescriptionPresenter extends CustomAbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        News news = (News) item;
        String text;

        if (news != null) {
            viewHolder.getTitle().setText(news.getTitle());
            viewHolder.getSubtitle().setText(news.getSource());
            text = news.getArticle() + "\n\nFoto: " + news.getPhotoCredit();
            viewHolder.getBody().setText(text);
        }
    }


}