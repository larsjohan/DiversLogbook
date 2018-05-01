package no.ntnu.diverslogbook.utils.oauth;

import android.content.Intent;

/**
 * Declares functions that should be available for an OAuthProvider
 * @param <T> The class that is used to store the login credential
 * @author Lars Johan
 */
public interface OAuthProvider<T> {

    /**
     * Get the login-intent for this provider
     */
    Intent getLoginIntent();

    /**
     * Returnes a login-credential from the provider
     * @param data The activity-result-intent
     */
    T getLoginCredentialFromActivityResult(Intent data);


}
