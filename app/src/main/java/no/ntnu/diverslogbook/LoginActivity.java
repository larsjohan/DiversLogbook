package no.ntnu.diverslogbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Handles login and authentication.
 *
 * Lets the user login using email/password or an existing account from
 * google, twitter or facebook.
 *
 * @author Lars Johan
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Request-code for google login intent
     */
    private static final int GOOGLE_LOGIN_RESULT = 1;

    /**
     * Request-code for twitter login intent
     */
    private static final int TWITTER_LOGIN_RESULT = 2;

    /**
     * Request-code for facebook login intent
     */
    private static final int FACEBOOK_LOGIN_RESULT = 3;

    /**
     * Request-code for local email/password login intent
     */
    private static final int EMAIL_LOGIN_RESULT = 4;

    /**
     * Login client for google-login
     */
    private GoogleSignInClient googleLoginClient;

    /**
     * Google account instance
     */
    private GoogleSignInAccount account;

    /**
     * Firebase authentication hook
     */
    private FirebaseAuth firebaseAuth;

    /**
     * Firebase user (authenticated usin firebaseAuth).
     * Represents a unified user, logged in with any credential
     */
    private FirebaseUser user;


    /**
     * {@inheritDoc}
     *
     * Creates a hook to google-authentication, and integration with firebase.
     * Sets listeners for buttons in gui.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googleLoginOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.Oauth2_client_secret))
                .requestEmail()
                .build();

        this.googleLoginClient = GoogleSignIn.getClient(this, googleLoginOptions);

        this.firebaseAuth = FirebaseAuth.getInstance();

        ImageButton googleLogin = findViewById(R.id.ib_login_with_google);
        ImageButton twitterLogin = findViewById(R.id.ib_login_with_twitter);
        ImageButton facebookLogin = findViewById(R.id.ib_login_with_facebook);

        twitterLogin.setOnClickListener(this::comingSoon);
        facebookLogin.setOnClickListener(this::comingSoon);
        googleLogin.setOnClickListener(this::loginWithGoogle);

    }

    /**
     * When the activity starts: Makes a check if the user already is logged in.
     */
    @Override
    protected void onStart() {
        super.onStart();
        this.user = this.firebaseAuth.getCurrentUser();
        updateUI();
    }

    /**
     * Displays a message for buttons with unimplemented functionality
     * @param view context
     */
    void comingSoon(View view){
        Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    /**
     * Starts login-process for google-authentication.
     * @param view context
     */
    void loginWithGoogle(View view){
        Intent loginWithGoogle = this.googleLoginClient.getSignInIntent();
        startActivityForResult(loginWithGoogle, GOOGLE_LOGIN_RESULT);
    }

    /**
     * When a user has logged in (provided credentials),
     * authenticate the credentials using the appropriate Oauth2 prvider
     * and integrate with firebase.
     * {@inheritDoc}
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            // Handle login/auth from Google
            case GOOGLE_LOGIN_RESULT:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try{
                    this.account = task.getResult(ApiException.class);
                    AuthCredential credential = GoogleAuthProvider.getCredential(this.account.getIdToken(), null);

                    this.firebaseAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this, (loginTask) -> {
                                if(loginTask.isSuccessful()){
                                    this.user = this.firebaseAuth.getCurrentUser();
                                    updateUI();
                                }else{
                                    Log.w(getString(R.string.app_name), "Authentication failed: " + loginTask.getException());
                                    this.user = null;
                                }
                            });

                    updateUI();
                } catch (ApiException e) {
                    Log.w(getString(R.string.app_name), "Signin result failed: " + e.getMessage());
                    this.account = null;
                    updateUI();
                }
                break;
            // Handle login/auth from Twitter
            case TWITTER_LOGIN_RESULT: break;
            // Handle login/auth from Facebook
            case FACEBOOK_LOGIN_RESULT: break;
            // Handle login/auth from local email/password
            case EMAIL_LOGIN_RESULT: break;
        }
    }

    /**
     * Starts the app if the user has been authenticated.
     */
    void updateUI() {
        if(this.user != null) {
            Intent startApp = new Intent(this, MainActivity.class);
            startActivity(startApp);
        } else {
            Toast.makeText(this, "Login failed. Try again.", Toast.LENGTH_LONG).show();
        }
    }
}
