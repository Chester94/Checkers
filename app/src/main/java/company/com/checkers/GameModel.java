package company.com.checkers;

interface Lose {
    void losing();
}

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

    private Lose loserListener = null;

    public GameModel() {
        grid = new int [GRID_DIMENSION][GRID_DIMENSION];
        initGame();
    }

    public void setLoserListener(Lose loserListener) {
        this.loserListener = loserListener;
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
            if (isAttackValid(activeCheckerRow, activeCheckerColumn, row, column))
                attack(row, column);
        }
        else {
            if (isMoveValid(row, column)) {
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

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public void setActiveCheckerRow(int activeCheckerRow) {
        this.activeCheckerRow = activeCheckerRow;
    }

    public void setActiveCheckerColumn(int activeCheckerColumn) {
        this.activeCheckerColumn = activeCheckerColumn;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setComboAttackerRow(int comboAttackerRow) {
        this.comboAttackerRow = comboAttackerRow;
    }

    public void setComboAttackerColumn(int comboAttackerColumn) {
        this.comboAttackerColumn = comboAttackerColumn;
    }

    public void setAttackStreak(boolean attackStreak) {
        this.attackStreak = attackStreak;
    }

    private void activateDragging(int row, int column) {
        activeCheckerRow = row;
        activeCheckerColumn = column;
        active = true;
    }

    private void deactivateDragging() {
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

    private boolean areMovesAvailableForPlayer() {
        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if ( areMovesAvailable(i, j) )
                    return true;
            }
        }
        return false;
    }

    private boolean areMovesAvailable(int row, int column) {
        boolean movesAvailable = false;

        if (checkerOwner(row, column) != turn)
            return false;

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

    private boolean areAttackAvailableForPlayer() {
        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (areAttackAvailable(i, j))
                    return true;
            }
        }

        return false;
    }

    private boolean areAttackAvailable(int row, int column) {
        if (checkerOwner(row, column) != turn)
            return false;

        if ( grid[row][column] == NONE )
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

    private boolean isMoveValid(int toRow, int toColumn) {
        if (isOutOfField(toRow, toColumn))
            return false;

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

    private boolean isAttackValid(int fromRow, int fromColumn, int toRow, int toColumn) {
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

    private boolean isOutOfField(int row, int column) {
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

        if (! areMovesAvailableForPlayer() && ! areAttackAvailableForPlayer() )
            throwLose();

        comboAttackerRow = -1;
        comboAttackerColumn = -1;
        attackStreak = false;
    }

    private void attack(int toRow, int toColumn) {
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
        }
    }

    private void move(int toRow, int toColumn) {
        grid[toRow][toColumn] = grid[activeCheckerRow][activeCheckerColumn];
        grid[activeCheckerRow][activeCheckerColumn] = NONE;

        promoteToQueen(toRow, toColumn);

        changeTurn();
    }

    private void throwLose() {
        if (loserListener != null) {
            loserListener.losing();
        }
    }

    private boolean isCheckerBelongsToPlayer(int row, int column) {
        if (turn == WHITE)
            return grid[row][column] == WHITE || grid[row][column] == WHITE_QUEEN;

        if (turn == BLACK)
            return grid[row][column] == BLACK || grid[row][column] == BLACK_QUEEN;

        return false;
    }
}
