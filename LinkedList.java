/*
file name:      LinkedList.java
Authors:        Colin Maloney
last modified:  03/20/2026

Provides functionality of a LinkedList object. 

*/

import java.util.Iterator;

public class LinkedList<T> implements Queue<T>, Stack<T>, Iterable<T>{
    private static class Node<T>{

        Node<T> next;
        T field;

        /**
         * Constructs a Node with the given item.
         * 
         * @param item the data to be stored in this node
         */
        public Node(T item){
            this.next = null;
            this.field = item;
        }

        /**
         * Retrieves the data stored in this node.
         * 
         * @return the data stored in this node
         */
        public T getData(){
            return this.field;
        }

        /**
         * Sets the next node in the linked list.
         * 
         * @param n the next node to link to
         */
        public void setNext(Node<T> n){
            this.next = n; 
        }

        /**
         * Retrieves the next node in the linked list.
         * 
         * @return the next node, or null if there is no next node
         */
        public Node<T> getNext(){
            return this.next;
        }

    }

    private class LLIterator implements Iterator<T>{
        
        Node<T> nextTraverse;

        /**
         * Constructs a LLIterator starting at the given head node.
         * 
         * @param head the starting node for iteration
         */
        public LLIterator(Node<T> head){
            this.nextTraverse = head;
        }

        /**
         * Determines if there are more elements to iterate through.
         * 
         * @return true if there is a next element, false otherwise
         */
        @Override
        public boolean hasNext(){
            return this.nextTraverse !=null;
        }

        /**
         * Retrieves the next element in the iteration.
         * 
         * @return the next element in the linked list, or null if traversal is complete
         */
        @Override
        public T next(){
            if(nextTraverse ==null && nextTraverse.getNext()==null){
                return null;
            }
            T nextItem = this.nextTraverse.getData();
            this.nextTraverse = this.nextTraverse.getNext();
            return nextItem;
        }


    }

    Node<T> head;
    Node<T> tail;
    int size;

    /**
     * Constructs an empty LinkedList with no elements.
     */
    public LinkedList(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Returns the number of elements currently in this linked list.
     * 
     * @return the number of elements in this linked list
     */
    @Override
    public int size(){
        return this.size;
    }

    /**
     * Clears all elements from this linked list, making it empty.
     */
    public void clear(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Determines if this linked list is empty.
     * 
     * @return true if the linked list contains no elements, false otherwise
     */
    public boolean isEmpty(){
        return this.size == 0;
    }

    /**
     * Returns a string representation of this linked list.
     * 
     * @return a string showing all elements in the linked list
     */
    @Override
    public String toString(){
        Node<T> n = this.head;
        String linkedListString = "";
        if(n!= null){
            linkedListString += "LinkedList as String: ";
            while(n!= null){
                linkedListString += n.getData()+" ";
                n = n.getNext();
            }
        }
        return linkedListString;
    }

    /**
     * Adds an element to the front of this linked list.
     * 
     * @param item the element to add
     */
    public void add(T item){
        Node<T> n = new Node<T>(item);
        if(this.head != null){
            Node<T> temp = this.head;
            this.head = n;
            this.head.setNext(temp);
            this.size++;
        } else{
            this.head = n;
            this.tail = n;
            this.size++;
        }
    }

    /**
     * Retrieves the element at the specified index in this linked list.
     * 
     * @param index the position of the element to retrieve
     * @return the element at the specified index, or null if index is out of bounds
     */
    public T get(int index){
        Node<T> n = this.head;
        int i =0;
        if(index == this.size-1){
            this.getLast();
        }
        while(i<=index){
            if(i==index){
                return n.getData();
            }
            n = n.getNext();
            i++;
        }
        return null;
    }

    /**
     * Adds an element at the specified index in this linked list.
     * 
     * @param index the position where the element should be inserted
     * @param item the element to add
     */
    public void add(int index, T item){
        Node<T> newNode = new Node<T>(item);
        if(this.head== null || index == 0){
            this.add(item);
        } 
        else if(index == this.size){
            this.addLast(item);
        } else if (this.head != null) {
            Node<T> n = this.head;
            int i = 0;
            while(i<(index-1)&& n.getNext()!= null){
                n = n.getNext();
                i++;
            }
            newNode.setNext(n.getNext());
            n.setNext(newNode);
            this.size++;
        }
    }

    /**
     * Determines if this linked list contains the specified element.
     * 
     * @param o the element to search for
     * @return true if the element is found in the linked list, false otherwise
     */
    public boolean contains(Object o){
        Node<T> n = this.head;
        int i = 0;
        while(i<this.size && n!= null){
            if(n.getData().equals(o)){
                return true;
            }
            n = n.getNext();
            i++;
        }
        return false;
    }

    /**
     * Removes and returns the first element from this linked list.
     * 
     * @return the first element that was removed, or null if the list is empty
     */
    public T remove(){
        if(this.head == null){
            this.tail = this.head;
        }
        T temp = this.head.getData();
        this.head = this.head.getNext();
        if(this.size == 1){
            this.tail = this.head;
        }
        this.size--;
        return temp;
    }

    public T remove(int index){
        if(head.getNext()==null){
            return null;
        }
        if(index==0){
            T temp = this.head.getData();
            this.head = this.head.getNext();
            this.size--;
            return temp;
        }
        if(index == this.size-1){
            this.removeLast();
        }
        Node<T> n = this.head;
        int i =0;
        while(i<index-1 && n.getNext()!=null){
            n = n.getNext();
            i++;
        }

        if(n.getNext()!= null){
            Node<T> deleteNode = n.getNext();
            if(deleteNode.getNext()== null){
                return deleteNode.getData();
            }
            n.setNext(deleteNode.getNext());
            this.size--;
            return deleteNode.getData();
        }
        return null;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof LinkedList<?>)){
            return false;
        }
        LinkedList<?> oAsALinkedList = (LinkedList<?>) o;
        Node<?> n = this.head;
        Node<?> oNode = oAsALinkedList.head;
        while(n!=null && oNode != null){
            if(!n.getData().equals(oNode.getData())){
                return false;
            }
            n = n.getNext();
            oNode = oNode.getNext();
        }
        return n == null && oNode == null;
    }

    // Return a new LLIterator pointing to the head of the list
    @Override
    public Iterator<T> iterator() {
        return new LLIterator( this.head );
    }

    public void addLast(T item){
        if(this.tail == null){
            this.tail = new Node<T>(item);
            this.head = this.tail;
        } else{
            this.tail.setNext(new Node<T>(item));
            this.tail = this.tail.next;
        }
        this.size++;
    }

    public T removeLast(){
        if(this.tail==null || this.head == null){
            return null;
        }
        Node<T> n = this.head;
        while(n!=null && n.next.next != null){
            n = n.next;
        }
        Node<T> temp = this.tail;
        this.tail = n;
        this.size--;
        return temp.getData();
    }

    public T getLast(){
        if(this.tail == null){
            return null;
        }
        return this.tail.getData();
    }

    @Override
    public void offer(T item){
        this.addLast(item);
    }

    @Override
    public T poll(){
        T deletedItem = this.remove();
        return deletedItem;
    }

    @Override
    public T peek(){
        if(this.head == null){
            return null;
        }
        return this.head.getData();
    }

    @Override
    public void push(T item){
        this.add(item);
    }

    @Override
    public T pop(){
        return this.remove();
    }

}
