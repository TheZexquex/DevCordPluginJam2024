# BuggyBedwars - PluginJam edition

Created in the course of the [DevCord](https://join.devcord.club) plugin Jam 2024 by [das_](https://github.com/dlsf) and TheZexquex

## The projects future
This repository is intended to capture the state of the PluginJam version and will therefore no longer receive updates. No promise, but maybe in the future there will be a version of the plugin that can actually be used on servers
This repository will definitely be updated with a link if this happens.

## Gameplay...
Bedwars, you know, flying islands, teams fighting each other and pushing each other into the void and, above all, beds, the thin thread on which one's life hangs

## ...with a twist?
Bedwars as you know it, or maybe not? 😏
- Wouldn't it be funny if you could change teams during a match?
- How about only being able to destroy your own bed?
- Or an unexpected surprise from the air?
- How about a knocckback stick hat gives YOU knockback?
- Funny special items like the egg brige or an guard that attacs random players? Check

## Testing the plugin

### Requirements
- `Java 21`
- `Paper 1.21.1`
- `FancyNPCs` (https://modrinth.com/plugin/fancynpcs)
  
### Building
To try the plugin you have to compile it from source<br>
First clone the code using `git clone git@github.com:TheZexquex/DevCordPluginJam2024.git`<br>
Then built the jar using `gradlew shadowJar`<br>

### Setup
Currently all bed / team spawn / lobby spawn locations are hardcoded to a specific world<br>
You can either use the map we used, which is available on [Planet Minecraft](https://www.planetminecraft.com/project/hypixel-bedwars-treenan-map/), or you can change the coordinates in the code<br>
There is no config either, for changes to countdowns, messages or item spawners you have to modify the source
