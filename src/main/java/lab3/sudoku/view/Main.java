package lab3.sudoku.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lab3.sudoku.model.SudokuBoard;
import lab3.sudoku.model.GeneratorPlansz;

import java.util.Optional;

/**
 * @author Ążej
 * główna klasa widoku aplikacji = JavaFX
 *  wyświetlanie planszy
 *  obsługę UI timer, podświetlania, style.css 
 * przekazywanie wpisów do SudokuGame
 */

public class Main extends Application {

    private SudokuBoard modelBoard;
    private TextField[][] fieldsUI = new TextField[9][9];
    private int mistakes = 0;
    private final int MAX_MISTAKES = 3;
    
    // pola pod timer i gui
    private Label infoLabel;
    private Label timerLabel;
    private Timeline timeline;
    private int secondsPlayed = 0;

    /**
     * Główna metoda startowa aplikacji JavaFX.
     * <p>
     * Odpowiada za:
     * <ol>
     * <li>Wyświetlenie okna wyboru poziomu trudności.</li>
     * <li>Inicjalizację modelu i generatora plansz.</li>
     * <li>Zbudowanie interfejsu graficznego (GridPane, VBox).</li>
     * <li>Załadowanie stylów CSS.</li>
     * <li>Uruchomienie licznika czasu.</li>
     * </ol>
     *
     * @param stage Główny kontener (okno) aplikacji dostarczany przez platformę JavaFX.
     */
    @Override
    public void start(Stage stage) {
        int fieldsToFill = showDifficultyDialog();
        if (fieldsToFill == -1) return;

        GeneratorPlansz generator = new GeneratorPlansz();
        SudokuBoard fullBoard = generator.generate(81);
        
        modelBoard = new SudokuBoard();
        prepareGame(fullBoard, modelBoard, fieldsToFill);

        // błędy + czas, generalnie glowny pasek
        infoLabel = new Label("Błędy: 0 / " + MAX_MISTAKES);
        infoLabel.getStyleClass().add("mistakes-label");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        timerLabel = new Label("00:00");
        timerLabel.getStyleClass().add("timer-label");

        HBox topBar = new HBox(infoLabel, spacer, timerLabel);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("top-bar");
        
        //dodanie timera
        startTimer();

        // plansza
        GridPane grid = new GridPane();
        grid.getStyleClass().add("sudoku-grid");
        grid.setAlignment(Pos.CENTER);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                TextField field = createTextField(row, col);
                fieldsUI[row][col] = field;
                grid.add(field, col, row);
            }
        }

        VBox root = new VBox(10); // 10px między paskiem a siatką
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(topBar, grid);

        Scene scene = new Scene(root, 650, 750);
        String css = getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("Sudoku");
        stage.setScene(scene);
        
        // stop timer
        stage.setOnCloseRequest(e -> stopTimer());
        
        stage.show();
    }

    // 
    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsPlayed++;
            timerLabel.setText(formatTime(secondsPlayed));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }
     /**
     * Formatuje czas w sekundach do formatu MM:SS.
     *
     * @param totalSeconds Liczba sekund, która upłynęła.
     * @return Sformatowany ciąg znaków (np. "05:30").
     */
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Obsługuje logikę wizualnego podświetlania pól na planszy.
     * <p>
     * Metoda wykonuje dwie akcje:
     * <ul>
     * <li>Podświetla "krzyżowo" wiersz i kolumnę zaznaczonego pola.</li>
     * <li>Podświetla wszystkie inne pola na planszy zawierające tę samą wartość.</li>
     * </ul>
     *
     * @param selectedRow Indeks wiersza zaznaczonego pola.
     * @param selectedCol Indeks kolumny zaznaczonego pola.
     */
    private void highlightBoard(int selectedRow, int selectedCol) {
        String valToHighlight = fieldsUI[selectedRow][selectedCol].getText();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                TextField field = fieldsUI[r][c];
                field.getStyleClass().remove("highlight-cross");
                field.getStyleClass().remove("highlight-same");
                
                if (r == selectedRow || c == selectedCol) {
                    if (!(r == selectedRow && c == selectedCol)) {
                        field.getStyleClass().add("highlight-cross");
                    }
                }
                if (!valToHighlight.isEmpty() && field.getText().equals(valToHighlight)) {
                    field.getStyleClass().add("highlight-same");
                }
            }
        }
    }
    /**
     * Tworzy i konfiguruje pojedyncze pole tekstowe (komórkę) siatki Sudoku.
     * <p>
     * Metoda odpowiada za:
     * <ul>
     * <li>Ustawienie rozmiaru i klas CSS (w tym grubych krawędzi dla bloków 3x3).</li>
     * <li>Dodanie obsługi zdarzeń myski i klawiatury (nawigacja strzałkami).</li>
     * <li>Walidację wprowadzanej wartości (tylko cyfry, sprawdzanie poprawności z modelem).</li>
     * <li>Obsługę warunków wygranej/przegranej po wpisaniu wartości.</li>
     * </ul>
     *
     * @param row Indeks wiersza tworzonego pola.
     * @param col Indeks kolumny tworzonego pola.
     * @return Skonfigurowany obiekt {@code TextField}.
     */
    private TextField createTextField(int row, int col) {
        TextField txt = new TextField();
        txt.setPrefSize(55, 55);
        txt.getStyleClass().add("sudoku-field");

        //generowanie bloków 3x3 
        if ((col + 1) % 3 == 0 && col < 8) {
            if ((row + 1) % 3 == 0 && row < 8) txt.getStyleClass().add("border-right-bottom");
            else txt.getStyleClass().add("border-right");
        } else if ((row + 1) % 3 == 0 && row < 8) {
            txt.getStyleClass().add("border-bottom");
        }

        txt.setOnMouseClicked(e -> highlightBoard(row, col));
        txt.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) highlightBoard(row, col);
        });

        //poruszanie sie po planszy strzałkami
        txt.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    if (row > 0) fieldsUI[row - 1][col].requestFocus();
                    break;
                case DOWN:
                    if (row < 8) fieldsUI[row + 1][col].requestFocus();
                    break;
                case LEFT:
                    if (col > 0) fieldsUI[row][col - 1].requestFocus();
                    break;
                case RIGHT:
                    if (col < 8) fieldsUI[row][col + 1].requestFocus();
                    break;
            }
        });

        int val = modelBoard.getField(row, col).getValue();
        if (val != 0) {
            txt.setText(String.valueOf(val));
            txt.setEditable(false);
            txt.getStyleClass().add("sudoku-field-fixed");
        } else {
            txt.setTextFormatter(new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                if (newText.matches("[1-9]?")) return change;
                return null;
            }));

            // logika gry (sprawdzanie poprawności)
            txt.textProperty().addListener((obs, oldVal, newVal) -> {
                highlightBoard(row, col);
                if (!newVal.isEmpty()) {
                    int entered = Integer.parseInt(newVal);
                    int correct = modelBoard.getField(row, col).getCorrectValue();

                    if (entered != correct) {
                        mistakes++;
                        updateMistakesLabel();
                        
                        //błędu + pogrubienie
                        txt.setStyle("-fx-text-fill: #ff4444; -fx-font-weight: bold;");
                        
                        if (mistakes >= MAX_MISTAKES) {
                            stopTimer();
                            showAlert("Porażka", "Przekroczyłeś limit błędów! Czas gry: " + formatTime(secondsPlayed));
                            txt.getScene().getWindow().hide();
                        }
                    } else {
                        // poprawny + pogrubienie
                        txt.setStyle("-fx-text-fill: #00ff00; -fx-font-weight: bold;");
                        modelBoard.getField(row, col).setValue(entered);
                        checkWin();
                    }
                } else {
                    txt.setStyle("");
                }
            });
        }
        return txt;
    }
    
    /**
     * Aktualizuje tekst licznika błędów i zmienia jego styl na ostrzegawczy,
     * gdy gracz zbliża się do limitu przegranej.
     */
    private void updateMistakesLabel() {
        infoLabel.setText("Błędy: " + mistakes + " / " + MAX_MISTAKES);
        if (mistakes >= 2) {
            infoLabel.getStyleClass().removeAll("mistakes-label");
            infoLabel.getStyleClass().add("mistakes-danger"); 
        }
    }

    /**
     * Przygotowuje nową rozgrywkę poprzez skopiowanie poprawnej planszy do modelu
     * i odkrycie określonej liczby pól losowo.
     *
     * @param source W pełni rozwiązana plansza (źródłowa).
     * @param target Plansza modelu gry (częściowo zakryta).
     * @param filledCount Liczba pól, które mają zostać odkryte na starcie.
     */
    private void prepareGame(SudokuBoard source, SudokuBoard target, int filledCount) {
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                target.getField(i, j).setCorrectValue(source.getField(i, j).getValue());
                target.getField(i, j).setValue(0);
            }
        }
        int exposed = 0;
        while(exposed < filledCount) {
            int r = (int)(Math.random()*9);
            int c = (int)(Math.random()*9);
            if(target.getField(r, c).getValue() == 0) {
                target.getField(r, c).setValue(target.getField(r, c).getCorrectValue());
                target.getField(r, c).setEditable(false);
                exposed++;
            }
        }
    }

    private void checkWin() {
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                if (modelBoard.getField(i, j).getValue() == 0) return;
            }
        }
        stopTimer();
        showAlert("Wygrana!", "Twój czas: " + formatTime(secondsPlayed));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int showDifficultyDialog() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Łatwy", "Łatwy", "Średni", "Trudny");
        dialog.setTitle("Wybór poziomu trudności");
        dialog.setHeaderText("Wybierz poziom trudności gry:");
        dialog.setContentText("Poziom:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            switch (result.get()) {
                case "Łatwy": return 30;
                case "Średni": return 25;
                case "Trudny": return 18;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        launch();
    }

}

