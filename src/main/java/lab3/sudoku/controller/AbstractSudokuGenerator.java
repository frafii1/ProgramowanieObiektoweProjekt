package lab3.sudoku.model;

public abstract class AbstractSudokuGenerator {

    
    protected abstract SudokuBoard generateSolvedBoard();


    public SudokuBoard generate(int filledFields) {
        SudokuBoard solved = generateSolvedBoard();
        return solved; //  rozwiązanie
    }
}
