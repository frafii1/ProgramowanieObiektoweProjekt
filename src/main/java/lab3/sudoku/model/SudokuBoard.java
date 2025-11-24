package lab3.sudoku.model;

/**
 *
 * @author Ążej
 * reprezentacja całej planszy 9x9 wartości tworzy generator planszy
 * przechowuje wszystkie 81 pól typu SudokuSingleField
 */
public class SudokuBoard {
    public static final int SIZE = 9;
    private SudokuSingleField[][] board = new SudokuSingleField[SIZE][SIZE];

    public SudokuBoard() {
        // inicjalizacja pustej planszy
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = new SudokuSingleField();
            }
        }
    }

    public SudokuSingleField getField(int x, int y) {
        return board[x][y];
    }
    
    public void setFieldValue(int x, int y, int value) {
        board[x][y].setValue(value);
        board[x][y].setEditable(false); 
    }
}

