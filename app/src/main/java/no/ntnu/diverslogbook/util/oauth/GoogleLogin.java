package no.ntnu.diverslogbook.util.oauth;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.util.OAuthProvider;

/**
 * Enables sign in using an existing Google account.
 * @author Lars Johan
 */
public class GoogleLogin implements OAuthProvider<AuthCredential>{
    /**
     * Login client for google-login
     */
    private GoogleSignInClient googleLoginClient;


    /**
     * Constructor.
     * Initializes the google signin client used for authentication.
     * @param context Application context
     */
    public GoogleLogin(Context context) {
        GoogleSignInOptions googleLoginOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.Oauth2_client_secret))
                .requestEmail()
                .build();

        this.googleLoginClient = GoogleSignIn.getClient(context, googleLoginOptions);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Intent getLoginIntent() {
        return this.googleLoginClient.getSignInIntent();
    }

    /**
     * {@inheritDoc}
     * @param data The activity-result-intent
     * @return
     */
    @Override
    public AuthCredential getLoginCredentialFromActivityResult(Intent data) {
        Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);

        try {
            GoogleSignInAccount account = signInTask.getResult(ApiException.class);

            return GoogleAuthProvider.getCredential(account.getIdToken(), null);

        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }
}
