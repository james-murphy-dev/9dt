# 9dt

Drop Token takes place on a 4x4 grid. A token is dropped along a column (labeled 0-3) and said
token goes to the lowest unoccupied row of the board. A player wins when they have 4 tokens
next to each other either along a row, in a column, or on a diagonal. If the board is filled, and
nobody has won then the game is a draw. Each player takes a turn, starting with player 1, until
the game reaches either win or draw. If a player tries to put a token in a column that is already
full, that results in an error state, and the player must play again until they play a valid move.
