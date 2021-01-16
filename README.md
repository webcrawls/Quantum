# Quantum

Quantum is a random location generator plugin for Minecraft servers running 1.16+. It provides an exhaustive API to allow plugins and users to define their own custom location generation algorithms.

As Quantum is intended to be used as an API, there are no user-facing features implemented. Instead of bundlng the logic for the various first-party modules, Quantum is currently split up into three modules: QuantumAPI, QuantumWild, and QuantumSpawn.

## QuantumAPI
- An API for plugins
- Provides configuration for users to modify rulesets

## QuantumSpawn
- Requires QuamtumAPI
- Uses rulesets to enable random spawning mechanics

QuantumSpawn is still in development, but you can read more [here](#).

## QuantumWild
- Requires QuantumAPI
- Provides /wild commands with configurable cooldown & per-world options

QuantumWild is still in development, but you can read more [here](#).

## Features

_NOTE: Read the [wiki](../../wiki) for an introduction to Quantum's features and the API._

Here's a break down of Quantum's main selling points:

- [x] **Supports 1.16+**

## Notes on Compiling

To compile, you must have access to the [FactionsUUID jar](https://www.spigotmc.org/resources/factionsuuid.1035/). Please download it, and put it in `{project_root}/libs/Factions.jar`.
    
## Credits

Thanks to [Kyori](https://github.com/KyoriPowered) for the adventure text library.

Thanks to [Incendo](https://github.com/Incendo) for the cloud commands library.

Thanks to [Paper](https://papermc.io) for existing.
