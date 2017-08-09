package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

import java.util.Iterator;

/**
 * Minimalisitic storage that sacrifices normal functionality (that is not needed in our usage)
 * and provides some very useful improvements. Some tested data:
 *
 // Million additions -
 // Linked - 1402875182 ns
 // MyDeque- 1316633929 ns
 // ------------------------------------------------------------------------------
 *
 // Million elements .size()
 // Linked - 199111 ns
 // MyDeque- 249679 ns -- close  enough (this is what O(1) looks like)
 // ------------------------------------------------------------------------------
 *
 *
 * The real performance upgrade
 // Million element .remove()
 // Linked - 8549143ns
 // MyDeque -229531 ns
 // ------------------------------------------------------------------------------
 */
public class MyDeque implements Iterable<Entity> {

    private Node tail;

    private int size = 0;

    public MyDeque() {
        tail = new Sentinel();

    }

    Node add(Entity value) {
        Node node = new Node(value, this.tail, this.tail.right);
        this.tail.right.left = node;
        this.tail.right = node;
        this.tail = node;
        this.size += 1;

        return node;
    }

    public int size() {
        return this.size;
    }

    @Override
    public Iterator<Entity> iterator() {
        return new DequeIterator(tail);
    }

    /**
     * Assumes the element being removed is in the list.
     */
    public void remove(Entity s) {
//        System.out.println("REMOVING - " + s.s + ". Tail is " + " " + this.tail.content.s);

        // If tail is being removed, reset the tail.
        if (s.getNode() == tail) {
            this.tail = this.tail.left;
        }
        s.getNode().remove();
        this.size -= 1;
    }


    public class Node {
        Node right, left;
        Entity content;

        Node(Entity content, Node left, Node right) {
//            this.content = new StringHolder(content, this);
            if (content != null) // if null in the sentinel case
                content.setNode(this);

            this.content = content;
            this.right = right;
            this.left = left;
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

//    class StringHolder {
//        String s;
//        Node node;
//
//        StringHolder(String s, Node node) {
//            this.s = s;
//            this.node = node;
//        }
//    }

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
}
