package uet.oop.bomberman.Library;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private int size;

    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to queue");
        }
        Node newNode = new Node(item);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public Item remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        Item item = head.item;
        head = head.next;
        size--;
        if (isEmpty()) {
            tail = null;
        } else {
            head.prev = null;
        }
        return item;
    }

    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class Node {
        private final Item item;
        private Node next;
        private Node prev;

        Node(Item item) {
            this.item = item;
            this.next = null;
            this.prev = null;
        }

        public Item getItem() {
            return item;
        }

        public void setNextNode(Node nextNode) {
            this.next = nextNode;
        }

        public void setPrevNode(Node preNode) {
            this.prev = preNode;
        }

        public Node nextNode() {
            return this.next;
        }

    }

    private class QueueIterator implements Iterator<Item> {
        private Node current;

        QueueIterator() {
            current = head;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to iterate");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Remove operation not supported");
        }
    }
}