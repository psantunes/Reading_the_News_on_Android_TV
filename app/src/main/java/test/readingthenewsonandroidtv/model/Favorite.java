package test.readingthenewsonandroidtv.model;

import com.google.firebase.auth.FirebaseUser;

public class Favorite {
    private long id;
    private String user;

    public Favorite(long id, String user) {
        this.id = id;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
