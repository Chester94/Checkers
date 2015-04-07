package company.com.checkers;

/**
 * Interface to handle the situation of losing.
 */
interface Lose {
    void losing();
}

/**
 * The main application class.
 * Stores information about the current state of the game.
 * Playing field, the current progress, active (raised) chip handles moves,
 * attacks and multiple attacks.
 */
public class GameModel {
    /**
     * The dimensions of the playing field.
     */
    public static final int GRID_DIMENSION = 8;

    /**
     * checkers no.
     */
    public static final int NONE = 0;

    /**
     * White checker.
     */
    public static final int WHITE = 1;

    /**
     * Black checker.
     */
    public static final int BLACK = 2;

    /**
     * White queen checker
     */
    public static final int WHITE_QUEEN = 3;

    /**
     * Black queen checker
     */
    public static final int BLACK_QUEEN = 4;

    /**
     * Current turn color.
     */
    private int turn = WHITE;

    /**
     * Game field.
     */
    private int[][] grid;

    /**
     * Row active checkers
     */
    private int activeCheckerRow = -1;

    /**
     * Column active checkers
     */
    private int activeCheckerColumn = -1;

    /**
     * true - there is an active checker
     * false - no active checkers
     */
    private boolean active = false;


    /**
     * Row checkers who have committed multiple attack
     */
    private int comboAttackerRow = -1;

    /**
     * Column checkers who have committed multiple attack
     */
    private int comboAttackerColumn = -1;

    /**
     * true - Occurs Multiattack
     * false - There is not a multiple attack
     */
    private boolean attackStreak = false;

    /**
     * Handler loss
     */
    private Lose loserListener = null;

    /**
     * Constructor.
     */
    public GameModel() {
        grid = new int [GRID_DIMENSION][GRID_DIMENSION];
        initGame();
    }

    /**
     * Setter
     * @param loserListener handler loss
     */
    public void setLoserListener(Lose loserListener) {
        this.loserListener = loserListener;
    }

    /**
     * Initialization of the game. Assigning initial values.
     */
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

    /**
     * Start step.
     * @param row row checkers
     * @param column column checkers
     */
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

    /**
     * Stop step.
     * @param row row checkers
     * @param column column checkers
     */
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

    /**
     * Getter.
     * @return current turn color
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Getter.
     * @return row activated checkers
     */
    public int getActiveCheckerRow() {
        return activeCheckerRow;
    }

    /**
     * Getter.
     * @return column activated checkers
     */
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

    /**
     * "Raises" checker
     * @param row row activated checkers
     * @param column column activated checkers
     */
    private void activateDragging(int row, int column) {
        activeCheckerRow = row;
        activeCheckerColumn = column;
        active = true;
    }

    /**
     * "lowers" checker
     */
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

    /**
     * Verifying current player to make a move checkers
     * @return available - true, non-available - false
     */
    private boolean areMovesAvailableForPlayer() {
        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if ( areMovesAvailable(i, j) )
                    return true;
            }
        }
        return false;
    }

    /**
     * Verifying current player to make a certain move checkers
     * @param row row checkers
     * @param column column checkers
     * @return available - true, non-available - false
     */
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

    /**
     * Verifying current player to make a certain move checkers up
     * @param row row checkers
     * @param column column checkers
     * @return available - true, non-available - false
     */
    private boolean areMovesAvailableAbove(int row, int column) {
        if (row == 0)
            return false;

        if( column > 0 && grid[row - 1][column - 1] == NONE )
            return true;

        if ( column < (GRID_DIMENSION - 1) && grid[row - 1][column + 1] == NONE )
            return true;

        return false;
    }

    /**
     * Verifying current player to make a certain move checkers down
     * @param row row checkers
     * @param column column checkers
     * @return available - true, non-available - false
     */
    private boolean areMovesAvailableBelow(int row, int column) {
        if (row == (GRID_DIMENSION - 1))
            return false;

        if( column > 0 && grid[row + 1][column - 1] == NONE )
            return true;

        if ( column < (GRID_DIMENSION - 1) && grid[row + 1][column + 1] == NONE )
            return true;

        return false;
    }

    /**
     * Verifying current player to make any attack with his checker
     * @return available - true, non-available - false
     */
    private boolean areAttackAvailableForPlayer() {
        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (areAttackAvailable(i, j))
                    return true;
            }
        }

        return false;
    }

    /**
     * Verifying current player to make certain checker attack
     * @param row row checkers
     * @param column column checkers
     * @return available - true, non-available - false
     */
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

    /**
     * Check the availability of active checkers move to a point of the field
     * @param toRow  to row checkers
     * @param toColumn to column checkers
     * @return valid - true, non-valid - false
     */
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

    /**
     * Check the availability of a specific attack checkers point field
     * @param fromRow from row checkers
     * @param fromColumn from column checkers
     * @param toRow to row checkers
     * @param toColumn to column checkers
     * @return valid - true, non-valid - false
     */
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

    /**
     * coordinates of the field should not go beyond its borders
     * @param row row checkers
     * @param column to column checkers
     * @return out - true
     */
    private boolean isOutOfField(int row, int column) {
        if( row < 0 || row > (GRID_DIMENSION - 1) ||
                column < 0 || column > (GRID_DIMENSION - 1))
            return true;

        return false;
    }

    /**
     * The owner of a specific checkers
     * @param row row checkers
     * @param column column checkers
     * @return player (turn) color
     */
    private int checkerOwner(int row, int column) {
        if (grid[row][column] == WHITE || grid[row][column] == WHITE_QUEEN)
            return WHITE;

        if (grid[row][column] == BLACK || grid[row][column] == BLACK_QUEEN)
            return BLACK;

        return NONE;
    }

    /**
     * The conversion of the queen, if possible
     * @param row row checkers
     * @param column column checkers
     */
    private void promoteToQueen(int row, int column) {
        if (turn == WHITE && row == 0)
            grid[row][column] = WHITE_QUEEN;

        if (turn == BLACK && row == (GRID_DIMENSION - 1))
            grid[row][column] = BLACK_QUEEN;
    }

    /**
     * Change turn and check for loss
     */
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

    /**
     * Attack specific cells activated checker
     * @param toRow to row checkers
     * @param toColumn to column checkers
     */
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

    /**
     * Motion Activated checkers into specific cell
     * @param toRow to row checkers
     * @param toColumn to column checkers
     */
    private void move(int toRow, int toColumn) {
        grid[toRow][toColumn] = grid[activeCheckerRow][activeCheckerColumn];
        grid[activeCheckerRow][activeCheckerColumn] = NONE;

        promoteToQueen(toRow, toColumn);

        changeTurn();
    }

    /**
     * Sobit loss
     */
    private void throwLose() {
        if (loserListener != null) {
            loserListener.losing();
        }
    }

    /**
     * Belongs to the player's checkers
     * @param row row checkers
     * @param column column checkers
     * @return belong - true, else - false
     */
    private boolean isCheckerBelongsToPlayer(int row, int column) {
        if (turn == WHITE)
            return grid[row][column] == WHITE || grid[row][column] == WHITE_QUEEN;

        if (turn == BLACK)
            return grid[row][column] == BLACK || grid[row][column] == BLACK_QUEEN;

        return false;
    }
}
