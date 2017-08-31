package com.ne.revival_games.entity.WorldObjects.MyCollections;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;
import com.ne.revival_games.entity.WorldObjects.Entity.Team;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class MyList implements BasicList<Entity> {

    // store a hashmap of Team's list of entities
    Map<Team, MyDeque> teamDeques;

    MyList() {
        teamDeques = new HashMap<>();
        for (Team team: Team.values()) {
            teamDeques.put(team, new MyDeque());
        }
    }

    @Override
    public MyDeque.Node add(Entity ent) {
        return teamDeques.get(ent.team).add(ent);
    }

    @Override
    public int size() {
        int total = 0;
        for (MyDeque deque: this.teamDeques.values()) {
            total += deque.size();
        }
        return total;
    }

    @Override
    public void remove(Entity ent) {
        teamDeques.get(ent.team).remove(ent);
    }

    public Iterator<Entity> getTeamIterator(Team team) {
        return this.teamDeques.get(team).iterator();
    }

    public Iterator<Entity> getForwardTeamIterator(Team team) {
        return this.teamDeques.get(team).reverseIterator();
    }

    @Override
    public Iterator<Entity> iterator() {
        Stack<Iterator<Entity>> iters = new Stack<>();
        for (MyDeque deque: this.teamDeques.values()) {
            iters.push(deque.iterator());
        }
        return new CombineIterator<>(iters);
    }

    public String sizes() {
        String total = "";
        for (Team team: this.teamDeques.keySet()) {
            total += team + " " + this.teamDeques.get(team).size() + "\n";
        }
        return total;
    }


    private class CombineIterator<T> implements Iterator<T> {
        private Stack<Iterator<T>> iters;

        private Iterator<T> current;

        public CombineIterator(Stack<Iterator<T>> iters) {
            this.iters = iters;
            current = iters.pop();
        }

        @Override
        public boolean hasNext() {
            if (current.hasNext()) {
                return true;
            } else if (!iters.isEmpty()) {
                this.current = iters.pop();
                return this.hasNext();
            }

            return false;
        }

        @Override
        public T next() {
            return current.next();
        }
    }
}
