package edu.jsu.mcis.cs408.project1;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TicTacToeModel {

    public static final int DEFAULT_SIZE = 3;

    private Mark[][] grid;      /* the game grid */
    private boolean xTurn;      /* is TRUE if X is the current player */
    private int size;           /* the size (width and height) of the game grid */

    private TicTacToeController controller;

    protected PropertyChangeSupport propertyChangeSupport;

    public TicTacToeModel(TicTacToeController controller, int size) {

        this.size = size;
        this.controller = controller;
        propertyChangeSupport = new PropertyChangeSupport(this);

        resetModel(size);

    }

    public void resetModel(int size) {

        //
        // This method resets the Model to its default state.  It should (re)initialize the size of
        // the grid, (re)set X as the current player, and create a new grid array of Mark objects,
        // initially filled with empty marks.
        //

        this.size = size;
        this.xTurn = true;

        /* Create grid (width x width) as a 2D Mark array */

        //Created a nested for loop to reset the Model. Make sure to set it to it's default state and reinitialize the size of it.
        grid = new Mark[size][size];
        for (int row = 0; row < DEFAULT_SIZE; ++row) {
            for (int col = 0; col < DEFAULT_SIZE; ++col) {
                grid[row][col] = Mark.EMPTY;
            }
        }

    }

    public boolean setMark(TicTacToeSquare square) {

        //
        // This method accepts the target square as a TicTacToeSquare argument, and adds the
        // current player's mark to this square.  First, it should use "isValidSquare()" to check if
        // the specified square is within range, and then it should use "isSquareMarked()" to see if
        // this square is already occupied!  If the specified location is valid, make a mark for the
        // current player, then use "firePropertyChange()" to fire the corresponding property change
        // event, which will inform the Controller that a change of state has taken place which
        // requires a change to the View.  Finally, toggle "xTurn" (from TRUE to FALSE, or vice-
        // versa) to switch to the other player.  Return TRUE if the mark was successfully added to
        // the grid; otherwise, return FALSE.
        //

        int row = square.getRow();
        int col = square.getCol();

        //MY CODE!!!
        boolean markMade;
        markMade = false;
        if ( (isValidSquare(row, col)) && (!isSquareMarked(row, col)) ) {
            if (xTurn) {
                grid[row][col] = Mark.X;
                firePropertyChange(TicTacToeController.SET_SQUARE_X, this, square);
                xTurn = false;
            }
            else {
                grid[row][col] = Mark.O;
                firePropertyChange(TicTacToeController.SET_SQUARE_O, this, square);
                xTurn = true;
            }
            markMade = true;

        }

        return markMade;

    }

    private boolean isValidSquare(int row, int col) {

        // This method should return TRUE if the specified location is within bounds of the grid

        //
        boolean validSquareBOOL;
        validSquareBOOL = false;
        if( (row >= 0 && row < size) && (col >= 0 && col < size) ) {
            validSquareBOOL = true;
        }
        return validSquareBOOL;

    }

    private boolean isSquareMarked(int row, int col) {

        // This method should return TRUE if the square at the specified location is already marked
        boolean squareMarkedBOOL;
        squareMarkedBOOL = false;

        if((getMark(row, col) == Mark.X) || (getMark(row, col) == Mark.O)) { squareMarkedBOOL = true; }

        return squareMarkedBOOL;

    }

    public Mark getMark(int row, int col) {

        // This method should return the Mark from the square at the specified location

        return grid[row][col];

    }

    public Result getResult() {

        //
        // This method should return a Result value indicating the current state of the game.  It
        // should use "isMarkWin()" to see if X or O is the winner, and "isTie()" to see if the game
        // is a TIE.  If neither condition applies, return a default value of NONE.
        //

        //Check for a winner
        if (isMarkWin(Mark.X)) {
            //Return X as the winner
            return Result.X;
        }else if (isMarkWin(Mark.O)) {
            //Returns O as the winner
            return Result.O;
        }else if (isTie()) {
            //Returns the Cat got it
            return Result.TIE;
        }else {
            return Result.NONE;
        }

    }

    private boolean isMarkWin(Mark mark) {

        //
        // This method should check the squares of the grid to see if the specified Mark is the
        // winner.  (Hint: this method must check for complete rows, columns, and diagonals, using
        // an algorithm which will work for all possible grid sizes!)
        //

        //Check for a winner vertically horizontally and diagonally
        int count = size - 1;
        boolean winTheGame;
        winTheGame = false;
        String userChoice = "";

        String horizontalWin, verticalWin, diagonalWinOne, diagonalWinTwo;
        horizontalWin = verticalWin = diagonalWinOne = diagonalWinTwo = "";

        for (int j = 0; j < size; ++j) {
            userChoice += mark.toString();
        }

        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                horizontalWin += (grid[row][col]).toString();
                verticalWin += (grid[col][row]).toString();
            }
            diagonalWinOne += (grid[row][row]).toString();
            diagonalWinTwo += (grid[row][count]).toString();

            count--;

            //Check for Three in a Row - If we have a winner, then set the boolean winTheGame to TRUE
            if ((userChoice.equals(horizontalWin)) || (userChoice.equals(verticalWin)) || (userChoice.equals(diagonalWinOne)) || (userChoice.equals(diagonalWinTwo)))
            {
                winTheGame = true;
                break;
            }else {
                winTheGame = false;
            }

            verticalWin = horizontalWin = "";
        }

        return winTheGame;

    }

    private boolean isTie() {

        boolean isTie = false;
        int count = 0;

        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {

                if (isSquareMarked(row, col)) {
                    count++;
                }

            }
        }

        if ((isMarkWin(Mark.X) == false) && (isMarkWin(Mark.O) == false) && (count == size * size)) {
            isTie = true;
        }

        return isTie;

    }

    public boolean isXTurn() {

        // Getter for "xTurn"
        return xTurn;

    }

    public int getSize() {

        // Getter for "size"
        return size;

    }

    // Property Change Methods (adds/removes a PropertyChangeListener, or fires a property change)

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    /* ENUM TYPE DEFINITIONS */

    // Mark (represents X, O, or an empty square)

    public enum Mark {

        X("X"),
        O("O"),
        EMPTY("-");

        private String message;

        private Mark(String msg) {
            message = msg;
        }

        @Override
        public String toString() {
            return message;
        }

    };

    // Result (represents the game state: X wins, O wins, a TIE, or NONE if the game is not over)

    public enum Result {

        X("X"),
        O("O"),
        TIE("TIE"),
        NONE("NONE");

        private String message;

        private Result(String msg) {
            message = msg;
        }

        @Override
        public String toString() {
            return message;
        }

    };

}