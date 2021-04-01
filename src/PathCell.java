import java.util.Objects;

public class PathCell {
    private static final int MOVE_COST = 1;

    public final int x;
    public final int y;
    private int heuristicCost;
    private int finalCost;
    private PathCell previous;

    public PathCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PathCell(int x, int y, Grid grid, PathCell current){
        this(x, y);
        heuristicCost = Math.abs(y - grid.getEndPathCell().y) + Math.abs(x - grid.getEndPathCell().x);
        finalCost = heuristicCost + current.getFinalCost() - current.getHeuristicCost() + MOVE_COST;
        previous = current;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public int getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(int finalCost) {
        this.finalCost = finalCost;
    }

    public void setPrevious(PathCell previous) {
        this.previous = previous;
    }

    public PathCell getPrevious() {
        return previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathCell pathCell = (PathCell) o;
        return x == pathCell.x && y == pathCell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
