package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class TestInjection {
    private static final String TAG = "[TestInjection]";
    protected static AuthService.AuthError nextError = AuthService.AuthError.OTHER;
    protected static boolean nextSignIn = false;
    protected static boolean nextSuccessStatus = false;
    protected static AuthServiceFactory asf = new TestAuthServiceFactory();
    protected static DatabaseServiceFactory dsf = new TestDatabaseServiceFactory();

    protected static IUser signedInUser = new FirebaseUserAdapter.Builder()
            .addDisplayName("test")
            .addEmail("tester@gmail.com")
            .addUid("1")
            .build();

    public TestInjection() {
        System.out.println(TAG + ": test injection created");
    }

    protected void setup() {
        FirebaseApplicationWWR.setDatabaseServiceFactory(dsf);
        FirebaseApplicationWWR.setAuthServiceFactory(asf);
//        FirebaseApplicationWWR.setMessagingServiceFactory(msf);
    }


    public static class TestAuthServiceFactory implements AuthServiceFactory {
        private static final String TAG = "[TestAuthServiceFactory]";
        @Override
        public AuthService createAuthService() {
            System.out.println(TAG + ": creating test auth service");
            return new TestAuthService();
        }
    }

    public static class TestDatabaseServiceFactory implements DatabaseServiceFactory {
        private static final String TAG = "[TestDatabaseServiceFactory]";

        @Override
        public DatabaseService createDatabaseService() {
            System.out.println(TAG + ": creating test database service");
            return new TestDatabaseService();
        }
    }

    public static class TestDatabaseService implements DatabaseService {

        @Override
        public DocumentReference createUserInDatabase(IUser user) {
            return null;
        }

        @Override
        public DocumentReference setUserTeam(IUser user, String teamUid) {
            return null;
        }

        @Override
        public DocumentReference createTeamInDatabase(ITeam team) {
            return null;
        }

        @Override
        public DocumentReference updateTeamMembers(ITeam team) {
            return null;
        }

        @Override
        public Task<?> addInvitationForReceivingUser(Invitation invitation) {
            return null;
        }

        @Override
        public Task<?> addInvitationForSendingUser(Invitation invitation) {
            return null;
        }

        @Override
        public DocumentReference createRootInvitationDocument(Invitation invitation) {
            return null;
        }

        @Override
        public List<Invitation> getUserPendingInvitations(IUser user) {
            return null;
        }

        @Override
        public ITeam getUserTeam(IUser user) {
            return null;
        }

        @Override
        public Object getField(String path, String fieldKey) {
            return null;
        }

        @Override
        public void addInvitationsSnapshotListener(IUser user) {

        }

        @Override
        public DocumentReference addUserMessagingRegistrationToken(IUser user, String token) {
            return null;
        }
    }

    public static class TestAuthService implements AuthService {
        private static final String TAG = "[TestAuthService]";

        List<AuthServiceObserver> authServiceObservers = new ArrayList<>();
        public TestAuthService() {
            System.out.println(TAG + ": created");
        }

        @Override
        public void signIn(String email, String password) {

            if (nextSuccessStatus) {
                notifyObserversSignedIn(signedInUser);
            } else {
                notifyObserversSignInError(nextError);
            }
        }

        @Override
        public void signUp(String email, String password, String displayName) {
            if (nextSuccessStatus) {
                notifyObserversSignedUp(signedInUser);
            } else {
                notifyObserversSignUpError(nextError);
            }
        }

        @Override
        public IUser getUser() {
            return signedInUser;
        }

        @Override
        public AuthError getAuthError() {
            return null;
        }

        @Override
        public boolean isUserSignedIn() {
            return nextSignIn;
        }

        @Override
        public void notifyObserversSignedIn(IUser user) {
            authServiceObservers.forEach(observer -> {
                observer.onUserSignedIn(user);
            });
        }

        @Override
        public void notifyObserversSignedUp(IUser user) {
            authServiceObservers.forEach(observer -> {
                observer.onUserSignedUp(user);
            });
        }

        @Override
        public void notifyObserversSignInError(AuthError error) {
            authServiceObservers.forEach(observer -> {
                observer.onAuthSignInError(nextError);
            });
        }

        @Override
        public void notifyObserversSignUpError(AuthError error) {
            authServiceObservers.forEach(observer -> {
                observer.onAuthSignUpError(nextError);
            });
        }

        @Override
        public void register(AuthServiceObserver authServiceObserver) {
            authServiceObservers.add(authServiceObserver);
        }

        @Override
        public void deregister(AuthServiceObserver authServiceObserver) {

        }
    }
}
