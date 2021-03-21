# Project 5: Mine Walker

* Author: Mason Tolley
* Class: CS121/Computer Science 1B
* Semester: Fall 2020

## Overview

This is a game where the player has to get from the bottom left corner to the top right corner of a grid. On the grid, there are several mines that the player has to avoid. The player can get cues about where the mines are by the colors of the squares. When they step on a square, it will change color based on the number of mines around it. The player gets five lives and they lose one when they step on a mine.

## Compiling and Using

Navigate to the directory that contains the Java files on the command line. To compile and run it, use the following commands:

```
$ javac MineWalker.java
$ java MineWalker
```

Then enjoy the game.

## Discussion

This project was more time consuming than hard. Most of the logic was straight forward, but it took time and lots of thinking to implement. The hardest problem I ran into was trying to get the colors to stay correct after temporary changes were made. For example, when the player steps on the path, and then reveals the path, it would mess up the colors after the player hid the path again. I fixed this by creating my own method to change color on the buttons and save the previous color to a variable. Then I created another method to return to the previous color. I feel like this project was a good culmination of everything we have worked on in this class, including writing my own class, using arrays, and implementing GUIs.

## Testing

To test this program, I ran it after any change I made. Whether I was implementing a new part of the game or I was just trying to fix a bug that came up, I would test the program to make sure it worked. I also wrote the program in pieces that allowed me to implement new parts while still allowing the game to function. After finishing, I tested the program and made sure that boundary values were working properly.

## Extra Credit

For extra credit, I added slider to change the difficulty. I also added sound effects for stepping, landing on a mine, reaching the goal, and pressing invalid buttons.

## Sources Used

[Playing sound effects](https://stackoverflow.com/questions/20354508/sound-effects-in-java)

[Prevent window resize](https://stackoverflow.com/questions/18031704/jframe-how-to-disable-window-resizing/18031725)