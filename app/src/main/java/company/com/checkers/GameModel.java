package company.com.checkers;


import java.util.ArrayList;

public class GameModel {
    public static final int GRID_DIMENSION = 8;

    public static final int NOTHING = 0;
    public static final int WHITE_CHECKER = 1;
    public static final int BLACK_CHECKER = 2;
    public static final int WHITE_CHECKER_QUEEN = 3;
    public static final int BLACK_CHECKER_QUEEN = 4;

    private final int WHITE_GO = 0;
    private final int WHITE_GO_CHECKER = 1;
    private final int BLACK_GO = 2;
    private final int BLACK_GO_CHECKER = 3;

    private final int[] GAME_STATE = {WHITE_GO, WHITE_GO_CHECKER, BLACK_GO, BLACK_GO_CHECKER};
    private int state = 0;
    private boolean activeFlag = false;

    private int activeCheckerType = NOTHING;
    private int activeCheckerRow;
    private int activeCheckerColumn;

    private int [][] grid  = {
            {NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER},
            {BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING},
            {NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER, NOTHING, BLACK_CHECKER},
            {NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING},
            {NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING, NOTHING},
            {WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING},
            {NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER},
            {WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING, WHITE_CHECKER, NOTHING}
    };

    private ArrayList<Integer> availableCells = new ArrayList<>();

    public GameModel() {
    }

    public int[][] getGrid() {
        return grid;
    }

    public void activateChecker(int row, int column) {
        if( row < 0 || row > 7 || column < 0 || column > 7 )
            return;

        if( ! checkCheckerAndState(row, column))
            return;

        activeFlag = true;

        activeCheckerRow = row;
        activeCheckerColumn = column;
        activeCheckerType = grid[row][column];

        grid[row][column] = NOTHING;
    }

    public void deactivateChecker(int row, int column) {
        if( ! activeFlag )
            return;

        if( row < 0 || row > 7 || column < 0 || column > 7 ) {
            resetActiveChecker();
            return;
        }

        activeFlag = false;

        activeCheckerRow = row;
        activeCheckerColumn = column;

        grid[row][column] = activeCheckerType;
        activeCheckerType = NOTHING;
    }

    public int getActiveCheckerType() {
        return activeCheckerType;
    }

    private boolean checkCheckerAndState(int row, int column) {
        if( GAME_STATE[state] == WHITE_GO || GAME_STATE[state] == WHITE_GO_CHECKER )
            if( grid[row][column] == WHITE_CHECKER ||
                    grid[row][column] == WHITE_CHECKER_QUEEN )
                return true;
            else
                return false;

        if( GAME_STATE[state] == BLACK_GO || GAME_STATE[state] == BLACK_GO_CHECKER )
            if( grid[row][column] == BLACK_CHECKER ||
                    grid[row][column] == BLACK_CHECKER_QUEEN )
                return true;
            else
                return false;

        return false;
    }

    private void resetActiveChecker() {
        grid[activeCheckerRow][activeCheckerColumn] = activeCheckerType;
        activeCheckerType = NOTHING;
        activeFlag = false;
    }
}
