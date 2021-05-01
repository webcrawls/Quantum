# Quantum


[![Build Status](https://ci.kscott.dev/api/badges/kadenscott/Quantum/status.svg?ref=refs/heads/v2)](https://ci.kscott.dev/kadenscott/Quantum)

> _An insanely customizable location generation suite, including /rtp, /wild, random spawns, and more!_ 

Quantum is a location generation suite for Minecraft 1.16+. It provides an interface (through plugin extensions) to interact with internal location APIs. Quantum can be configured to generate locations under highly customizable criteria, such as:

- **always** in a forest biome...
- **never** in a water lake...
- **always** near a pig...
- **never** near a zombie...
- **within** 1000 blocks of 0,0...
- AND **never** in a cave!

These are only a few of the many possibilities Quantum's advanced configuration offers. Quantum is very adaptable, and can (and is!) used by many types of servers: Factions, minigames, PvP, and more. Quantum's documentation is constantly growing, so make sure to check out the wiki page and the [Discord server](https://chat.ksc.sh)!

## Modules

### QuantumAPI

- An API for plugins.
- Provides configuration for users to modify rulesets.

### QuantumSpawn

- Requires QuantumAPI.
- Uses rulesets to enable random spawning mechanics.

### QuantumWild

- Requires QuantumAPI.
- Provides /wild commands with configurable cooldown & per-world options.

> Keep in mind, these modules often receive updates bringing more features. See the wiki page and my Discord server to get the latest information. 

## Features

_NOTE: Read the [wiki](../../wiki) for an introduction to Quantum's features and the API._

Here's a break down of Quantum's main selling points:

- [x] **Supports 1.16+**

## Notes on Compiling

To compile, you must have access to the [FactionsUUID jar](https://www.spigotmc.org/resources/factionsuuid.1035/).
Please download it, and put it in `{project_root}/libs/Factions.jar`.

## Credits

Thanks to [Kyori](https://github.com/KyoriPowered) for the adventure text library.

Thanks to [Incendo](https://github.com/Incendo) for the cloud commands library.

Thanks to [Paper](https://papermc.io) for existing.
