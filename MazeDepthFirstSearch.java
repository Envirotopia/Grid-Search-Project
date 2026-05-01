/*
file name:      MazeDepthFirstSearch.java
Authors:        Colin Maloney
last modified:  04/26/2026

Provides functionality of a MazeDepthFirstSearch object. 

*/

public class MazeDepthFirstSearch extends AbstractMazeSearch{
    
    private Stack<Cell> stack;

    /**
     * Constructs a depth-first maze searcher for the provided maze.
     * 
     * @param maze the maze to search.
     */
    public MazeDepthFirstSearch(Maze maze){
        super(maze);
        stack = new LinkedList<>();
    }

    /**
     * Removes and returns the next cell to explore from the stack.
     * 
     * @return the next cell to explore.
     */
    @Override
    public Cell findNextCell(){
        return stack.pop();
    }

    /**
     * Pushes a cell onto the stack of pending cells.
     * 
     * @param next the cell to add.
     */
    @Override
    public void addCell(Cell next){
        stack.push(next);
    }

    /**
     * Updates a cell's priority in the underlying frontier structure.
     * <p>
     * Depth-first search does not use priorities, so this is a no-op.
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
        return stack.size();
    }

}
