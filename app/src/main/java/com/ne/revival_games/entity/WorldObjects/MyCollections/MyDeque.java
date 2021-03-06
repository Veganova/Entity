package com.ne.revival_games.entity.WorldObjects.MyCollections;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import java.util.Iterator;

/**
 * Minimalisitic storage that sacrifices normal functionality (that is not needed in our usage)
 * and provides some very useful improvements. Some tested data:
 *
 // Million additions -
 // Linked - 1402875182 ns
 // DequeStuff2.MyDeque- 1316633929 ns
 // ------------------------------------------------------------------------------
 *
 // Million elements .size()
 // Linked - 199111 ns
 // DequeStuff2.MyDeque- 249679 ns -- close  enough (this is what O(1) looks like)
 // ------------------------------------------------------------------------------
 *
 *
 * The real performance upgrade
 // Million element .remove()
 // Linked - 8549143 ns
 // DequeStuff2.MyDeque -229531 ns
 // ------------------------------------------------------------------------------
 */
public class MyDeque implements BasicList<Entity> {

    private Node tail;

    private int size = 0;


    public MyDeque() {
        tail = new Sentinel();

    }

    public Node add(Entity value) {
        // if no node is associated with the given
        Node n = value.getNode();
        if (n == null) {
            Node node = new Node(value, this.tail, this.tail.right);
            this.tail.right.left = node;
            this.tail.right = node;
            this.tail = node;
            this.size += 1;

            return node;
        } else {
            n.increment();
            return n;
        }
    }

    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Entity> iterator() {
        return new DequeIterator(tail);
    }

    public Iterator<Entity> reverseIterator() {
        return new BackwardsDequeIterator(tail);
    }

    /**
     * Assumes the element being removed is in the list.
     */
    public void remove(Entity s) {
//        System.out.println("REMOVING - " + s.s + ". Tail is " + " " + this.tail.content.s);

        // If tail is being removed, reset the tail.
        Node n = s.getNode();
        n.decrement();
        if (n.num == 0) {
            if (n == tail) {
                this.tail = this.tail.left;
            }
            n.remove();
            this.size -= 1;
        }
    }


    public class Node {
        Node right, left;
        Entity content;
        int num = 1;

        Node(Entity content, Node left, Node right) {
//            this.content = new StringHolder(content, this);
            if (content != null) // if null in the sentinel case
                content.setNode(this);

            this.content = content;
            this.right = right;
            this.left = left;
        }

        void increment() {
            this.num += 1;
        }

        void decrement() {
            this.num -= 1;
        }

        // fix refernces
        void remove() {
            this.left.right = this.right;
            this.right.left = this.left;
        }

        public boolean isSentinel() {
            return false;
        }
    }

    class Sentinel extends Node {

        // right is the tail.
        Sentinel() {
            super(null, null, null);
            this.right = this;
            this.left = this;
        }

        void remove() {
//      throw new RuntimeException("cannot remove sentinel");
        }

        public boolean isSentinel() {
            return true;
        }
    }


    private class DequeIterator implements Iterator<Entity> {

        private Node tail;


        DequeIterator(Node tail) {
            this.tail = tail;
        }

        @Override
        public boolean hasNext() {
            return !this.tail.isSentinel();
        }
        private int index = 0;
        @Override
        public Entity next() {
            Node current = tail;
            this.tail = this.tail.left;
            index += 1;

            if (current.isSentinel()) {
                throw new RuntimeException("Iterator is returning the sentinel!");
            }
            return current.content;
        }
    }

    private class BackwardsDequeIterator implements Iterator<Entity> {

        private Node start;


        BackwardsDequeIterator(Node tail) {
            this.start = tail.right.right;
        }

        @Override
        public boolean hasNext() {
            return !this.start.isSentinel();
        }

        private int index = 0;

        @Override
        public Entity next() {
            Node current = start;
            this.start = this.start.right;
            index += 1;

            if (current.isSentinel()) {
                throw new RuntimeException("Iterator is returning the sentinel!");
            }
            return current.content;
        }
    }


}
