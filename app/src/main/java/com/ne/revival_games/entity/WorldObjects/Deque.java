package com.ne.revival_games.entity.WorldObjects;

import com.ne.revival_games.entity.WorldObjects.Entity.Entity;

/**
 * Created by Veganova on 8/7/2017.
 */

public class Deque {


    Deque() {

    }



    class Node {
        private Node right, left;

        private Entity content;

        Node(Entity content, Node right, Node left) {
            this.content = content;
            this.right = right;
            this.left = left;
        }
    }

    class Sentinel {

    }


}
