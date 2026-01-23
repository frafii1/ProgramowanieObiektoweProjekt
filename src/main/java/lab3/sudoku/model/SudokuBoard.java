package lab3.sudoku.model;

import lab3.sudoku.exception.SudokuException;

/**
 *
 * @author Ążej
 * reprezentacja całej planszy 9x9 wartości tworzy generator planszy
 * przechowuje wszystkie 81 pól typu SudokuSingleField
 * Klasa reprezentująca całą planszę gry Sudoku.
 * <p>
 * Pełni rolę kontenera dla 81 pól typu {@link SudokuSingleField}.
 * Odpowiada za:
 * <ul>
 * <li>Przechowywanie struktury siatki 9x9.</li>
 * <li>Inicjalizację pustych pól przy tworzeniu obiektu.</li>
 * <li>Udostępnianie metod dostępowych do poszczególnych komórek planszy.</li>
 * </ul>
 *
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
    /**
     * Ustawia wartość liczbową w wybranym polu i blokuje jego edycję.
     * <p>
     * Metoda ta jest przeznaczona głównie do inicjalizacji planszy (ustawiania
     * wartości początkowych), ponieważ wywołuje {@code setEditable(false)}.
     *
     * @param x Indeks wiersza (0-8).
     * @param y Indeks kolumny (0-8).
     * @param value Wartość do wpisania w pole (1-9).
     */
    public void setFieldValue(int x, int y, int value) {
        board[x][y].setValue(value);
        board[x][y].setEditable(false); 
    }
}

