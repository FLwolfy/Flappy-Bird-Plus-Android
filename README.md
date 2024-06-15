# FlappyBird Plus

Welcome to FlappyBird Plus! This advanced version of the original mobile game “FlappyBird” is recreated through Android Studio with multiple added game mechanics to make the game more interesting and engaging. Here is the preview GIF:
<div align="center">
  <img src="Preview.gif" alt="Preview">
</div>

## Table of Contents

1. [Introduction](#introduction)
2. [APP Overview](#app-overview)
3. [System Requirements](#system-requirements)
4. [Installation](#installation)
5. [Game Title Page / Settings](#game-title-page--settings)
6. [Gameplay](#gameplay)
7. [Game Events](#game-events)
8. [Game Over View](#game-over-view)
9. [Game Logic Behind](#game-logic-behind)

## Introduction

This user manual provides a comprehensive guide on how to play FlappyBird Plus, including the major elements of the game and how the game mechanics work. It also offers insights into the multithread programming used to keep the game smooth, helping you understand and enjoy the game better.

## APP Overview

FlappyBird Plus is an advanced version of the original mobile game “FlappyBird.” This version includes several new features:
- Customizable game appearance and difficulties.
- Power-up coins to gain more scores and heal lives.
- A game event system introducing randomness and additional challenges or rewards.

## System Requirements

- **Application Version**: v1.0.0
- **Platform**: Android 8.0 and above
- **Required Permissions**: Front running service; system time access

## Installation

Android users can copy the installation files directly to their phone for installation.

## Game Title Page / Settings

### Title Page Overview

Upon opening the application, you will see the main title page with two interactable buttons: `START` and `OPTION`.

### Setting Window

Clicking on the `OPTION` button opens the setting window where you can customize the game settings:
- **Avatar**: Choose the bird's appearance (currently only color changes).
- **Scene Speed**: Controls the speed of the scene objects.
- **Event Frequency**: Adjusts the frequency of game events.

#### Debug Frame Rate

Long-click the `OPTION` button to activate the debug frame rate shower, displaying the frame rate in the top right corner during gameplay.

## Gameplay

### Flappy Bird

After clicking the `START` button, control the bird by tapping the screen to make it flap up. Avoid obstacles and collect coins to score points and heal lives.

### Damaging Obstacles

- **Pipes**: Avoid touching different types of pipes (green, red, golden) to survive.
- **Base Ground**: Touching the ground causes damage and bounces the bird back.

### Power-Up Coins

Collect coins to gain additional scores and heal lives. Each coin counts as 5 points at the end of the game, and every two coins heal one life if not at full health.

## Game Events

Three types of game events occur with a 1/3 chance each, adding challenge or reward elements to the game:

1. **Crazy Pipes**: All pipes become red and move.
2. **More Gravity**: Doubles the gravity, making the bird fall faster.
3. **Gold Rush**: All pipes become golden, providing coins and healing the player.

## Game Over View

When the player’s life reaches zero, the game over view displays the final score. If a new high score is achieved, it updates the record system and shows the "new" tag next to the recorded score.

## Game Logic Behind

### Multithread Game Base

The game uses a multi-threaded architecture to separate rendering and game logic, ensuring smooth gameplay even if frame rate issues occur.

- **Render Thread**: Handles rendering aspects like drawing and animation, ensuring stability and smooth movements.
- **Logic Thread**: Handles calculations and physical logic, ensuring no jitters in the physics detections.

### Data Structure Abstractions

The game uses abstractions for game objects, scenes, music, and events to facilitate code reuse and focus on gameplay design.
