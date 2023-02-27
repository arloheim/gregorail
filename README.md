**GregoRail** is a Spigot plugin containing powerful railroad management tools for Minecraft. Requires Spigot **1.19**, tested on **1.19.2**.

# Features

GregoRail provides useful [commands](https://github.com/danae/gregorail/wiki/Commands) that are meant to be used in a creative world to create a rail-based public transport system. All of the command can be used in command blocks to create your own rail route network that acts accordingly depending on which lines run on the rail. At its core, GregoRail uses a range of commands to manage codes for minecarts and change the world around it based on those codes.

The features of the plugin include:

* Assign codes to minecarts to identify them in your rail network. You can also define user-friendly aliases/display names for the codes using chat formatting codes, which will be used as names for the minecarts.
* Change the shape of rails, either unconditionally or dependent of the code of a minecart. Wildcard queries exist to act on multiple codes.
* Change the material of blocks, again either unconditionally or dependent of the code of a minecart, which is useful for powering rails on demand or custom rail signals.
* Change the speed of minecarts using commands, also optionally dependent on the code of a minecart.
* Automatically kill living entities in a specified range around a player when they are riding a minecart, of which the behaviour can be customized.

# Installation

* Download the [latest release](https://github.com/danae/gregorail/releases) of GregoRail from the GitHub repository and place it in the `plugins/` directory of your Spigot server. No additional plugns are required to run GregoRail, but this is subject to change if future features require advanced code to be implemented.
* Run the server and [tweak the configuration](https://github.com/danae/gregorail/wiki/Configuration) in `plugins/GregoRail/config.yml` if necessary. To apply the changes in the configuration, either use the `/gregorail reload` command or restart the server.
* All set! Now you can use all useful [commands](https://github.com/danae/gregorail/wiki/Commands) and event handlers that GregoRail has to offer.

# Issues and feature requests

GregoRail is always open for improvement! Should you encounter a bug while using the plugin or have a feature request, please [create an issue](https://github.com/danae/gregorail/issues) or, if you have some plugin coding experience, make a [pull request](https://github.com/danae/gregorail/pulls). Note that feature requests are judged on a case-by-case basis and are not guaranteed to be implemented immediately or at all.

# License

GregoRail is licensed under the [GPL 3.0](https://github.com/danae/gregorail/blob/master/LICENSE.txt) license.
