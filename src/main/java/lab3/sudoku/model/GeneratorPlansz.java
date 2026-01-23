package lab3.sudoku.model;

import java.util.Random;

/**
 *@author Ążej
 * generator pełnej planszy sudoku
 * Tworzy losowe, poprawne sudoku 9x9, rozwiązuje je i usuwa określoną liczbę pól, aby stworzyć łamigłówkę
 * Używany w SudokuGame do generowania nowych gier.
 */

public class GeneratorPlansz {
    private SudokuBoard board;
    private Random random = new Random();

    public GeneratorPlansz() {
        this.board = new SudokuBoard();
    }

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

    // rozwiazywanie przez uzytkownika
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
                //przed usunięciem usawi ć poprawną odpowiedź (correctValue)
                board.getField(row, col).setCorrectValue(board.getField(row, col).getValue());
                
                board.getField(row, col).setValue(0); 
                countToRemove--;
            }
        }
    }

}





