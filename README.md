# Quantum

Quantum is a random location generator for 1.16+. It provides an exhaustive API to allow plugins and users to define their own custom location generation algorithms.

As Quantum is intended to be used as an API, there are no user-facing features implemented. Instead of bundlng the logic for the various first-party modules, Quantum is currently split up into three modules: QuantumAPI, QuantumWild, and QuantumSpawn.

## QuantumAPI
- An API for plugins
- Provides configuration for users to modify rulesets

## QuantumSpawn
- Requires QuamtumAPI
- Uses rulesets to enable random spawning mechanics

QuantumSpawn is still in development, but you can read more [here](#).

## QuantumWild
- Requires QuamtumAPI
- Provides /wild commands with configurable cooldown & per-world options

QuantumWild is still in development, but you can read more [here](#).

## Features

_NOTE: Read the wiki for an introduction to Quantum, it's features, and the API._

Quantum has two main features: RTP, and /wild. Here's a break down of Quantum's main selling points.

- [x] **Supports 1.16+**
    
## Credits

Thanks to Kyori for the adventure text library.

Thanks to Incendo for the cloud commands library.

Thanks to Paper for existing.
