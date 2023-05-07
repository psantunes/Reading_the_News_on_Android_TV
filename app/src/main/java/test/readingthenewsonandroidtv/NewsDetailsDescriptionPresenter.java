package test.readingthenewsonandroidtv;

import test.readingthenewsonandroidtv.model.News;

public class NewsDetailsDescriptionPresenter extends CustomAbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        News news = (News) item;
        String credit;

        if (news != null) {
            viewHolder.getTitle().setText(news.getTitle());
            credit = "Fonte: " + news.getSource() + ". Foto: " + news.getPhotoCredit();
            viewHolder.getSubtitle().setText(credit);
            viewHolder.getBody().setText(news.getArticle());
        }
    }


}