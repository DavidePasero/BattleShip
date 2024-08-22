# BattleShip

## Description
Java Swing implementation of the BattleShip game with a twist: the user create its own ships! 1vs1 and Computer Modes

This is a project of when I was 15 years old, so the design and code quality are not very high, though the "AI" algorithm that the computer uses when "VS Computer" mode is selected has some value in my opinion. It is basically a non-trivial brute force algorithm that tries every remaining ship in every feasible spot with every feasible ship rotation and associates every spot with a value. The higher the value the more probable it is for a player ship to be in that spot.

Another valuable feature is the ship package editor.

## To run
1 - Open your favourite Java IDE and import the project
2 - Run the class BattagliaNavale.java (the main class)

## To play
1 - Click on the play button
2 - Choose the mode (vs Computer or 1vs1)
3 - Select settings (difficulty, who starts, and ships package)
4 - Click on the spot you want to fire your shot

## To create ships packages
1 - Click on the play button
2 - Choose "Editor navi"
3 - Draw a shape in the table that follows the rules (no separated ships and not too big)
4 - Click on "Conferma button"
5 - Repeat 3-4 for 6 times, you will create 6 ships.
6 - Save, click on "Salva".

## Gameplay
Youtube link: https://youtu.be/U6MTyOHeDXc
