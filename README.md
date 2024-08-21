# BattleShip
Java Swing implementation of the BattleShip game with a twist: the user create its own ships! 1vs1 and Computer Modes

This is a project of when I was 15 years old, so the design and code quality are not very high, though the "AI" algorithm that the computer uses when "VS Computer" mode is selected has some value in my opinion. It is basically a non-trivial brute force algorithm that tries every remaining ship in every feasible spot with every feasible ship rotation and associates every spot with a value. The higher the value the more probable it is for a player ship to be in that spot.

Another valuable feature is the ship editor.
