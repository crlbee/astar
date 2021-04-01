import java.util.Comparator;
import java.util.PriorityQueue;

public class RouteFinderImpl implements RouteFinder {
    private static final char END_POINT = 'X';
    private static final char START_POINT = '@';
    private static final char WALL = '#';

    public char[][] findRoute(char[][] map) {
        Grid grid;
        try {
            grid = processMap(map);
        } catch (InvalidMapException e) {
            return null;
        }
        if (hasPath(grid)){
            //i change input argument for the optimisation moment
            addPath(map, grid.getEndPathCell().getPrevious());
            return map;
        } else {
            return null;
        }

    }

    Grid processMap(char[][] map) throws InvalidMapException {
        Grid grid = new Grid(map);
        for(int y = 0; y < map.length; ++y) {
            for(int x = 0; x < map[y].length; ++x) {
                if (map[y][x] == WALL) {
                    grid.prohibitedCells[y][x] = true;
                    continue ;
                }
                if (map[y][x] == START_POINT) {
                    if (grid.getStartPathCell() != null)
                        throw new InvalidMapException("too many startpoints");
                    grid.setStartPathCell(new PathCell(x, y));
                }
                if (map[y][x] == END_POINT) {
                    if (grid.getEndPathCell() != null)
                        throw new InvalidMapException("too many endpoints");
                    grid.setEndPathCell(new PathCell(x, y));
                }
            }
        }
        if (grid.getStartPathCell() == null || grid.getEndPathCell() == null)
            throw new InvalidMapException("don't have start or end point");
        return grid;
    }

    boolean hasPath(Grid grid) {
        PriorityQueue<PathCell> candidatesToMove = new PriorityQueue<>(Comparator.comparing(PathCell::getFinalCost));
        candidatesToMove.add(grid.getStartPathCell());
        while(true) {
            PathCell current = candidatesToMove.poll();
            if (current == null) {
                return false;
            }
            if (current.equals(grid.getEndPathCell())) {
                grid.setEndPathCell(current);
                return true;
            }
            grid.prohibitedCells[current.y][current.x] = true;
            if (current.x - 1 >= 0 && isTraversablePathCell(grid, current.x - 1, current.y)) {
                candidatesToMove.add(new PathCell(current.x - 1, current.y, grid, current));
            }
            if (current.x + 1 < grid.cells[0].length && isTraversablePathCell(grid, current.x + 1, current.y)) {
                candidatesToMove.add(new PathCell(current.x + 1, current.y, grid, current));
            }
            if (current.y - 1 >= 0 && isTraversablePathCell(grid, current.x, current.y - 1)) {
                candidatesToMove.add(new PathCell(current.x, current.y - 1, grid, current));
            }
            if (current.y + 1 < grid.cells.length && isTraversablePathCell(grid, current.x, current.y + 1)) {
                candidatesToMove.add(new PathCell(current.x, current.y + 1, grid, current));
            }
        }
    }

    boolean isTraversablePathCell(Grid grid, int x, int y){
        return !grid.prohibitedCells[y][x] && grid.cells[y][x] == null;
    }

    void addPath(char[][] map, PathCell currentPathCell) {
        while(currentPathCell.getPrevious() != null) {
            map[currentPathCell.y][currentPathCell.x] = '+';
            currentPathCell = currentPathCell.getPrevious();
        }
    }
}

class InvalidMapException extends Exception{
    public InvalidMapException(String message) {
        super(message);
    }
}