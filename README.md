# ProgramowanieObiektoweProjekt

Repozytorium zawiera kody źródłowe aplikacji Sudoku stworzonej w ramach projektu zaliczeniowego z przedmiotu **Programowanie Obiektowe**.

Aplikacja umożliwia grę w Sudoku 9×9 z wyborem poziomu trudności, walidacją ruchów gracza oraz pomiarem czasu rozgrywki.

---

## Wymagania funkcjonalne 

### 1. Generowanie i przygotowanie planszy
- System generuje poprawną, standardową planszę Sudoku o wymiarach **9×9**.
- Plansza jest logicznie podzielona na **bloki 3×3**.
- System rozwiązuje wygenerowaną planszę w celu uzyskania pełnego, poprawnego rozwiązania.
- Na podstawie wybranego poziomu trudności system usuwa odpowiednią liczbę wartości z pól planszy.
- Usunięte wartości są zapamiętywane jako wartości poprawne (np. w klasie `correctValue`).
- Każde pole planszy jest reprezentowane jako osobny obiekt (np. `SudokuSingleField`).

---

### 2. Rozpoczęcie gry
- Gracz wybiera jeden z trzech poziomów trudności:
  - łatwy
  - średni
  - trudny
- Na podstawie poziomu trudności system:
  - ustala liczbę początkowo wypełnionych pól,
  - inicjalizuje planszę gry.
- Pola wygenerowane na starcie są zablokowane:
  - gracz nie może ich edytować,
  - gracz może edytować wyłącznie pola puste.

---

### 3. Wyświetlanie planszy
- Plansza jest wyświetlana jako **siatka 9×9 pól tekstowych**.
- Pola stałe (początkowe) są oznaczone innym stylem wizualnym.
- Granice bloków **3×3** są wyraźnie wyróżnione.

---

### 4. Podświetlanie i nawigacja
- Po zaznaczeniu pola:
  - podświetlany jest cały **wiersz** i **kolumna**.
- System podświetla wszystkie pola zawierające tę samą cyfrę:
  - po wpisaniu cyfry,
  - po kliknięciu pola z istniejącą cyfrą.
- Gracz może poruszać się po planszy za pomocą **strzałek klawiatury**.

---

### 5. Wprowadzanie danych
- Gracz może wpisywać wyłącznie cyfry z zakresu **1–9**.
- System blokuje możliwość wpisywania znaków niebędących cyframi.
- Wpisana wartość jest:
  - oznaczana **kolorem zielonym**, jeśli jest poprawna,
  - oznaczana **kolorem czerwonym**, jeśli jest błędna.
- System wykonuje walidację każdej wprowadzonej wartości.

---

### 6. Zasady gry i obsługa błędów
- System zlicza liczbę błędnych wpisów gracza.
- Domyślny limit błędów wynosi **3**.
- Gracz może zmienić limit dopuszczalnych błędów.
- Aktualny licznik błędów jest wyświetlany na ekranie.
- Po przekroczeniu limitu błędów:
  - gra zostaje zakończona,
  - wyświetlany jest komunikat **„porażka”**.

---

### 7. Warunki zwycięstwa
- System sprawdza, czy:
  - wszystkie pola planszy są wypełnione,
  - wszystkie wartości są poprawne.
- Po spełnieniu warunków:
  - wyświetlany jest komunikat **„wygrana”**,
  - licznik czasu zostaje zatrzymany.

---

### 8. Pomiar czasu
- System mierzy czas rozwiązywania Sudoku za pomocą timera.
- Timer jest widoczny w **prawej górnej części okna aplikacji**.
- Czas jest zatrzymywany po zakończeniu gry (wygrana lub porażka).

---

## Wymagania niefunkcjonalne 

### 1. Użyteczność (Usability)
- Interfejs użytkownika jest czytelny i intuicyjny.
- Kolory używane do oznaczania poprawnych i błędnych pól są jednoznaczne.
- Podświetlanie wierszy, kolumn i takich samych cyfr poprawia przejrzystość gry.
- Sterowanie klawiaturą umożliwia szybkie poruszanie się po planszy.

---

### 2. Spójność i estetyka
- Styl wizualny planszy jest jednolity w całej aplikacji.
- Pola stałe i edytowalne są wyraźnie rozróżnione.
- Granice bloków 3×3 są jednoznacznie widoczne.

---

### 3. Poprawność działania
- Generowane plansze Sudoku są zawsze logicznie poprawne.
- System nie dopuszcza do wprowadzenia nieprawidłowych danych.
- Walidacja działa w czasie rzeczywistym po każdej akcji gracza.

---

### 4. Rozszerzalność
- Struktura aplikacji umożliwia:
  - łatwą zmianę limitu błędów,
  - dodanie nowych poziomów trudności,
  - rozbudowę logiki gry.
- Kod jest zgodny z zasadami programowania obiektowego.

---

### 5. Niezawodność
- Aplikacja działa stabilnie przez cały czas trwania rozgrywki.
- Zakończenie gry (wygrana/porażka) następuje tylko w jasno określonych warunkach.
