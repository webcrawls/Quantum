# Quantum

Quantum is a modern RTP & /wild plugin for 1.16+. It sports a broad feature-set, an in-depth configuration, and expandability via a developer API.

## Features

Quantum has two main features: RTP, and /wild. Here's a break down of Quantum's main selling points.

- [x] **Supports 1.16+**
- [ ] **Random teleportation**
    - [ ] **Random spawn point on death and first join.** Both are configurable - you can disable random spawn points on first join, enable random spawn points on death, and vice-versa.
- [ ] **/wild**
    - [ ] **Configurable cooldowns.** Cooldowns are controlled via permission nodes - i.e. `quantum.wild.cooldown.10` would result in a /wild cooldown of 10 seconds for users with said permission.
- [ ] **Custom spawn location rules**
    - [ ] **Quantum's spawn location algorithm is completely customizable.** You can use custom rules to decide whether a location will be marked as valid.
        - [ ] Supports WorldGuard regions out-of-the-box.
        - [ ] Supports FactionsUUID claims out-of-the-box.
- [ ] **Efficiency**
    - [ ] All chunk access is called using async methods. Quantum will never load chunks on the main thread.
    - [ ] Quantum has an optional spawn caching system, where valid spawnpoints will be saved for later use (instead of searching on the fly). Locations are still validated, so players won't be thrown in lava pools even if this feature is enabled.
- [ ] **Developer API**
    - [ ] **Quantum's developer API is very powerful.** Using it, you can query for random spawn points, define your own custom location validators, and more. For example, you can disable all user-facing features and use Quantum as the backbone for your minigame's spawnpoints.
    
## Credits

Thanks to Kyori for the adventure text library.

Thanks to Incendo for the cloud commands library.

Thanks to Paper for existing.