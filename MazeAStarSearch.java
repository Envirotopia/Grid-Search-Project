/*
file name:      MazeAStarSearch.java
Authors:        Colin Maloney
last modified:  04/26/2026

Provides functionality of a MazeAStarSearch object. 

*/

import java.util.Comparator;

public class MazeAStarSearch extends AbstractMazeSearch{
    
    private PriorityQueue<Cell> priorityQueue;

    /**
     * Constructs an A* maze searcher for the provided maze.
     * 
     * @param maze the maze to search.
     */
    public MazeAStarSearch(Maze maze){
        super(maze);
        Comparator<Cell> comparator = new Comparator<Cell>(){
            @Override
            public int compare(Cell o1, Cell o2) {
                return MazeAStarSearch.this.compare(o1, o2);
            }
        };
        this.priorityQueue = new Heap<Cell>(comparator);
    }

    /**
     * Compares two cells by estimated total A* cost.
     * 
     * @param cell1 the first cell to compare.
     * @param cell2 the second cell to compare.
     * @return a negative value if {@code cell1} has higher priority, a positive
     *         value if {@code cell2} has higher priority, or {@code 0} if tied.
     */
    public int compare(Cell cell1, Cell cell2){
        // Get steps to reach each cell by counting the traceback path
        LinkedList<Cell> path1 = traceback(cell1);
        LinkedList<Cell> path2 = traceback(cell2);
        
        int steps1 = (path1 == null) ? 0 : path1.size();
        int steps2 = (path2 == null) ? 0 : path2.size();
        
        // Calculate lower bound estimate to target
        int bound1 = Math.abs(cell1.getRow() - getTarget().getRow()) + 
                     Math.abs(cell1.getCol() - getTarget().getCol());
        int bound2 = Math.abs(cell2.getRow() - getTarget().getRow()) + 
                     Math.abs(cell2.getCol() - getTarget().getCol());
        
        // Calculate total cost (steps taken + estimated steps remaining)
        int cost1 = steps1 + bound1;
        int cost2 = steps2 + bound2;
        
        // Return negative if cell1 has higher priority (lower total cost)
        return cost1 - cost2;
    }

    /**
     * Removes and returns the next lowest-cost cell from the frontier.
     * 
     * @return the next cell to explore.
     */
    @Override
    public Cell findNextCell(){
        return this.priorityQueue.poll();
    }

    /**
     * Adds a cell to the A* frontier.
     * 
     * @param next the cell to add.
     */
    @Override
    public void addCell(Cell next){
        this.priorityQueue.offer(next);
    }

    /**
     * Updates a cell's ordering in the A* frontier.
     * 
     * @param next the cell whose priority changed.
     */
    @Override
    public void updateCell(Cell next){
        updatePriority(this.priorityQueue, next);
    }

    /**
     * Returns the number of cells still waiting to be explored.
     * 
     * @return the number of remaining cells.
     */
    @Override
    public int numRemainingCells(){
        return this.priorityQueue.size();
    }

}
