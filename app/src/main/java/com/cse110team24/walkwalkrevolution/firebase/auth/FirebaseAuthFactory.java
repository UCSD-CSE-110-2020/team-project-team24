package com.cse110team24.walkwalkrevolution.firebase.auth;

public class FirebaseAuthFactory implements AuthFactory {
    private static final String TAG = "[AuthServiceFactory]";

    // TODO: 3/1/20 because login activity is instantiated first, we will have to use mockito
    //  to mock AuthServiceFactor and return a different AuthService instead
    @Override
    public Auth createAuthService() {
        return new FirebaseAuthAdapter();
    }
}
