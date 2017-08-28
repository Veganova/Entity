package com.ne.revival_games.entity.WorldObjects.MyCollections;

/**
 * Lists basic list functionality that is needed in our game.
 */
interface BasicList<T> extends Iterable<T> {

    int size();

    void remove(T item);

    MyDeque.Node add(T item);
}
