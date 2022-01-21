public class patternBlock extends Pattern {
    private final static int[][] grid = new int[][] {// 1 represents an "alive" cell in the starting position
        {1,1},
        {1,1}
    };

    @Override
    public int getSizeX() { return 2; }

    @Override
    public int getSizeY() { return 2; }

    @Override
    public boolean getCell(int x, int y) {
        return grid[y][x] == 1; // 1 represents an "alive" cell in the starting position
    }

}