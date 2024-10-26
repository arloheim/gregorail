# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Support for block data and NBT tags in the `/grail block` and `/grail blockif` commands.

## [2.0.0] - 2024-03-12

### Added

- Added conditional commands for most `/gcart` and `/grail`commands.
- Include multiple conditional branches in `/gcart` and `/grail` commands with the `||` separator.
- Conditionally play sounds with the `/grail sound` and `/grail soundif` commands.
- Set the search distance for blocks and minecarts in commands as properties.
- Added parser and scanner classes to handle different types of command arguments.

### Changed

- Create code tags with the `/gtag` command that include display names and URLs, instead of the `gregorail code` command. Display names included from v0.3.6 onwards will be migrated to code tags automatically. More properties in code tags can be added on request.
- The butcher only kills mobs now, as opposed to all "living" entities including armor stands.
- Webhooks execute on a separate thread now, so moving minecarts don't lag or get interrupted.
- Refactored model and plugin structure, created interfaces and data classes for common models like minecarts, codes and queries.

## [1.1.0] - 2023-11-05

### Added

- Execution of webhooks can be assigned to the `/gcart` and `/grail` commands to funnel the data processed by GregoRail to a web server.

### Fixed

- #1 Minecarts with code drop themselves when killed in creative

## [1.0.0] - 2023-02-27

### Added

- Adjust the speed multiplier of minecarts with the `/gcart speed` and `/gcart speedif` commands.
- The `/grail switch` and `/grail switchif` commands now support all types of rails instead of only normal rails.

### Changed

- Improved error messages.

## [0.3.6] - 2023-02-12

### Fixed

- Checking query codes checks against the Code class itself instead of its id field.

## [0.3.5] - 2023-02-12

### Added

- Interactively assign codes to minecarts with the `/gcart promptset` command.
- Tab completion for all commands.
- All command handlers now implement the Listener interface.

### Changed

- Moved the `/gcart unset` subcommand to the `/gcart clear` subcommand.
- Refactored packages and utility classes.

## [0.3.4] - 2023-02-11

### Added

- Set blocks based on minecart codes with the `/grail block` and `/grail blockif` commands.

## [0.3.3] - 2023-02-11

### Changed

- Executing queries on minecart codes will now happen at the location of the command sender.

### Fixed

- Fixed checking the nearest cart without code.

## [0.3.2] - 2023-02-11

### Added

- Added configuration options for dropping items from butchered living entities.

## [0.3.1] - 2023-02-11

### Fixed

- Don't kill living entities that already have been killed by the butcher.

## [0.3.0] - 2023-02-11

### Added

- Added a configurable butcher that kills living entities in a specified radius if the player is riding a minecarts.

## [0.2.2] - 2023-02-11

### Added

- Added configuration options for block and entity search radius.

### Changed

- Queries now use pipes `|` instead of commas `,`.
- Moved `/gadmin locate` and `/gadmin locatecart` subcommands to the `/glocate` command.
- Moved other `/gadmin` subcommands to the `/gregorail` command.
- Refactored `/grail` subcommands.

## [0.2.1] - 2023-02-10

### Added

- Use compound queries separated by commas `,` with the `/grail switchif` command.

### Changed

- Refactored utility classes.

## [0.2.0] - 2023-02-10

### Added

- Remove codes from minecarts with the `/grail unset` command.

### Changed

- Moved subcommands to `/gcart`, `/grail` and `/gadmin` commands.
- Refactored command and minecart utility classes.

### Fixed

- Added null checks for several commands.

## [0.1.0] - 2023-02-09

### Added

- Assign codes to minecarts with the `/rail assign` command.
- Switch rails with the `/rail switch` and `/rail switchif` commands.
- Locate blocks and carts using the `/rail locate` and `/rail locatcart` commands.
