package test.readingthenewsonandroidtv.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.Date;

/**
 * The class News serializes the news received via external file or database
 */
public class News implements Serializable {
    static final long serialVersionUID = 727566175075960653L;
    private int id;
    private String title;
    private String article;
    private String bgImageUrl;
    private String cardImageUrl;
    private String source;
    private String link;
    private String photoCredit;
    private Date publishedAt;
    private String orientation;

    public News() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBgImageUrl() {
        return bgImageUrl;
    }

    public void setBgImageUrl(String bgImageUrl) {
        this.bgImageUrl = bgImageUrl;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public String getPhotoCredit() { return photoCredit; }

    public void setPhotoCredit(String photoCredit) { this.photoCredit = photoCredit; }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    @BindingAdapter("newsImage")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", article='" + article + '\'' +
                ", bgImageUrl='" + bgImageUrl + '\'' +
                ", cardImageUrl='" + cardImageUrl + '\'' +
                ", source='" + source + '\'' +
                ", link='" + link + '\'' +
                ", photoCredit='" + photoCredit + '\'' +
                ", orientation='" + orientation + '\'' +
                '}';
    }
}