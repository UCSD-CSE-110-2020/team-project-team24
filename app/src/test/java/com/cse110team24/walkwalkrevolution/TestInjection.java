package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;

public class TestInjection {
    protected AuthServiceFactory asf;
    protected DatabaseServiceFactory dsf;
    protected AuthService mAuth;
    protected DatabaseService mDb;

    void setup() {
    }
}
