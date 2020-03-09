const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendInviteNotification = functions.firestore
    .document('invitations/{userInvite}/received/{inviteId}')
    .onCreate((snap, context) => {
        const document = snap.exists ? snap.data() : null;

        if (document) {
            var message = {
                notification: {
                    title: document.from.name + ' has invited you to join a team!',
                    body: 'Click to accept or decline this invitation'
                },
                topic: context.params.userInvite
            };

            return admin.messaging().send(message)
                .then((response) => {
                    console.log('Successfully sent invite:', message);
                    return response;
                })
                .catch((error) => {
                    console.log('Error sending invite:', error);
                    return error;
                });
        }

        return "document was null or empty";
    });

exports.sendInvitationResponseNotification = functions.firestore
    .document('invitations/{userInvite}/sent/{inviteId}')
    .onUpdate((change, context) => {
        const document = change.after.data();

        if (document) {
            var message = {
                notification: {
                    title: document.to.name + ' has ' + document.status + ' your invitation!',
                    body: 'Click to see your team'
                },
                topic: context.params.userInvite
            };

            return admin.messaging().send(message)
                .then((response) => {
                    console.log('Successfully sent invite:', message);
                    return response;
                })
                .catch((error) => {
                    console.log('Error sending invite:', error);
                    return error;
                });
        }

        return "document was null or empty";
    });

exports.sendNewTeammateNotification = functions.firestore
    .document('teams/{team}/teammates/{teammate}')
    .onCreate((snap, context) => {
        const document = snap.exists ? snap.data() : null;

        if (document) {
            var message = {
                notification: {
                    title: document.displayName + ' has joined your team!',
                    body: 'Click to see your team'
                },
                topic: context.params.team
            };

            return admin.messaging().send(message)
                .then((response) => {
                    console.log('Successfully sent new team notification:', message);
                    return response;
                })
                .catch((error) => {
                    console.log('Error sending new team notification:', error);
                    return error;
                });
        }

        return "document was null or empty";
    });

exports.sendTeamWalkUpdateNotification = functions.firestore
    .document('teams/{team}/teamWalks/{teamWalk}')
    .onUpdate((snap, context) => {
        const document = snap.exists ? snap.data() : null;

        if (document) {
            var message = {
                notification: {
                    title: document.proposedBy + ' has ' + document.status.toLowerCase() + ' a team walk',
                    body: 'Click to see your team'
                },
                topic: context.params.team
            };

            return admin.messaging().send(message)
                .then((response) => {
                    console.log('Successfully sent new team notification:', message);
                    return response;
                })
                .catch((error) => {
                    console.log('Error sending new team notification:', error);
                    return error;
                });
        }

        return "document was null or empty";
    });