/*
file name:      Simulation.java
Authors:        Colin Maloney
last modified:  04/26/2026

Provides functionality of a Simulation object. 

*/

public class Simulation {

    private static final int ROWS = 50;
    private static final int COLS = 50;
    private static final int TRIALS_PER_DENSITY = 60;
    private static final double DENSITY_START = 0.00;
    private static final double DENSITY_END = 0.70;
    private static final double DENSITY_STEP = 0.05;
    private static final double NEAR_ZERO_THRESHOLD = 0.01;

    private static final int START_ROW = 1;
    private static final int START_COL = 1;
    private static final int TARGET_ROW = ROWS - 2;
    private static final int TARGET_COL = COLS - 2;

    private static class AlgorithmSummary {
        private int successfulRuns;
        private double totalPathLength;
        private double totalExplored;

        /**
         * Computes the average path length for successful runs.
         * 
         * @return the average path length, or {@code Double.NaN} if there are no
         *         successful runs.
         */
        private double averagePathLength() {
            return successfulRuns == 0 ? Double.NaN : totalPathLength / successfulRuns;
        }

        /**
         * Computes the average number of explored cells for successful runs.
         * 
         * @return the average explored-cell count, or {@code Double.NaN} if there
         *         are no successful runs.
         */
        private double averageExplored() {
            return successfulRuns == 0 ? Double.NaN : totalExplored / successfulRuns;
        }
    }

    private static class DensityResult {
        private final double density;
        private int bfsReachabilitySuccesses;
        private final int trials;
        private final AlgorithmSummary dfs = new AlgorithmSummary();
        private final AlgorithmSummary bfs = new AlgorithmSummary();
        private final AlgorithmSummary astar = new AlgorithmSummary();
        private int tripleSuccesses;

        /**
         * Constructs an aggregate result for one obstacle density.
         * 
         * @param density the tested obstacle density.
         * @param bfsReachabilitySuccesses number of BFS runs that reached the target.
         * @param trials total number of trials run at this density.
         */
        private DensityResult(double density, int bfsReachabilitySuccesses, int trials) {
            this.density = density;
            this.bfsReachabilitySuccesses = bfsReachabilitySuccesses;
            this.trials = trials;
        }
    }

    /**
     * Runs repeated searches at a specific obstacle density and aggregates outcomes.
     * 
     * @param density obstacle density used to generate mazes.
     * @param trials number of random mazes to test.
     * @return aggregated statistics for that density.
     */
    private static DensityResult runDensity(double density, int trials) {
        DensityResult result = new DensityResult(density, 0, trials);

        for (int trial = 0; trial < trials; trial++) {
            Maze maze = new Maze(ROWS, COLS, density);
            maze.get(START_ROW, START_COL).setType(CellType.FREE);
            maze.get(TARGET_ROW, TARGET_COL).setType(CellType.FREE);
            Cell start = maze.get(START_ROW, START_COL);
            Cell target = maze.get(TARGET_ROW, TARGET_COL);

            boolean dfsSuccess = false;
            boolean bfsSuccess = false;
            boolean astarSuccess = false;

            LinkedList<Cell> path = new MazeDepthFirstSearch(maze).search(start, target, false, 0);
            if (path != null) {
                dfsSuccess = true;
                result.dfs.successfulRuns++;
                result.dfs.totalPathLength += path.size();
                int explored = 0;
                for (int row = 0; row < maze.getRows(); row++) {
                    for (int col = 0; col < maze.getCols(); col++) {
                        if (maze.get(row, col).getPrev() != null) {
                            explored++;
                        }
                    }
                }
                result.dfs.totalExplored += explored;
            }

            maze.reset();
            path = new MazeBreadthFirstSearch(maze).search(start, target, false, 0);
            if (path != null) {
                bfsSuccess = true;
                result.bfsReachabilitySuccesses++;
                result.bfs.successfulRuns++;
                result.bfs.totalPathLength += path.size();
                int explored = 0;
                for (int row = 0; row < maze.getRows(); row++) {
                    for (int col = 0; col < maze.getCols(); col++) {
                        if (maze.get(row, col).getPrev() != null) {
                            explored++;
                        }
                    }
                }
                result.bfs.totalExplored += explored;
            }

            maze.reset();
            path = new MazeAStarSearch(maze).search(start, target, false, 0);
            if (path != null) {
                astarSuccess = true;
                result.astar.successfulRuns++;
                result.astar.totalPathLength += path.size();
                int explored = 0;
                for (int row = 0; row < maze.getRows(); row++) {
                    for (int col = 0; col < maze.getCols(); col++) {
                        if (maze.get(row, col).getPrev() != null) {
                            explored++;
                        }
                    }
                }
                result.astar.totalExplored += explored;
            }

            if (dfsSuccess && bfsSuccess && astarSuccess) {
                result.tripleSuccesses++;
            }
        }

        return result;
    }

    /**
     * Formats a numeric table value with two decimals, or NA for missing values.
     * 
     * @param value the value to format.
     * @return the formatted string.
     */
    private static String formatValue(double value) {
        return Double.isNaN(value) ? "NA" : String.format("%.2f", value);
    }

    /**
     * Prints a table showing reachability probability by obstacle density.
     * 
     * @param rows aggregated density results to print.
     */
    private static void printReachabilityResults(java.util.ArrayList<DensityResult> rows) {
        System.out.println("=== Table 1: Reachability vs. Obstacle Density ===");
        System.out.println("Caption: Probability that start can reach target as obstacle density increases.");
        System.out.println("density,success_probability");
        double firstZeroDensity = -1.0;
        double firstNearZeroDensity = -1.0;

        for (DensityResult row : rows) {
            double probability = (double) row.bfsReachabilitySuccesses / row.trials;
            System.out.printf("%.2f, %.3f%n", row.density, probability);
            if (firstZeroDensity < 0.0 && probability == 0.0) {
                firstZeroDensity = row.density;
            }
            if (firstNearZeroDensity < 0.0 && probability < NEAR_ZERO_THRESHOLD) {
                firstNearZeroDensity = row.density;
            }
        }

        if (firstZeroDensity >= 0.0) {
            System.out.printf(
                    "Estimated density where success drops to 0 in this experiment: %.2f%n",
                    firstZeroDensity);
        } else if (firstNearZeroDensity >= 0.0) {
            System.out.printf(
                    "No exact zero observed; success falls below %.2f around density %.2f%n",
                    NEAR_ZERO_THRESHOLD, firstNearZeroDensity);
        } else {
            System.out.println("Success never reached near-zero in scanned density range.");
        }
        System.out.println();
    }

    /**
     * Prints a table of average successful path lengths by algorithm.
     * 
     * @param comparisons aggregated density results to print.
     */
    private static void printPathLengthTable(java.util.ArrayList<DensityResult> comparisons) {
        System.out.println("=== Table 2: Average Path Length by Algorithm ===");
        System.out.println("Caption: Mean path length among successful runs at each density.");
        System.out.println("density,dfs_avg_path,bfs_avg_path,astar_avg_path,dfs_successes,bfs_successes,astar_successes,triple_successes");
        for (DensityResult result : comparisons) {
            System.out.printf("%.2f,%s,%s,%s,%d,%d,%d,%d%n",
                    result.density,
                    formatValue(result.dfs.averagePathLength()),
                    formatValue(result.bfs.averagePathLength()),
                    formatValue(result.astar.averagePathLength()),
                    result.dfs.successfulRuns,
                    result.bfs.successfulRuns,
                    result.astar.successfulRuns,
                    result.tripleSuccesses);
        }
        System.out.println();
    }

    /**
     * Prints a table of average explored-cell counts by algorithm.
     * 
     * @param comparisons aggregated density results to print.
     */
    private static void printExploredCellsTable(java.util.ArrayList<DensityResult> comparisons) {
        System.out.println("=== Table 3: Average Cells Explored by Algorithm ===");
        System.out.println("Caption: Mean number of explored cells among successful runs at each density.");
        System.out.println("density,dfs_avg_explored,bfs_avg_explored,astar_avg_explored");
        for (DensityResult result : comparisons) {
            System.out.printf("%.2f,%s,%s,%s%n",
                    result.density,
                    formatValue(result.dfs.averageExplored()),
                    formatValue(result.bfs.averageExplored()),
                    formatValue(result.astar.averageExplored()));
        }
        System.out.println();
    }

    /**
     * Runs the simulation suite across all configured obstacle densities.
     * 
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        java.util.ArrayList<DensityResult> rows = new java.util.ArrayList<>();
        for (double density = DENSITY_START; density <= DENSITY_END + 1e-9; density += DENSITY_STEP) {
            rows.add(runDensity(density, TRIALS_PER_DENSITY));
        }
        printReachabilityResults(rows);
        printPathLengthTable(rows);
        printExploredCellsTable(rows);
    }
}
