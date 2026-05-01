/*
file name:      Heap.java
Authors:        Colin Maloney
last modified:  04/26/2026

Provides functionality of a Heap object. 

*/

import java.util.ArrayList;
import java.util.Comparator;

public class Heap<T> implements PriorityQueue<T>{
   
    Comparator<T> comparator;
    ArrayList<T> heap;

    /**
     * Constructs a heap with the given comparator and heap type.
     * 
     * @param comparator the comparator used to determine element priority
     * @param maxHeap true to create a max-heap, false to create a min-heap
     */
    public Heap(Comparator<T> comparator, boolean maxHeap){

        this.heap = new ArrayList<>();

        if(maxHeap){
            this.comparator = new Comparator<T>(){
                @Override
                public int compare(T o1, T o2) {
                    return comparator.compare(o2, o1);
                }
            };
        } else {
            this.comparator = comparator;
        }
    }

    /**
     * Constructs a min-heap with the given comparator.
     * 
     * @param comparator the comparator used to determine element priority
     */
    public Heap(Comparator<T> comparator){
        this(comparator, false);
    }

    /**
     * Swaps two elements in the backing array list.
     * 
     * @param idx1 the index of the first element
     * @param idx2 the index of the second element
     */
    private void swap(int idx1, int idx2){
        T temp = heap.get(idx1);
        heap.set(idx1, heap.get(idx2));
        heap.set(idx2, temp);
    }

    /**
     * Returns the index of the parent of a given node index.
     * 
     * @param idx the index of the child node
     * @return the index of the parent node
     */
    private int getParentIdx( int idx ){
        return (idx-1)/2;
    }

    /**
     * Returns the index of the left child of a given node index.
     * 
     * @param idx the index of the parent node
     * @return the index of the left child
     */
    private int getLeftChildIdx( int idx ){
        return 2*idx+1;
    }

    /**
     * Returns the index of the right child of a given node index.
     * 
     * @param idx the index of the parent node
     * @return the index of the right child
     */
    private int getRightChildIdx( int idx ){
        return 2*idx+2;
    }

    /**
     * Moves an element upward until heap order is restored.
     * 
     * @param idx the starting index of the element to bubble up
     */
    private void bubbleUp( int idx ){
        if(idx == 0) return;
        int pIdx = this.getParentIdx(idx);
        if(this.comparator.compare(this.heap.get(idx), this.heap.get(pIdx))<0){
            this.swap(pIdx, idx);
            this.bubbleUp(pIdx);
        }
    }

    /**
     * Moves an element downward until heap order is restored.
     * 
     * @param idx the starting index of the element to bubble down
     */
    private void bubbleDown(int idx){
        int leftIdx = getLeftChildIdx(idx);
        int rightIdx = getRightChildIdx(idx);

        if(leftIdx >= this.size()) return;

        // If there is only a left child, compare and stop.
        if(rightIdx >= this.size()){
            if(this.comparator.compare(this.heap.get(idx), this.heap.get(leftIdx)) > 0){
                this.swap(idx, leftIdx);
            }
            return;
        }

        // Select the child with higher priority (smaller comparator value).
        int smallerChildIdx = leftIdx;
        if(this.comparator.compare(this.heap.get(rightIdx), this.heap.get(leftIdx)) < 0){
            smallerChildIdx = rightIdx;
        }

        if(this.comparator.compare(this.heap.get(idx), this.heap.get(smallerChildIdx)) > 0){
            this.swap(idx, smallerChildIdx);
            this.bubbleDown(smallerChildIdx);
        }
    }

    /**
     * Returns a sideways tree representation of this heap.
     * 
     * @return a formatted string representation of the heap
     */
    @Override
    public String toString() {
        int depth = 0 ;
        return toString( 0 , depth );
    }
    
    /**
     * Recursively builds a sideways tree string for this heap.
     * 
     * @param idx the current node index
     * @param depth the current depth used for indentation
     * @return the formatted subtree string rooted at idx
     */
    private String toString( int idx , int depth ) {
        if (idx >= this.size() ) {
            return "";
        }
        String left = toString(getLeftChildIdx( idx ) , depth + 1 );
        String right = toString(getRightChildIdx( idx ) , depth + 1 );

        String myself = "\t".repeat(depth) + this.heap.get( idx ) + "\n";
        return right + myself + left;
    }

    /**
     * Returns the number of elements in this heap.
     * 
     * @return the number of elements currently stored
     */
    @Override
    public int size(){
        return this.heap.size();
    }

    /**
     * Returns, but does not remove, the highest-priority element.
     * 
     * @return the element at the root of the heap
     */
    @Override
    public T peek(){
        return this.heap.get(0);
    }

    /**
     * Inserts an item into the heap and restores heap order.
     * 
     * @param item the item to insert
     */
    @Override
    public void offer(T item){
        this.heap.add(item);
        this.bubbleUp(this.size()-1);
    }

    /**
     * Removes and returns the highest-priority element.
     * 
     * @return the removed root element, or null if the heap is empty
     */
    @Override
    public T poll(){
        if(this.size() == 0){
            return null;
        }
        T temp = this.heap.get(0);
        if(this.size() == 1){
            this.heap.remove(0);
            return temp;
        }
        this.heap.set(0, this.heap.get(this.size()-1));
        this.heap.remove(this.size()-1);
        this.bubbleDown(0);
        return temp;
    }

    /**
     * Repositions an updated item to restore heap order.
     * 
     * @param item the item whose priority may have changed
     */
    @Override
    public void updatePriority(T item){
        for (int i = 0; i < this.size(); i++) {
            if(this.heap.get(i).equals(item)){
                this.bubbleUp(i);
                this.bubbleDown(i);
            }
        }
    }

}

