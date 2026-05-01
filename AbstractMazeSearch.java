/*
file name:      AbstractMazeSearch.java
Authors:        Colin Maloney
last modified:  04/26/2026

Provides functionality of an AbstractMazeSearch object. 

*/

import java.awt.Color;
import java.awt.Graphics;

public abstract class AbstractMazeSearch{
    
    private Maze maze;
    private Cell start;
    private Cell target;
    private Cell cur;
    private MazeSearchDisplay msDisplay;

    /**
     * Constructs an abstract maze searcher for the provided maze.
     * 
     * @param maze the maze to search.
     */
    public AbstractMazeSearch(Maze maze){
        this.maze = maze;
        this.start = null;
        this.cur = null;
        this.target = null;
    }

    /**
     * Returns the maze used by this searcher.
     * 
     * @return the maze being searched.
     */
    public Maze getMaze(){
        return maze;
    }

    /**
     * Returns the current target cell.
     * 
     * @return the target cell.
     */
    public Cell getTarget(){
        return target;
    }

    /**
     * Sets the current cell being processed.
     * 
     * @param cell the current cell.
     */
    public void setCur(Cell cell){
        cur = cell;
    }

    /**
     * Returns the current cell being processed.
     * 
     * @return the current cell.
     */
    public Cell getCur(){
        return cur;
    }

    /**
     * Returns the starting cell for the current search.
     * 
     * @return the start cell.
     */
    public Cell getStart(){
        return start;
    }

    /**
     * Resets search state for a new run.
     */
    public void reset(){
        cur = null;
        start = null;
        target = null;
    }

    /**
     * Retrieves the next cell to process from the frontier.
     * 
     * @return the next cell to process.
     */
    public abstract Cell findNextCell();
    /**
     * Adds a cell to the search frontier.
     * 
     * @param next the cell to add.
     */
    public abstract void addCell(Cell next);
    /**
     * Updates a cell's priority or position in the frontier.
     * 
     * @param next the cell to update.
     */
    public abstract void updateCell(Cell next);
    /**
     * Returns the number of cells still in the frontier.
     * 
     * @return the number of remaining cells.
     */
    public abstract int numRemainingCells();

    /**
     * Updates a cell's priority within a given priority queue.
     * 
     * @param queue the priority queue to update.
     * @param cell the cell whose priority changed.
     */
    public void updatePriority(PriorityQueue<Cell> queue, Cell cell){
        queue.updatePriority(cell);
    }

    /**
     * Reconstructs a path ending at the given cell by following predecessor links.
     * 
     * @param cell the ending cell for the reconstructed path.
     * @return a linked list containing the reconstructed path, or {@code null} if
     *         no predecessor chain exists.
     */
    public LinkedList<Cell> traceback(Cell cell){
        LinkedList<Cell> cells = new LinkedList<>();

        if(cell.getPrev()==null) return null;

        while(cell.getPrev()!=start){
            cells.add(cell);
            cell = cell.getPrev();
        }

        cells.add(cell);

        return cells;
    }

    /**
     * Runs the maze search from start to target.
     * 
     * @param start the starting cell.
     * @param target the target cell.
     * @param display whether to display the search visualization.
     * @param delay the delay in milliseconds between display updates.
     * @return the discovered path to the target, or {@code null} if no path exists.
     */
    public LinkedList<Cell> search(Cell start, Cell target, boolean display, int delay){
        setCur(start);

        // This line is just to make the drawing work correctly
        start.setPrev(start); 

        this.start = start;
        this.target = target;

        addCell(start);

        if(display== true){
            this.msDisplay = new MazeSearchDisplay(this, 20);
        } else {
            this.msDisplay = null;
        }

        while(numRemainingCells()>0){
            cur = findNextCell();

            if(display == true){
                try{ Thread.sleep(delay); }
                catch(InterruptedException e) {};
            }

            for(int i =0; i<maze.getNeighbors(cur).size(); i++){
                LinkedList<Cell> neighbors = maze.getNeighbors(cur);
                if(neighbors.get(i).getPrev()== null){
                    neighbors.get(i).setPrev(cur);
                    addCell(neighbors.get(i));
                } else if(neighbors.get(i) == target){
                    neighbors.get(i).setPrev(cur);
                    addCell(neighbors.get(i));
                }

                if(neighbors.get(i) == target){
                    return traceback(target);
                }
            }
        }
        return null;
    }

    /**
     * Draws the maze and current search state.
     * 
     * @param g the graphics context used for drawing.
     * @param scale the pixel scale for each maze cell.
     */
    public void draw(Graphics g, int scale) {
        // Draws the base version of the maze
        getMaze().draw(g, scale);
        // Draws the paths taken by the searcher
        getStart().drawAllPrevs(getMaze(), g, scale, Color.RED);
        // Draws the start cell
        getStart().draw(g, scale, Color.BLUE);
        // Draws the target cell
        getTarget().draw(g, scale, Color.RED);
        // Draws the current cell
        getCur().draw(g, scale, Color.MAGENTA);

        // If the target has been found, draws the path taken by the searcher to reach
        // the target sans backtracking.
        if (getTarget().getPrev() != null) {
            Cell traceBackCur = getTarget().getPrev();
            while (!traceBackCur.equals(getStart())) {
                traceBackCur.draw(g, scale, Color.GREEN);
                traceBackCur = traceBackCur.getPrev();
            }
            getTarget().drawPrevPath(g, scale, Color.BLUE);
        }
    }
}

