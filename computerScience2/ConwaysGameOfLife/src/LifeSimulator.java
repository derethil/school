import java.util.Arrays;

public class LifeSimulator {
    private final int sizeX;
    private final int sizeY;
    boolean[][] currentCells;

    public LifeSimulator(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.currentCells = new boolean[this.sizeY][this.sizeX];
    }

    public int getSizeX() { return this.sizeX; }
    public int getSizeY() { return this.sizeY; }

    public boolean getCell(int x, int y) {
        return this.currentCells[y][x];
    }

    public void insertPattern(Pattern pattern, int startX, int startY) {
        for (int row = startY; row < (startY + pattern.getSizeY()); row++) {
            for (int col = startX; col < (startX + pattern.getSizeX()); col++) {
                this.currentCells[row][col] = pattern.getCell(col - startX, row - startY);
            }
        }
    }

    public void update() {
        boolean[][] tempGrid = new boolean[sizeY][sizeX];

        for (int row = 0; row < getSizeY(); row++) {
            for (int col = 0; col < getSizeX(); col++) {

                boolean currCell = this.currentCells[row][col];

                // Finding the neighboring cells this way allows for easy avoidance of IndexOutOfBoundsException errors
                boolean[] neighbors = new boolean[]{
                    (col != 0 && row != 0) && this.currentCells[row - 1][col - 1],                               // Top Left
                    row != 0 && this.currentCells[row - 1][col],                                                 // Top Middle
                    (col != getSizeX() - 1 && row != 0) && this.currentCells[row - 1][col + 1],                  // Top Right
                    col != 0 && this.currentCells[row][col - 1],                                                 // Middle Left
                    col != (getSizeX() - 1) && this.currentCells[row][col + 1],                                  // Middle Right
                    (col != 0 && row != getSizeY() - 1) && this.currentCells[row + 1][col - 1],                  // Bottom Left
                    row != (getSizeY()- 1) && this.currentCells[row + 1][col],                                   // Bottom Middle
                    (row != (getSizeY()- 1) && col != (getSizeX() - 1)) && this.currentCells[row + 1][col + 1]   // Bottom Right
                };

                int neighborsAlive = getNeighborsAlive(neighbors);
                if (currCell && (neighborsAlive == 3 || neighborsAlive == 2)) {
                   tempGrid[row][col] = true;
                } else tempGrid[row][col] = !currCell && (neighborsAlive == 3);
            }
        }

        this.currentCells = tempGrid;
    }

    private int getNeighborsAlive(boolean[] neighbors) {
        int neighborsAlive = 0;

        for (boolean cell: neighbors) {
            if (cell) {
                neighborsAlive++;
            }
        }
        return neighborsAlive;
    }
};