# ProgramowanieObiektoweProjekt
repo pod kody do projektu na zaliczenie programowania obiektowego
<br>
<br>

# Poniżej spis funkcjonalności jakie spełnia aplikacja

 ### Przed rozpoczęciem:
- generuje w pełni standardową planszę sudoku 9x9
- rozdziela planszę na czytelne bloki 3x3
- aplikacja rozwiązuję uprzednio wygenerowaną planszę
- usuwa odpowiednią ilość wartości z pól (SudokuSingleField) w zależności od wybranej trudności przez gracza i zapisuje jaka wartość uprzednio się tam znajdowała (klasa correctValue)

### Rozpoczęcie gry:
- gracz wybiera poziom trudności spośród trzech możliwych (łatwy, średni, trudny)
- aplikacja na podstawie trudności ustala ilość wypełnionych pól
- inicjalizacja planszy
- blokada początkowo wygenerowanych pól (gracz nie może ich zmienić, ale może edytować wypełniane przez samego siebie)

### Plansza: 
- aplikacja wyświetla planszę w postaci siatki 9×9 pól tekstowych
- stałe pola gry są oznaczone innym stylem
- wyróżniane są granice bloków 3×3

### Podświetlanie i nawigacja:
- po wybraniu pola podświetlany jest cały wiersz i kolumna na lekko szarawy kolor w celu lepszej przejrzystości
- podświetlenie tych samych pól zawierających tę samą cyfrę po wpisaniu danej cyfry lub kliknięciu na pole z daną cyfrą (lepsza przejrzystość)
- gracz może poruszać się po planszy strzałkami klawiatury

### Wpisywanie liczb
- gracz może wpisywać cyfry z zakresu 1–9 do pustych pól (odpowiednia walidacja zabrania mu na wpisane innych)
- gracz nie może wpisać znaków nie będących cyframi
- poprawna wartość jest oznaczana kolorem zielonym
- błędna wartość jest oznaczana kolorem czerwonym

### Zasady gry
- aplikacja zlicza błędne wpisy gracza (bazowy limit błędów wynosi 3 - limit jest możliwy do zmiany przez gracza)
- wyświetlany jest aktualny licznik błędów
- po przekroczeniu limitu system kończy grę komunikatem „porażka”

### Warunki wygranej
- aplikacja sprawdza, czy wszystkie pola zostały poprawnie uzupełnione
- po uzupełnieniu planszy poprawnie wyświetlany jest komunikat „wygrana”
- system zatrzymuje licznik czasu po zakończeniu gry

### Timer
- aplikacja udostępnia możliwość mierzenia czasu rozwiązywania sudoku za pomocą timera widocznego w prawej górnej częsci okienka.
