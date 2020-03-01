const functions = require('firebase-functions');

exports.sendInviteNotification = functions.firestore
    //.document('invitations/{inviteId}')
    .document('users/{user}/invitations/{inviteId}')
    .onCreate((snap, context) => {
        const document = snap.exists ? snap.data() : null;

        if (document) {
            var message = {
                notification: {
                    title: document.from + ' sent you an invite!',
                }
                topic: context.params.inviteId
            };

            return admin.messaging().send(message)
                .then((response) => {
                    console.log('Successfully sent invite:', response);
                    return response;
                })
                .catch((error) => {
                    console.log('Error sending invite:', error);
                    return error;
                });
        }

        return "document was null or empty";
    });