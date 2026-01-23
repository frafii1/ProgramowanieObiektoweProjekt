package lab3.sudoku.model;

import java.util.Random;
import lab3.sudoku.exception.SudokuException;

/**
 * @author Ążej
 * Klasa narzędziowa odpowiedzialna za generowanie nowych plansz Sudoku.
 * <p>
 * Algorytm generowania przebiega w trzech głównych krokach:
 * <ol>
 * <li>Wypełnienie losowymi liczbami bloków 3x3 na przekątnej.</li>
 * <li>Rozwiązanie reszty planszy przy użyciu backtracking.</li>
 * <li>Losowe usunięcie określonej liczby cyfr (ustawiając correctValue przed usunięciem).</li>
 * </ol>
 */
public class GeneratorPlansz extends AbstractSudokuGenerator {

    private SudokuBoard board;
    private final Random random = new Random();

    public GeneratorPlansz() {
        this.board = new SudokuBoard();
    }

    /**
     * abstract method.
     * Generuje pełną, rozwiązaną planszę.
     */
    @Override
    protected SudokuBoard generateSolvedBoard() {
        board = new SudokuBoard();
        fillDiagonal();
        boolean solved = solve(0, 0);

        if (!solved) {
            throw new IllegalStateException("Nie udało się wygenerować poprawnej planszy Sudoku (solver failed).");
        }

        return board;
    }

    /**
     * Główna metoda generująca gotową do gry łamigłówkę.
     * @param filledFields liczba pól, które mają pozostać wypełnione (0..81)
     * @return plansza do gry
     */
    @Override
    public SudokuBoard generate(int filledFields) {
        if (filledFields < 0) filledFields = 0;
        if (filledFields > 81) filledFields = 81;

        // 1) najpierw generujemy PEŁNE rozwiązanie
        SudokuBoard solved = generateSolvedBoard();

        // 2) ustaw correctValue dla WSZYSTKICH pól (ważne dla sprawdzania ruchów)
        for (int r = 0; r < SudokuBoard.SIZE; r++) {
            for (int c = 0; c < SudokuBoard.SIZE; c++) {
                solved.getField(r, c).setCorrectValue(solved.getField(r, c).getValue());
            }
        }

        // 3) usuwamy pola (zostaje filledFields wypełnionych)
        removeDigits(81 - filledFields);

        return solved;
    }

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

                board.getField(row, col).setValue(0);
                board.getField(row, col).setEditable(true); // usunięte pole powinno być edytowalne
                countToRemove--;
            }
        }
    }
}

