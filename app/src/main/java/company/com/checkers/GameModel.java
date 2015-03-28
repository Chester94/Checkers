package company.com.checkers;

public class GameModel {
    public static final int GRID_HEIGHT = 8;
    public static final int GRID_WIDTH = 8;

    public static final int NOTHING = 0;
    public static final int WHITE_CHECKER = 1;
    public static final int BLACK_CHECKER = 2;
    public static final int WHITE_CHECKER_QUEEN = 3;
    public static final int BLACK_CHECKER_QUEEN = 4;

    private int [][] grid  = {
            {NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER},
            {BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING},
            {NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER},
            {NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING},
            {NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING},
            {NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER},
            {WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING},
            {NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER}
    };

    public GameModel() {
    }

    public int[][] getGrid() {
        return grid;
    }
}
