package com.cse110team24.walkwalkrevolution.observer;

public interface Subject<TObserver> {
    void register(TObserver observer);

    void deregister(TObserver observer);
}
