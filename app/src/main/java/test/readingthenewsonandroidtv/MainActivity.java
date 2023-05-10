package test.readingthenewsonandroidtv;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.FragmentActivity;
import com.google.firebase.auth.FirebaseAuth;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_browse_fragment, new MainFragment())
                    .commitNow();
        }

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                        // Verify on log if login is successful
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInAnonymously:success. User:" + mAuth.getCurrentUser().getUid());
                        } else {
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                        }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}