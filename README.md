# Snakes: Boons and Curses
A Java-Based OOP UI Game, With a twist of the Snakes &amp; Ladders game

## 📖 Overview
A thrilling twist on the classic tabletop game. Snake and Ladders. Traverse through a wretched underworld of limbo, where luck isn't the only thing that can influence one’s pursuit. Watch your step, mysterious game-changing boons and curses await at every crevice, making the roll of the die more intense than ever. Explorers race to reach the final stretch of the tile, careful must they be, as vines and snakes may look alike in this uncharted expansion. One misstep may herald one’s doom, or if you're lucky enough, may lead you to your boon!


## 🚀 Features
 - **GUI Implementation through Swing**
 - **Program Structure Constructed using Maven Apache** 
 - **Game Class** - core logic, controls the player turns, dice rolls, movement across the board, player positions, player win condition
 Player class - player character, current position, character-unique passive/active skills.
- **Dice class** - random dice rolls that determine how far a player moves from their position.
- **Board class** - contains all the tiles, with varying tiles such as with a vine, snake, a cursed tile, and a boon tile.
- **Cursed tile class** - gives the player a randomized disadvantage if a player lands on it.
- **Boon tile class** - gives the player a randomized advantage if a player lands on it.

## Game Mechanics
- Hidden Tricks deceptive vines or treacherous snakes may appear on tiles.


- **Cursed Tiles**:


__(What are the odds?)__ - When stopping by a cursed tile, the next 2 rolls will have a diminishing effect: you have to roll an even number of pips to move forward, if you roll an odd number of pips, you get to roll that many backwards. 

__(Barred heaven)__ - For the next 3 turns, stopping on ladders won’t move you up.

__(Unmovable man)__ - Your next turn will be given up and will instead be given to the player in last place. Given that the player is the one who rolled, nothing will happen.

__(Pillar of Salt)__ - If the player steps on this tile, the player will be cursed. The curse will make you lose your next turn if you are eaten by the snake.

__(Blackout)__ - The snakes on the board will be moved randomly and hidden. A snake will be visible if a player lands on it, or the curse ends after 4-5 turns.

__(Job’s Suffering)__ - When stepped on, all curses will be applied to the player (no duplications).

- **Boons Tiles**:

__(Foretold Fate)__ - On your next turn, choose from 1-10, and that will be the number of steps you’ll take. 

__(Daniel’s Blessing)__ - For the next two turns, shut all snakes’ mouths for the player.

__(Switcheroo)__ - Swap places with a random player higher than you.

__(Jacob’s ladder)__ - Nullifies the next 2 cursed effects you step on.

__(Shackled)__ - Chains the player, subtract 2 pips from their roll.(minimum: 0)

__(Semented)__ - Let a player choose a player ahead of them, then bond them together. When a player, the Tail, activates the link with a player ahead, the Head, their movements become linked: if the Head moves forward, the Tail is pulled along, and if the Tail is forced backward by a penalty, the Head is dragged back with them. However, the Head player's movements, if backward, do not affect the Tail, making it a high-risk, high-reward move for the Tail player to either hitch a ride or sabotage their rival. Will last 3 turns.


## Prerequisites
Before setting up the project, ensure you have the following installed:

- [Maven](https://maven.apache.org/download.cgi)
- [Maven Daemon](https://dlcdn.apache.org/maven/mvnd/1.0.3/maven-mvnd-1.0.3-windows-amd64.zip)(Optional)

## Installation

Clone the repository:

```bash
git clone https://github.com/dejely/Snakes-Blessings-and-Curses.git
```

**With Maven**:

```bash
mvn compile
mvn verify
```

**With Maven Daemon**:

```bash
mvnd compile
mvnd verify
```

## 🧮 To Run

On Directory Root

```ruby
java -jar target/Snakes-Blessing-and-Curse-1.0-SNAPSHOT.jar
```


## 📋 Test Cases
Navigate to src/test/java/game

**For Maven**:


```bash
mvn test
```

**For Maven Daemon**:

```bash
mvnd test
```



## Contributing


