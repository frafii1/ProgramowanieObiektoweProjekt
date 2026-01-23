package lab3.sudoku.model;

import java.util.Random;

/**
 * @author Ążej
 * Klasa narzędziowa odpowiedzialna za generowanie nowych plansz Sudoku.
 * <p>
 * Algorytm generowania przebiega w trzech głównych krokach:
 * <ol>
 * <li>Wypełnienie losowymi liczbami bloków 3x3 na przekątnej (są one niezależne od siebie).</li>
 * <li>Rozwiązanie reszty planszy przy użyciu algorytmu z nawrotami (ang. <i>backtracking</i>), aby uzyskać pełną, poprawną planszę.</li>
 * <li>Losowe usunięcie określonej liczby cyfr, przy jednoczesnym zachowaniu poprawnego rozwiązania w polu {@code correctValue}.</li>
 * </ol>
 */

public class GeneratorPlansz {
    private SudokuBoard board;
    private Random random = new Random();

    public GeneratorPlansz() {
        this.board = new SudokuBoard();
    }
    /**
     * Główna metoda generująca gotową do gry łamigłówkę.
     * <p>
     * Metoda tworzy pełną planszę, a następnie usuwa z niej nadmiarowe pola,
     * pozostawiając jedynie zadaną liczbę wypełnionych komórek (wskazówek).
     *
     * @param filledFields Liczba pól, które mają pozostać wypełnione (poziom trudności).
     * Wartość jest automatycznie przycinana do zakresu 0-81.
     * @return Obiekt {@link SudokuBoard} przygotowany do rozgrywki.
     */
    public SudokuBoard generate(int filledFields) {
        if (filledFields < 0) filledFields = 0;
        if (filledFields > 81) filledFields = 81;

        board = new SudokuBoard();
        fillDiagonal(); //wypelnienie blokow
        solve(0, 0);

        //poziom trudnosci
        removeDigits(81 - filledFields);

        return board;
    }

    /**
     * Rekurencyjna metoda rozwiązująca Sudoku (algorytm Backtracking).
     * <p>
     * Przechodzi po planszy pole po polu. Jeśli napotka puste pole,
     * próbuje wstawić cyfry od 1 do 9, sprawdzając ich poprawność.
     *
     * @param row Aktualny indeks wiersza.
     * @param col Aktualny indeks kolumny.
     * @return {@code true} jeśli uda się rozwiązać planszę od tego momentu,
     * {@code false} jeśli trzeba się cofnąć (nawrót).
     */
    private boolean solve(int row, int col) {
        if (col == 9) { 
            col = 0;
            row++;
            if (row == 9) return true; 
        }

        if (board.getField(row, col).getValue() != 0) {
            return solve(row, col + 1);
        }

        for (int num = 1; num <= 9; num++) {
            if (isValid(row, col, num)) {
                board.getField(row, col).setValue(num);
                if (solve(row, col + 1)) return true;
                board.getField(row, col).setValue(0);
            }
        }
        return false;
    }
    
    /**
     * Sprawdza, czy wstawienie danej liczby w określone pole jest zgodne z zasadami Sudoku.
     * <p>
     * Weryfikuje:
     * <ul>
     * <li>Unikalność w wierszu.</li>
     * <li>Unikalność w kolumnie.</li>
     * <li>Unikalność w bloku 3x3.</li>
     * </ul>
     *
     * @param row Indeks wiersza.
     * @param col Indeks kolumny.
     * @param num Wstawiana liczba.
     * @return {@code true} jeśli ruch jest dozwolony.
     */
    private boolean isValid(int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board.getField(row, i).getValue() == num) return false;
            if (board.getField(i, col).getValue() == num) return false;
        }
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getField(startRow + i, startCol + j).getValue() == num) return false;
            }
        }
        return true;
    }

    private void fillDiagonal() {
        for (int i = 0; i < 9; i = i + 3) {
            fillBox(i, i);
        }
    }

    private void fillBox(int row, int col) {
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = random.nextInt(9) + 1;
                } while (!isSafeInBox(row, col, num));
                board.getField(row + i, col + j).setValue(num);
            }
        }
    }

    private boolean isSafeInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board.getField(rowStart + i, colStart + j).getValue() == num) return false;
            }
        }
        return true;
    }

    private void removeDigits(int countToRemove) {
        while (countToRemove > 0) {
            int cellId = random.nextInt(81);
            int row = cellId / 9;
            int col = cellId % 9;
            
            if (board.getField(row, col).getValue() != 0) {
                //przed usunięciem usawi ć poprawną odpowiedź (correctValue)
                board.getField(row, col).setCorrectValue(board.getField(row, col).getValue());
                
                board.getField(row, col).setValue(0); 
                countToRemove--;
            }
        }
    }

}



