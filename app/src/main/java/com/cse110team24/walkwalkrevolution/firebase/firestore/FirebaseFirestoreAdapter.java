package com.cse110team24.walkwalkrevolution.firebase.firestore;

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

    public FirebaseFirestoreAdapter() {

    }
}
