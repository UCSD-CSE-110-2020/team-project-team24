package com.cse110team24.walkwalkrevolution.firebase.auth;

import java.util.HashMap;
import java.util.Map;

public class FirebaseAuthServiceFactory {
    private static final String TAG = "[AuthServiceFactory]";

    // TODO: 3/1/20 because login activity is instantiated first, we will have to use mockito
    //  to mock AuthServiceFactor and return a different AuthService instead
    public AuthService createAuthService() {
        return new FirebaseAuthAdapter();
    }
}
