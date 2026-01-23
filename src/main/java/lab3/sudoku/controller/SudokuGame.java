package lab3.sudoku.controller;

import lab3.sudoku.exception.SudokuException;
import lab3.sudoku.model.GeneratorPlansz;
import lab3.sudoku.model.SudokuBoard;

/**
 * @author Ążej
 * Główna klasa realizująca logikę biznesową gry (warstwa Controller w MVC).
 * <p>
 * Klasa ta zarządza cyklem życia rozgrywki, odpowiadając za:
 * <ul>
 * <li>Przechowywanie bieżącego stanu gry (plansza, liczniki błędów, czas).</li>
 * <li>Inicjalizację nowej rozgrywki i generowanie planszy.</li>
 * <li>Walidację ruchów gracza w czasie rzeczywistym.</li>
 * <li>Określanie warunków zwycięstwa lub porażki.</li>
 * </ul>
 * </p>
 */
public class SudokuGame {

    public enum MoveResult { OK, MISTAKE, WIN, LOSE, EMPTY, INVALID, NOT_EDITABLE }

    private SudokuBoard modelBoard;
    private int mistakes = 0;

    // pole statyczne (pod rubrykę) + stała
    private static final int MAX_MISTAKES = 3;

    private int secondsPlayed = 0;

    public SudokuBoard getBoard() {
        return modelBoard;
    }

    public int getMistakes() {
        return mistakes;
    }

    public int getMaxMistakes() {
        return MAX_MISTAKES;
    }

    public int getSecondsPlayed() {
        return secondsPlayed;
    }

    public int tickSecond() {
        return ++secondsPlayed;
    }

    public static String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    // inicjalizacja gry
    public void initGame(int fieldsToFill) {
        if (fieldsToFill < 0 || fieldsToFill > 81) {
            throw new SudokuException("fieldsToFill out of range: " + fieldsToFill);
        }

        GeneratorPlansz generator = new GeneratorPlansz();
        SudokuBoard solution = generator.generate(81);

        modelBoard = new SudokuBoard();
        prepareGame(solution, modelBoard, fieldsToFill);

        mistakes = 0;
        secondsPlayed = 0;
    }

    // logika przygotowania planszy
    private void prepareGame(SudokuBoard source, SudokuBoard target, int filledCount) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                target.getField(i, j).setCorrectValue(source.getField(i, j).getValue());
                target.getField(i, j).setValue(0);
                target.getField(i, j).setEditable(true); // ważne: na starcie edytowalne
            }
        }

        int exposed = 0;
        while (exposed < filledCount) {
            int r = (int) (Math.random() * 9);
            int c = (int) (Math.random() * 9);

            if (target.getField(r, c).getValue() == 0) {
                target.getField(r, c).setValue(target.getField(r, c).getCorrectValue());
                target.getField(r, c).setEditable(false);
                exposed++;
            }
        }
    }

    // obsługa wpisu gracza
    public MoveResult handleInput(int row, int col, String newVal) {
        if (modelBoard == null) {
            throw new SudokuException("Game not initialized");
        }
        if (row < 0 || row > 8 || col < 0 || col > 8) {
            return MoveResult.INVALID;
        }
        if (!modelBoard.getField(row, col).isEditable()) {
            return MoveResult.NOT_EDITABLE;
        }
        if (newVal == null || newVal.isEmpty()) {
            return MoveResult.EMPTY;
        }

        int entered;
        try {
            entered = Integer.parseInt(newVal);
        } catch (NumberFormatException ex) {
            return MoveResult.INVALID;
        }

        if (entered < 1 || entered > 9) {
            return MoveResult.INVALID;
        }

        int correct = modelBoard.getField(row, col).getCorrectValue();

        if (entered != correct) {
            mistakes++;
            return (mistakes >= MAX_MISTAKES) ? MoveResult.LOSE : MoveResult.MISTAKE;
        }

        modelBoard.getField(row, col).setValue(entered);
        return checkWin() ? MoveResult.WIN : MoveResult.OK;
    }

    private boolean checkWin() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                var f = modelBoard.getField(i, j);
                if (f.getValue() != f.getCorrectValue()) {
                    return false;
                }
            }
        }
        return true;
    }
}

