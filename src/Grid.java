public class Grid {
    private PathCell startPathCell;
    private PathCell endPathCell;

    public final PathCell[][] cells;
    public final boolean[][] prohibitedCells;

    public Grid(char[][] map) {
        cells = new PathCell[map.length][map[0].length];
        prohibitedCells = new boolean[map.length][map[0].length];
    }

    public PathCell getStartPathCell() {
        return startPathCell;
    }

    public void setStartPathCell(PathCell startPathCell) {
        this.startPathCell = startPathCell;
    }

    public PathCell getEndPathCell() {
        return endPathCell;
    }

    public void setEndPathCell(PathCell endPathCell) {
        this.endPathCell = endPathCell;
    }

}
