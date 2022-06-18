package test.readingthenewsonandroidtv;

import java.io.Serializable;

/*
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
public class News implements Serializable {
    static final long serialVersionUID = 727566175075960653L;
    private long id;
    private String title;
    private String article;
    private String bgImageUrl;
    private String cardImageUrl;
    private String source;
    private String link;
    private String photoCredit;

    public News() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getBackgroundImageUrl() {
        return bgImageUrl;
    }

    public void setBackgroundImageUrl(String bgImageUrl) {
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
                '}';
    }
}