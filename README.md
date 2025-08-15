# 🧱 Java Brick Breaker Game

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Game](https://img.shields.io/badge/Game-Brick%20Breaker-blue?style=for-the-badge)

A multithreaded **Brick Breaker** game built in Java.  
The player controls a paddle to bounce a ball that destroys blocks, with collision physics, bonuses, penalties, and smooth UI rendering.  
The ball bounces off the UI borders and interacts with blocks using precise collision detection.  

---

## 🎮 Features
- **Smooth Ball Physics** – accurate collision detection and response.
- **Multithreading** – separate threads for game logic, rendering, and background music.
- **Bonuses & Penalties** – power-ups and challenges to keep the game interesting.
- **Customizable Game Mechanics** – easy to tweak ball speed, block layout, etc.
- **JavaFX UI** – clean and interactive interface using `sample.fxml`.

---

## 📂 Project Structure

| File/Folder               | Description |
|---------------------------|-------------|
| `BackgroundMusic.java`    | Handles in-game music |
| `BallMovement.java`       | Ball movement & collision |
| `Block.java`              | Block object definition |
| `BlockSerializable.java`  | Block saving/loading |
| `Bonus.java`               | Bonus logic |
| `GameController.java`     | Game state management |
| `GameEngine.java`         | Main game loop |
| `GameIOController.java`   | Save/load game data |
| `GameInitializer.java`    | Game setup |
| `LoadSave.java`           | File handling |
| `Main.java`               | Application entry point |
| `MainMenuController.java` | Menu UI controller |
| `Penalty.java`            | Penalty logic |
| `Score.java`              | Score tracking |
| `UIController.java`       | UI rendering |
| `sample.fxml`             | JavaFX UI layout |


