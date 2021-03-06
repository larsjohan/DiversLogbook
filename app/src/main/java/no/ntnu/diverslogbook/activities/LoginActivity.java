package no.ntnu.diverslogbook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.models.Diver;
import no.ntnu.diverslogbook.utils.Database;
import no.ntnu.diverslogbook.utils.oauth.GoogleLogin;
import no.ntnu.diverslogbook.utils.oauth.OAuthProvider;

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
     * The provider the user has selected to log in with
     */
    OAuthProvider oAuthProvider;

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
     * Constructor.
     * Initializes the database
     */
    public LoginActivity(){
        Database.init();
    }


    /**
     * {@inheritDoc}
     *
     * Initializes listeners for buttons in GUI, and firebase Authentication.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init firebase
        this.firebaseAuth = FirebaseAuth.getInstance();

        // Find oAuth buttons
        ImageButton googleLogin = findViewById(R.id.ib_login_with_google);
        ImageButton twitterLogin = findViewById(R.id.ib_login_with_twitter);
        ImageButton facebookLogin = findViewById(R.id.ib_login_with_facebook);

        // Add listeners
        twitterLogin.setOnClickListener(this::comingSoon);
        facebookLogin.setOnClickListener(this::comingSoon);
        googleLogin.setOnClickListener(this::loginWithGoogle);


        // The user pressed logout in preferences
        if (getIntent().getBooleanExtra("logout", false)){
            this.firebaseAuth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * {@inheritDoc}
     *
     * When the activity starts: Check if the user already is logged in.
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
        this.oAuthProvider = new GoogleLogin(this);
        Intent loginWithGoogle = this.oAuthProvider.getLoginIntent();
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
                    AuthCredential credential = (AuthCredential) this.oAuthProvider.getLoginCredentialFromActivityResult(data);
                    if(credential != null) {
                        firebaseSignIn(credential);
                    } else {
                        this.user = null;
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
        
        if (this.user != null) {
            Diver diver = new Diver(this.user.getUid(), this.user.getDisplayName(), this.user.getEmail(), this.user.getPhoneNumber(), this.user.getPhotoUrl().toString());
            Database.setLoggedInDiver(diver);


            Intent startApp = new Intent(this, MainActivity.class);
            startActivity(startApp);
            finish();   // Remove this screen from stack to avoid back-button from MainActivity to open a new instance of itself
        }
    }


    /**
     * Authenticates the provided credentials and updates the GUI.
     * @param credential The login credentials to log in with
     */
    private void firebaseSignIn(AuthCredential credential) {
        this.firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (loginTask) -> {
                    if(loginTask.isSuccessful()){
                        this.user = this.firebaseAuth.getCurrentUser();
                    }else{
                        Toast.makeText(this, "Login Failed. Try again", Toast.LENGTH_SHORT).show();
                        Log.w(getString(R.string.app_name), "Authentication failed: " + loginTask.getException());
                        this.user = null;
                    }
                    updateUI();
                });
    }
}
