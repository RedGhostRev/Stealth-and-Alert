# Stealth and Alert

[简体中文](README.zh-CN.md)

## Introduction

**Stealth and Alert** is a Minecraft mod bringing a stealth and alert system just like what in other games.

The goal of this project is to create a tactical stealth experience where players must utilize shadows, line of sight,
and environmental factors to bypass enemies.

## Implemented Mechanics

- **Vision-based Detection**: A realistic vision cone system that accounts for horizontal FOV and vertical angles,
  ensuring
  mobs only see what's physically within their sight.
- **Circular HUD Indicator**: A dynamic, Assassin's Creed-style UI that tracks enemy positions and their alert levels
  toward the player in real-time.
- **Multi-stage Detection AI**: Mobs transition through Idle, Suspicious, Searching, and Fighting states based on their
  perception.
- **LKP (Last Known Position)**: Mobs will investigate the spot where they last saw the player, rather than having "
  god-mode" vision.
- **Multi-player Sync**: Fully synchronized stealth data in multiplayer environments, including primary target
  competition.
- **In-World Alert Indicators**: Visual cues displayed directly above enemies' heads within the game world to indicate
  their current alertness. With HUD.
- **Unified Visibility System**: A complex perception model influenced by environmental factors (such as light levels) 
and player stances (crouching/running).
- **Environmental Stealth**: Hiding in specific blocks like tall grass.
- **Acoustic Detection**: Mobs will react to sounds, such as footsteps, block breaking, or arrow impacts near them. 
With HUD.

## Planned Features

- **Assassination System**: Special takedowns and lethal strikes for unaware enemies.
- **Corpse Mechanics**: Enemies will become alerted or enter a search state upon discovering the bodies of their fallen
  allies.
- And more!

There may be bugs and performance issues, and feel free to report them!

## Authors

- RedGhostRev

## Requirements

- Minecraft: 1.21.1
- NeoForge: 21.1.213
- Java: 21

## Contributing

- Issues and PRs are welcome.

## License

- MIT