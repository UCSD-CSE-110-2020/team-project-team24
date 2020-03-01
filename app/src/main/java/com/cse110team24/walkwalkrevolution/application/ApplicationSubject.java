package com.cse110team24.walkwalkrevolution.application;

import com.cse110team24.walkwalkrevolution.utils.Subject;

public interface ApplicationSubject extends Subject<ApplicationObserver> {

    void notifyObserversNewToken(String token);
}
