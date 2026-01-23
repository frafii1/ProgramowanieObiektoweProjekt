package lab3.sudoku.exception;

/**
 * Bazowy wyjątek aplikacji Sudoku.
 * Używany do sygnalizowania błędów logiki/operacji w domenie Sudoku.
 */
public class SudokuException extends RuntimeException {

    public SudokuException(String message) {
        super(message);
    }

    public SudokuException(String message, Throwable cause) {
        super(message, cause);
    }
}
