# Stockpig - Chess Bot

A simple Java implementation of chess using bitboards.

## Running the CLI

Linux: 
```
./gradlew  run
```

Windows:
```
gradlew.bat run
```

#### Commands
`new`: Start a new game with standard set up

`fen [fen string]`: Setup up a game from a given FEN string

`move [move]`: Make the given move

`undo`: Undo the last move

`perft [depth]`: Perform a move enumeration test

`divide [depth]`: Perform a move enumeration test, showing move breakdown at depth 1

`exit`: Exit the app