
An advanced 5-Card Draw Poker simulator implemented with a rigorous focus on algorithmic efficiency and architectural separation of concerns.

#### 1. Immutable Card Primitives
`Card`, `Rank`, and `Suit` utilize a combination of enums and record-like class structures. 
- **Enums**: `Rank` and `Suit` provide type safety and constant-time lookups. `Rank` stores intrinsic values (2-14) used for algorithmic comparison.
- **Immutability**: The `Card` class is immutable by design, ensuring thread safety and preventing accidental state mutation during complex hand evaluations.

#### 2. Collection Framework Utilization
- **ArrayList**: Used in the `Deck` and `Hand` for efficient ordered retrieval and manipulation. The `Deck` utilizes `Collections.shuffle()` which implements a Fisher-Yates shuffle algorithm (O(n)).
- **HashMap**: Heavily utilized within `Hand.evaluate()` to perform frequency analysis of ranks and suits. This allows for O(1) lookups during pattern matching (e.g., detecting Four-of-a-Kind or Full House).

### Algorithmic Implementation

#### Hand Evaluation Heuristics
The core evaluation engine in `Hand.java` uses a tiered frequency analysis:
1. **Histogram Generation**: A frequency map of Ranks and Suits is produced.
2. **Boolean Flags**: Checks for `flush` (single-suit count) and `straight` (consecutive rank values).
3. **Pattern Matching**: Categorizes the hand into `HandCategory` based on histogram peaks (e.g., `containsValue(4)` -> Four of a Kind).

#### Tie-Breaker Logic (Comparable Interface)
A custom implementation of the `Comparable` interface handles tie-breaking. When two hands share the same category, the engine performs a high-to-low rank comparison across all 5 cards, ensuring deterministic outcomes even in complex "Kickers" scenarios.

#### Auto-Hold Strategy Engine
In `PokerGUI.java`, an `applyAutoHold()` method implements a basic optimal strategy. It parses the current hand using rank frequency and automatically markers cards for "Retention" if they contribute to a Pair or better, minimizing human error in fast-paced play.


```terminal_session
# Compilation (Output to bin directory)
javac -d bin src/com/poker/core/*.java src/com/poker/ui/*.java

# Execution
java -cp bin com.poker.ui.PokerGUI
```
