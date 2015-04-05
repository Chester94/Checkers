package company.com.checkers;


import android.util.Log;

public class GameModel {
    public static final int GRID_DIMENSION = 8;

    public static final int NONE = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;
    public static final int WHITE_QUEEN = 3;
    public static final int BLACK_QUEEN = 4;

    private int turn = WHITE;

    private int[][] grid;

    private int activeCheckerRow = -1;
    private int activeCheckerColumn = -1;
    private boolean active = false;

    private int comboAttackerRow = -1;
    private int comboAttackerColumn = -1;
    private boolean attackStreak = false;

    public GameModel() {
        grid = new int [GRID_DIMENSION][GRID_DIMENSION];
        initGame();
    }

    public void initGame() {
        turn = WHITE;

        activeCheckerRow = activeCheckerColumn = -1;
        active = false;

        comboAttackerRow = comboAttackerColumn = -1;
        attackStreak = false;

        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if ( ( i + j ) % 2 != 0 ) {
                    if ( i < 3 ) {
                        grid[i][j] = BLACK;
                        continue;
                    }

                    if ( i > 4 ) {
                        grid[i][j] = WHITE;
                        continue;
                    }

                    grid[i][j] = NONE;
                }
            }
        }
    }

    public void startStep(int row, int column) {
        if (isOutOfField(row, column))
            return;

        if (isCheckerBelongsToPlayer(row, column)) {
            if (areAttackAvailableForPlayer()) {
                if (areAttackAvailable(row, column))
                    activateDragging(row, column);
                return;
            }

            if (areMovesAvailableForPlayer()) {
                if (areMovesAvailable(row, column))
                    activateDragging(row, column);
                return;
            }
        }
    }

    public void stopStep(int row, int column) {
        if (!isActiveDragging())
            return;

        if (areAttackAvailableForPlayer()) {
            Log.d("1111", "yes");
            if (isAttackValid(activeCheckerRow, activeCheckerColumn, row, column))
                attack(row, column);
        }
        else {
            if (isMoveValid(row, column)) {
                Log.d("1111", "yes1");
                move(row, column);
            }
        }

        deactivateDragging();
    }

    public int getTurn() {
        return turn;
    }

    public int getActiveCheckerRow() {
        return activeCheckerRow;
    }

    public int getActiveCheckerColumn() {
        return activeCheckerColumn;
    }

    public boolean isActive() {
        return active;
    }

    public int getComboAttackerRow() {
        return comboAttackerRow;
    }

    public int getComboAttackerColumn() {
        return comboAttackerColumn;
    }

    public boolean isAttackStreak() {
        return attackStreak;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void activateDragging(int row, int column) {
        activeCheckerRow = row;
        activeCheckerColumn = column;
        active = true;
    }

    public void deactivateDragging() {
        activeCheckerRow = -1;
        activeCheckerColumn = -1;
        active = false;
    }

    public boolean isActiveDragging() {
        return active;
    }

    public boolean isActiveDragging(int row, int column) {
        return activeCheckerRow == row && activeCheckerColumn == column;
    }

    public boolean areMovesAvailableForPlayer() {
        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if ( areMovesAvailable(i, j) )
                    return true;
            }
        }
        return false;
    }

    public boolean areMovesAvailable(int row, int column) {
        boolean movesAvailable = false;

        if ( grid[row][column] == NONE )
            return false;

        if ( grid[row][column] != BLACK )
            movesAvailable = movesAvailable || areMovesAvailableAbove(row, column);

        if ( grid[row][column] != WHITE )
            movesAvailable = movesAvailable || areMovesAvailableBelow(row, column);

        return movesAvailable;
    }

    private boolean areMovesAvailableAbove(int row, int column) {
        if (row == 0)
            return false;

        if( column > 0 && grid[row - 1][column - 1] == NONE )
            return true;

        if ( column < (GRID_DIMENSION - 1) && grid[row - 1][column + 1] == NONE )
            return true;

        return false;
    }

    private boolean areMovesAvailableBelow(int row, int column) {
        if (row == (GRID_DIMENSION - 1))
            return false;

        if( column > 0 && grid[row + 1][column - 1] == NONE )
            return true;

        if ( column < (GRID_DIMENSION - 1) && grid[row + 1][column + 1] == NONE )
            return true;

        return false;
    }

    public boolean areAttackAvailableForPlayer() {
        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (areAttackAvailable(i, j))
                    return true;
            }
        }

        return false;
    }

    public boolean areAttackAvailable(int row, int column) {
        if (checkerOwner(row, column) != turn)
            return false;

        if (isAttackValid(row, column, row - 2, column - 2))
            return true;

        if (isAttackValid(row, column, row - 2, column + 2))
            return true;

        if (isAttackValid(row, column, row + 2, column - 2))
            return true;

        if (isAttackValid(row, column, row + 2, column + 2))
            return true;

        return false;
    }

    public boolean isMoveValid(int toRow, int toColumn) {
        if (isOutOfField(toRow, toColumn))
            return false;

        Log.d("1111", "" + activeCheckerRow + " " + activeCheckerColumn + " " + toRow + " " + toColumn);

        if (grid[toRow][toColumn] != NONE)
            return false;

        if (Math.abs(toColumn - activeCheckerColumn) != 1)
            return false;

        if (grid[activeCheckerRow][activeCheckerColumn] != BLACK)
            if (activeCheckerRow - toRow == 1)
                return true;

        if (grid[activeCheckerRow][activeCheckerColumn] != WHITE)
            if (activeCheckerRow - toRow == -1)
                return true;

        return false;
    }

    public boolean isAttackValid(int fromRow, int fromColumn, int toRow, int toColumn) {
        if (isOutOfField(toRow, toColumn))
            return false;

        if (attackStreak && ! ( fromRow == comboAttackerRow && fromColumn == comboAttackerColumn ))
            return false;

        if (Math.abs( fromRow - toRow ) != 2 || Math.abs( fromColumn - toColumn ) != 2 )
            return false;

        if (checkerOwner(fromRow, fromColumn) !=
                checkerOwner((fromRow + toRow) / 2, (fromColumn + toColumn) / 2) &&
                checkerOwner((fromRow + toRow) / 2, (fromColumn + toColumn) / 2) != NONE &&
                checkerOwner( toRow, toColumn ) == NONE )
            return true;

        return false;
    }

    public boolean isOutOfField(int row, int column) {
        if( row < 0 || row > (GRID_DIMENSION - 1) ||
                column < 0 || column > (GRID_DIMENSION - 1))
            return true;

        return false;
    }

    private int checkerOwner(int row, int column) {
        if (grid[row][column] == WHITE || grid[row][column] == WHITE_QUEEN)
            return WHITE;

        if (grid[row][column] == BLACK || grid[row][column] == BLACK_QUEEN)
            return BLACK;

        return NONE;
    }

    private void promoteToQueen(int row, int column) {
        if (turn == WHITE && row == 0)
            grid[row][column] = WHITE_QUEEN;

        if (turn == BLACK && row == (GRID_DIMENSION - 1))
            grid[row][column] = BLACK_QUEEN;
    }

    private void changeTurn() {
        if (turn == WHITE) {
            turn = BLACK;
        }
        else {
            turn = WHITE;
        }

        comboAttackerRow = -1;
        comboAttackerColumn = -1;
        attackStreak = false;
    }

    public void attack(int toRow, int toColumn) {
        grid[toRow][toColumn] = grid[activeCheckerRow][activeCheckerColumn];
        grid[(toRow + activeCheckerRow) / 2][(toColumn + activeCheckerColumn) / 2] = NONE;
        grid[activeCheckerRow][activeCheckerColumn] = NONE;

        promoteToQueen(toRow, toColumn);

        comboAttackerRow = toRow;
        comboAttackerColumn = toColumn;

        if (areAttackAvailable(toRow, toColumn)) {
            attackStreak = true;
        }
        else {
            attackStreak = false;
            changeTurn();
            if (! areMovesAvailableForPlayer() && ! areAttackAvailableForPlayer() )
                throwLose();
        }
    }

    public void move(int toRow, int toColumn) {
        grid[toRow][toColumn] = grid[activeCheckerRow][activeCheckerColumn];
        grid[activeCheckerRow][activeCheckerColumn] = NONE;

        promoteToQueen(toRow, toColumn);

        changeTurn();

        if (! areMovesAvailableForPlayer() && ! areAttackAvailableForPlayer() )
            throwLose();
    }

    private void throwLose() {
        Log.d("1111", "END GAME");
        Log.d("1111", "SOME PROBLEM");
    }

    public boolean isCheckerBelongsToPlayer(int row, int column) {
        if (turn == WHITE)
            return grid[row][column] == WHITE || grid[row][column] == WHITE_QUEEN;

        if (turn == BLACK)
            return grid[row][column] == BLACK || grid[row][column] == BLACK_QUEEN;

        return false;
    }
}
