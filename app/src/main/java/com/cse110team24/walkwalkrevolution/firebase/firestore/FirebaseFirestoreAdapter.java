package com.cse110team24.walkwalkrevolution.firebase.firestore;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.cse110team24.walkwalkrevolution.models.team.TeamAdapter.MEMBERS_KEY;
import static com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter.TEAM_UID_KEY;

/** TODO: 2/28/20 flow for a team should be
 * get the user's account (sign in or up if necessary)
 * Check if they have a team.
 * check if user has team
 *      if yes, update UI
 *
 *      if not, when user sends invite,
 *          instantiate team,
 *          add user as member of team,
 *          create team in database
 *          set team's new UID as teamUID for user
 *
 */
// TODO: 3/3/20 split this adapter into three specialized service adapters
public class FirebaseFirestoreAdapter implements DatabaseService {
    private static final String TAG = "WWR_FirebaseFirestoreAdapter";
    public static final String ROUTES_COLLECTION_KEY = "routes";
    public static final String TEAM_ID_KEY = "teamUid";

    List<DatabaseServiceObserver> observers = new ArrayList<>();

    public FirebaseFirestoreAdapter() {

    }
}
