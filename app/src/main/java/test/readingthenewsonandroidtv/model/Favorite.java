package test.readingthenewsonandroidtv.model;

import com.google.firebase.auth.FirebaseUser;

public class Favorite {
    private int id;
    private String user;

    public Favorite() { }
    public Favorite(int id, String user) {
        this.id = id;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
