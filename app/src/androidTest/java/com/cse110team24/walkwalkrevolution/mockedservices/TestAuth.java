package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.auth.Auth;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthFactory;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthObserver;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

/**
 * Contains the test implementation of {@link AuthFactory} and {@link Auth}.
 * <p><b>Set the static fields of this class to provide results for method calls.</b></p>
 */
public class TestAuth {

    /**
     * Creates instance of TestAuthService.
     */
    public static class TestAuthFactory implements AuthFactory {

        /**
         *
         * @return an instance of {@link TestAuthImplementation}
         */
        @Override
        public Auth createAuthService() {
            return new TestAuthImplementation();
        }
    }


    // set these when you need to.
    /**
     * The intended user signed in to the app. Should be set both in sign-up and sign-in tests.
     * <p>This user is returned by {@link TestAuthImplementation#getUser()}</p>
     */
    public static IUser testAuthUser;

    /**
     * The intended {@link Auth.AuthError} during
     * sign-in and sing-up error tests.
     * <p>This auth error is returned by {@link TestAuthImplementation#getAuthError()} and is sent to observers by
     * {@link TestAuthImplementation#signUp(String, String, String)} or {@link TestAuthImplementation#signIn(String, String)}
     * when one of {@link com.cse110team24.walkwalkrevolution.mockedservices.TestAuth#successUserSignedUp} or {@link com.cse110team24.walkwalkrevolution.mockedservices.TestAuth#successUserSignedIn} is false</p>
     */
    public static Auth.AuthError testAuthError;

    /**
     * Set to true when sign-in or sign-up is required for the test.
     * <p>This boolean is returned by {@link TestAuthImplementation#isUserSignedIn()}</p>
     */
    public static boolean isTestUserSignedIn;

    /**
     * Set to true when testing that a user signed up successfully. Set false when testing sign-up errors.
     *
     * <p>When this is true, this TestAuthService will call {@link AuthObserver#onUserSignedUp(IUser)} with argument {@link com.cse110team24.walkwalkrevolution.mockedservices.TestAuth#testAuthUser}</p>
     * <p>When this is false, this TestAuthService will call {@link AuthObserver#onAuthSignUpError(Auth.AuthError)} with argument {@link com.cse110team24.walkwalkrevolution.mockedservices.TestAuth#testAuthError}</p>
     */
    public static boolean successUserSignedUp;

    /**
     * Set to true when testing that user sign in successfully. Set false when testing sign-in errors.
     *
     * <p>When this is true, this TestAuthService will call {@link AuthObserver#onUserSignedIn(IUser)} with argument {@link com.cse110team24.walkwalkrevolution.mockedservices.TestAuth#testAuthUser}</p>
     * <p>When this is false, this TestAuthService will call {@link AuthObserver#onAuthSignInError(Auth.AuthError)} with argument {@link com.cse110team24.walkwalkrevolution.mockedservices.TestAuth#testAuthError}</p>
     */
    public static boolean successUserSignedIn;

    /**
     * Test implementation of {@link Auth}.
     */
    public static class TestAuthImplementation implements Auth {
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
        public AuthObserver observer;

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
        public void register(AuthObserver authObserver) {
            observer = authObserver;
        }

        @Override
        public void deregister(AuthObserver authObserver) {

        }
    }
}
