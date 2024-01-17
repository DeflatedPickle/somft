# somft ![Build](https://github.com/DeflatedPickle/somft/actions/workflows/gradle-build.yml/badge.svg)
qol tweaks and vanilla+ additions

<p align="center">
  <a href="https://modrinth.com/mod/somft"><img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg"></a>
  <a href="https://curseforge.com/minecraft/mc-mods/somft"><img alt="curseforge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg"></a>
  <a href="https://github.com/DeflatedPickle/somft"><img alt="github" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/github_vector.svg"></a>
</p>

<p align="center">
  <img alt="quilt" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/supported/quilt_vector.svg">
  <img alt="forge" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/unsupported/forge_vector.svg">
  <img alt="fabric" height="28" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/unsupported/fabric_vector.svg">
</p>

## what it do tho
### changes
- tips on loading screens
- 3d person death screen _(experimental)_
- #### mobs
  - mobs can be set on fire directly
  - mobs on fire spread said fire (gamerule: `doMobFireGrief`)
  - recently fed animals lose interest in food
  - cows need to eat grass between being milked
  - shows arrows stuck in mobs & lets you pull them out
  - feed squids raw fish to make baby squids
  - expanded pet behaviour _(experimental)_
  - creepers are arthropod's
  - tamed parrots can be;
    * healed & mate with seeds
    * picked up by couch+interacting
- #### items
  - shows extra info for held items
  - flowers are edible but disgusting
- #### blocks
  - lit blocks spread fire (gamerule: `doBlockFireGrief`)
  - dispensers can now:
    - fill/empty cauldrons
    - use hoes
    - plant crops
    - milk cows
    - dye sheep
    - carve pumpkins
    - light carved pumpkins
  - projectiles break flower pots
  - cauldrons can be filled with a bucket of fish
  - item frames can be:
    * waxed to stop the item rotating & being popped out
    * crouch+interacted to reset item rotation
- #### armor
  - lower armour stops berry bushes hurting
    * doesn't work for chainmail
  - horse armor can be enchanted
- #### vehicles
  - crouch+interact with vehicles to pick them up
- #### combat
  - entities are knocked back based on their size
- #### hud
  - hides some debug info when not creative/op
  - (most) debug menu text is now translatable
  - debug menu shows more info for targeted entities
- #### gui
  - armour slots on most guis
  - totem slot in inventory _(experimental)_
  - better tooltips
  - customizable hotbar
  - recipe book tab tooltips
  - book editing now:
    * has jump to start/end buttons
    * remove/clear page buttons
### new stuff
- #### items
  - empty inksack item & ink collection from squids
  - quivers!
    * goes on your back
    * stores arrows you pick up
    * only stores arrows
    * they're basically bundles
    * bows only shoot from them
    * bows will not work without one
  - chainmail & netherite horse armour
  - arrow on a lead
    * crafted from any arrow and a lead
    * never lose your arrows again
    * attaches the lead to mobs
    * walk your dog from afar
- #### blocks
  - milk cauldron
    * removes status effects
  - potion cauldrons
  - weather detector
- #### enchantments
  - curse of degradation
    * the applied item takes triple damage
  - curse of malnutrition
    * positive effects of food are divided by 1.2 for each piece of armour affected
- #### hud
  - controls hud (config: `showControls`)
  - lil guy hud (config: `showLilGuy`)
  - tools hud (config: `showTools`)
- ### gui
  - pet management gui _(experimental)_
  - noteblock gui _(experimental)_
  - armor stand gui _(experimental)_
- ### commands
  - health \[add, set, query]
  - hunger \[add, set, query]

## it kinda similar to (and might reimplement features from)
- [evil minecraft](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1275682-1-2-5-evilminecraft-v0-666-bugfix-released)
- [the right branch of development](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/2770122-the-right-branch-of-development-a-complete-new)
- [quark](https://curseforge.com/minecraft/mc-mods/quark)
- [charm](https://www.curseforge.com/minecraft/mc-mods/charm)
- [supplementaries](https://www.curseforge.com/minecraft/mc-mods/supplementaries)
