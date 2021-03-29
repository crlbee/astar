import java.util.Comparator;
import java.util.PriorityQueue;

public class RouteFinderImpl implements RouteFinder {
    private Cell[][] grid;
    private PriorityQueue<Cell> openCells;
    private boolean[][] closedCells;

    private final int MOVE_COST = 10;
    private final char END_POINT = 'X';
    private final char START_POINT = '@';
    private final char WALL = '#';

    private Cell startCell;
    private Cell endCell;

    public char[][] findRoute(char[][] map) {
        grid = new Cell[map.length][map[0].length];
        closedCells = new boolean[map.length][map[0].length];
        openCells = new PriorityQueue<>(Comparator.comparing(Cell::getFinalCost));
        processMap(map);
        if (startCell == null || endCell == null)
            return null;
        return hasPath(map) ? map : null;
    }

    void processMap(char[][] map) {
        for(int y = 0; y < grid.length; ++y) {
            for(int x = 0; x < grid[y].length; ++x) {
                if (map[y][x] == WALL) {
                    closedCells[y][x] = true;
                }
                if (map[y][x] == START_POINT) {
                    grid[y][x] = new Cell(y, x);
                    startCell = grid[y][x];
                    openCells.add(grid[startCell.y][startCell.x]);
                }
                if (map[y][x] == END_POINT) {
                    grid[y][x] = new Cell(y, x);
                    endCell = grid[y][x];
                }
            }
        }
    }


    public boolean hasPath(char[][] map) {
        while(true) {
            Cell current = openCells.poll();
            if (current == null) {
                return false;
            }
            if (current.equals(endCell)) {
                printPath(map, current.parent);
                return true;
            }
            closedCells[current.y][current.x] = true;
            Cell cellToMove;
            if (current.x - 1 >= 0) {
                cellToMove = createCell(current.x - 1, current.y);
                updateCostIfNeed(current, cellToMove, cellToMove.getFinalCost() + MOVE_COST);
            }
            if (current.x + 1 < grid[0].length) {
                cellToMove = createCell(current.x + 1, current.y);
                updateCostIfNeed(current, cellToMove, cellToMove.getFinalCost() + MOVE_COST);
            }
            if (current.y - 1 >= 0) {
                cellToMove = createCell(current.x, current.y - 1);
                updateCostIfNeed(current, cellToMove, cellToMove.getFinalCost() + MOVE_COST);
            }
            if (current.y + 1 < grid.length) {
                cellToMove = createCell(current.x, current.y + 1);
                updateCostIfNeed(current, cellToMove, cellToMove.getFinalCost() + MOVE_COST);
            }
        }
    }

    public Cell createCell(int x, int y) {
        grid[y][x] = new Cell(y, x);
        grid[y][x].setHeuristicCost(Math.abs(y - endCell.y) + Math.abs(x - endCell.x));
        return grid[y][x];
    }

    public void updateCostIfNeed(Cell current, Cell cellToMove, int cost) {
        if (closedCells[cellToMove.y][cellToMove.x]){
            return ;
        }
        int finalCost = cellToMove.getHeuristicCost() + cost;
        boolean notInOpenCells = !openCells.contains(cellToMove);
        if (notInOpenCells || finalCost < cellToMove.getFinalCost()) {
            cellToMove.setFinalCost(finalCost);
            cellToMove.parent = current;
            if (notInOpenCells) {
                openCells.add(cellToMove);
            }
        }
    }

    void printPath(char[][] map, Cell currentCell) {
        while(currentCell.parent != null) {
            map[currentCell.y][currentCell.x] = '+';
            currentCell = currentCell.parent;
        }
    }

}