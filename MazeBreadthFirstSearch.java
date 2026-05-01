/*
file name:      MazeBreadthFirstSearch.java
Authors:        Colin Maloney
last modified:  04/26/2026

Provides functionality of a MazeBreadthFirstSearch object. 

*/

public class MazeBreadthFirstSearch extends AbstractMazeSearch{
    
    private Queue<Cell> queue;

    /**
     * Constructs a breadth-first maze searcher for the provided maze.
     * 
     * @param maze the maze to search.
     */
    public MazeBreadthFirstSearch(Maze maze){
        super(maze);
        this.queue = new LinkedList<>();
    }

    /**
     * Removes and returns the next cell to explore from the queue.
     * 
     * @return the next cell to explore.
     */
    @Override
    public Cell findNextCell(){
        return this.queue.poll();
    }

    /**
     * Enqueues a cell to be explored later.
     * 
     * @param next the cell to add.
     */
    @Override
    public void addCell(Cell next){
        this.queue.offer(next);
    }

    /**
     * Updates a cell's priority in the underlying frontier structure.
     * <p>
     * Breadth-first search does not use priorities, so this is a no-op.
     * 
     * @param next the cell whose priority would be updated.
     */
    @Override
    public void updateCell(Cell next){
    }

    /**
     * Returns the number of cells still waiting to be explored.
     * 
     * @return the number of remaining cells.
     */
    @Override
    public int numRemainingCells(){
        return this.queue.size();
    }
}
