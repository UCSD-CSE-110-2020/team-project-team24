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

exports.sendNewTeamWalkNotification = functions.firestore
    .document('teams/{team}/teamWalks/{teamWalk}')
    .onCreate((snap, context) => {
          const document = snap.exists ? snap.data() : null;
          if (document) {
              var message = {
                  notification: {
                      title: document.proposedBy + ' has proposed a team walk',
                      body: 'Click to see your team'
                  },
                  topic: context.params.team
              };

              return admin.messaging().send(message)
                  .then((response) => {
                      console.log('Successfully sent new team walk notification:', message);
                      return response;
                  })
                  .catch((error) => {
                      console.log('Error sending new team walk notification:', error);
                      return error;
                  });
          }

          return "document was null or empty";
    });

exports.sendUpdateTeamWalkNotification = functions.firestore
    .document('teams/{team}/teamWalks/{teamWalk}')
    .onUpdate((change, context) => {
        const document = change.after.data();

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
                    console.log('Successfully sent team walk update notification:', message);
                    return response;
                })
                .catch((error) => {
                    console.log('Error sending team walk update notification:', error);
                    return error;
                });
        }

        return "document was null or empty";
    });

exports.sendTeammateUpdateWalkStatusNotification = functions.firestore
    .document('teams/{team}/teamWalks/{teamWalk}/teammateStatuses/{teammateStatus}')
    .onUpdate((change, context) => {
        const document = change.after.data();

        if (document) {
            var statusString = document.status;
            var messageTitle = statusString;
            var messageBody = 'Click to see your team';
            var statusArr = statusString.split(' ');
            if (statusArr[0] === 'declined') {
                messageTitle = 'declined';
                statusArr.shift();
                messageBody = statusArr.join(' ');
            }
            var message = {
                notification: {
                    title: document.displayName + ' has ' + messageTitle,
                    body: messageBody
                },
                topic: context.params.team
            };

            return admin.messaging().send(message)
                .then((response) => {
                    console.log('Successfully sent teammate changed status for walk notification:', message);
                    return response;
                })
                .catch((error) => {
                    console.log('Error sending teammate changed status for walk notification:', error);
                    return error;
                });
        }

        return "document was null or empty";
    });

exports.sendTeammateChoseStatusNotification = functions.firestore
    .document('teams/{team}/teamWalks/{teamWalk}/teammateStatuses/{teammateStatus}')
    .onCreate((snap, context) => {
          const document = snap.exists ? snap.data() : null;

        if (document) {
            var statusString = document.status;
            var messageTitle = statusString;
            var messageBody = 'Click to see your team';
            var statusArr = statusString.split(' ');
            if (statusArr[0] === 'declined') {
                messageTitle = 'declined';
                statusArr.shift();
                messageBody = statusArr.join(' ');
            }
            var message = {
                notification: {
                    title: document.displayName + ' has ' + messageTitle,
                    body: messageBody
                },
                topic: context.params.team
            };

            return admin.messaging().send(message)
                .then((response) => {
                    console.log('Successfully sent teammate changed status for walk notification:', message);
                    return response;
                })
                .catch((error) => {
                    console.log('Error sending teammate changed status for walk notification:', error);
                    return error;
                });
        }

        return "document was null or empty";
    });