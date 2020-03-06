package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceObserver;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the test implementation of {@link AuthServiceFactory} and {@link AuthService}.
 * <p><b>Set the static fields of this class to provide results for method calls.</b></p>
 */
public class TestAuth {

    /**
     * Creates instance of TestAuthService.
     */
    public static class TestAuthAuthServiceFactory implements AuthServiceFactory {

        /**
         *
         * @return an instance of {@link TestAuthService}
         */
        @Override
        public AuthService createAuthService() {
            return new TestAuthService();
        }
    }


    // set these when you need to.
    /**
     * The intended user signed in to the app. Should be set both in sign-up and sign-in tests.
     */
    public static IUser testAuthUser;

    /**
     * The intended {@link com.cse110team24.walkwalkrevolution.firebase.auth.AuthService.AuthError} during
     * sign-in and sing-up error tests.
     */
    public static AuthService.AuthError testAuthError;

    /**
     * Set to true when sign-in or sign-up is required for the test.
     */
    public static boolean isTestUserSignedIn;

    /**
     * Set to true when testing that a user signed up successfully. Set false when testing sign-up errors.
     */
    public static boolean successUserSignedUp;

    /**
     * Set to true when testing that user sign in successfully. Set false when testing sign-in errors.
     */
    public static boolean successUserSignedIn;

    /**
     * Test implementation of {@link AuthService}.
     */
    public static class TestAuthService implements AuthService{
        private static final String TAG = "[WWR_TestAuthService]";

        @Override
        public void signIn(String email, String password) {
            /** TODO: 3/5/20 should call one of
             * [observer].onUserSignedUp(testAuthUser)
             * [observer].onAuthSignUpError(testAuthError)
             */
            if (successUserSignedIn) {
                observer.onUserSignedIn(testAuthUser);
            } else {
                observer.onAuthSignInError(testAuthError);
            }
        }

        /**
         * The single observer who will be notified of method calls.
         */
        public AuthServiceObserver observer;

        @Override
        public void signUp(String email, String password, String displayName) {
            /** TODO: 3/5/20 should call one of
             * [observer].onUserSignedUp(testAuthUser)
             * [observer].onAuthSignUpError(testAuthError)
             */

            System.out.println(TAG + ": signUp: called with email " + email + " and name " + displayName);
            if (successUserSignedUp) {
                observer.onUserSignedUp(testAuthUser);
            } else {
                observer.onAuthSignUpError(testAuthError);
            }
        }

        @Override
        public IUser getUser() {
            return testAuthUser;
        }

        @Override
        public AuthError getAuthError() {
            return testAuthError;
        }

        @Override
        public boolean isUserSignedIn() {
            return isTestUserSignedIn;
        }

        @Override
        public void notifyObserversSignedIn(IUser user) {

        }

        @Override
        public void notifyObserversSignedUp(IUser user) {

        }

        @Override
        public void notifyObserversSignInError(AuthError error) {

        }

        @Override
        public void notifyObserversSignUpError(AuthError error) {

        }

        @Override
        public void register(AuthServiceObserver authServiceObserver) {
            observer = authServiceObserver;
        }

        @Override
        public void deregister(AuthServiceObserver authServiceObserver) {

        }
    }
}
