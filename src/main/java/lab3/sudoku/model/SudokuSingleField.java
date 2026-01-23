package lab3.sudoku.model;

import lab3.sudoku.exception.SudokuException;

/**
 *
 * @author Ążej
 * tworzenie pojedynczego kwadratu na planszy w sudoku
 * przechowuje wygenerowaną poprawną wartość planszy (jednego klocka) i informacje czy można edytować pole - isEditable
 */
public class SudokuSingleField {
    private int value;
    private int correctValue;
    private boolean isEditable;

    public SudokuSingleField() {
        this.value = 0;
        this.isEditable = true;
    }

    public SudokuSingleField(int value) {
        this.value = value;
        this.isEditable = (value == 0);  //mozliwość zmiany wartości wpisanej liczby przez gracza
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < 0 || value > 9) {
            return; 
        }
        this.value = value;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
    
    public int getCorrectValue() { return correctValue; }
    public void setCorrectValue(int v) { this.correctValue = v; }
}


